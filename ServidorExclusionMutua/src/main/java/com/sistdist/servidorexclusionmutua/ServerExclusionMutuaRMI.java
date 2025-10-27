package com.sistdist.servidorexclusionmutua;

import com.sistdist.interfaces.IServicioExclusionMutua;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
/**
 *
 * @author lucianafigueroa
 */
public class ServerExclusionMutuaRMI extends UnicastRemoteObject implements IServicioExclusionMutua {
    //los estados segun quien este usando la bomba
    private static final int ESTADO_LIBRE = 0;
    private static final int ESTADO_RIEGO = 1;
    private static final int ESTADO_FERTIRRIGACION = 2;

    private int parcelasRegandoCount = 0; //contador de parcelas regando
    private final Object BOMBA_LOCK = new Object(); //objeto bloqueante
    private int bombaEnUso = ESTADO_LIBRE;

    public ServerExclusionMutuaRMI() throws RemoteException {
        super();
    }

    @Override
    public void liberarRecursos(){
        parcelasRegandoCount = 0;
        bombaEnUso = ESTADO_LIBRE;
    }

    @Override
    public void solicitarBombaRiego(int idParcela) throws RemoteException, InterruptedException {
        synchronized (BOMBA_LOCK) {
            while (bombaEnUso == ESTADO_FERTIRRIGACION) {
                System.out.println("⏳ [El riego de la parcela " + idParcela + "] Espera. Bomba ocupada por FERTIRRIGACION.");
                BOMBA_LOCK.wait();
            }
            if (bombaEnUso == ESTADO_LIBRE) {
                bombaEnUso = ESTADO_RIEGO;
            }
            parcelasRegandoCount++;
            System.out.println("🔒 [Riego Parcela " + idParcela + "] Bomba ADQUIRIDA. Regando parcelas: " + parcelasRegandoCount);
        }
    }

    @Override
    public void solicitarBombaFertirrigacion() throws RemoteException, InterruptedException {
        synchronized (BOMBA_LOCK) {
            while (bombaEnUso != ESTADO_LIBRE) {
                System.out.println("⏳ [Fertirrigacion] Espera. Bomba ocupada por: " + (bombaEnUso == ESTADO_RIEGO ? "RIEGO" : "OTRA FERTIRRIGACION"));
                BOMBA_LOCK.wait();
            }
            bombaEnUso = ESTADO_FERTIRRIGACION;
            System.out.println("🔒 [Fertirrigacion] Bomba ADQUIRIDA. Exclusion Mutua activa.");
        }
    }

    @Override
    public void liberarBomba(int liberadorId) throws RemoteException {
        synchronized (BOMBA_LOCK) {
            if (liberadorId == ESTADO_RIEGO) {
                parcelasRegandoCount--;

                if (parcelasRegandoCount <= 0) {
                    parcelasRegandoCount = 0;
                    bombaEnUso = ESTADO_LIBRE;
                    System.out.println("🔓 [Riego] Ultima parcela LIBERO la bomba.");
                    BOMBA_LOCK.notifyAll();
                } else {
                    System.out.println("🔓 [Riego] Parcela liberada. Restan regando: " + parcelasRegandoCount);
                }

            } else if (liberadorId == ESTADO_FERTIRRIGACION) {
                if (bombaEnUso == ESTADO_FERTIRRIGACION) {
                    bombaEnUso = ESTADO_LIBRE;
                    System.out.println("🔓 [Fertirrigacion] LIBERO la bomba. Notificando a todos.");
                    BOMBA_LOCK.notifyAll();
                }
            }
        }
    }
}