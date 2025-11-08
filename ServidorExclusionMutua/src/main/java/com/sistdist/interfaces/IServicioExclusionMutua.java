package com.sistdist.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author lucianafigueroa
 */
public interface IServicioExclusionMutua extends Remote {

    void solicitarBombaRiego(int idParcela) throws RemoteException, InterruptedException;

    void solicitarBombaFertirrigacion() throws RemoteException, InterruptedException;

    void liberarBomba(int liberadorId) throws RemoteException;

    void liberarRecursos() throws RemoteException;
}