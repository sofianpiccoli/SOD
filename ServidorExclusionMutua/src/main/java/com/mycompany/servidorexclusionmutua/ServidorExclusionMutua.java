/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.servidorexclusionmutua;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
import java.util.Queue;
import interfaces.IServidorExclusion;
import interfaces.IDetectorFalla;

public class ServidorExclusionMutua extends UnicastRemoteObject implements IServidorExclusion {

    private boolean enUso = false;
    private String procesoActual = null;
    private final Queue<String> cola = new LinkedList<>();

    public ServidorExclusionMutua() throws RemoteException {
        super();
    }

    @Override
    public synchronized boolean requestAccess(String sistema) throws RemoteException {
        if (!enUso) {
            enUso = true;
            procesoActual = sistema;
            System.out.println("[Servidor] Acceso concedido a: " + sistema);
            return true;
        } else {
            cola.add(sistema);
            System.out.println("[Servidor] Recurso ocupado. " + sistema + " encolado.");
            return false;
        }
    }

    @Override
    public synchronized void releaseAccess(String sistema) throws RemoteException {
        if (enUso && sistema.equals(procesoActual)) {
            if (!cola.isEmpty()) {
                String siguiente = cola.poll();
                procesoActual = siguiente;
                System.out.println("[Servidor] " + sistema + " liberó. Siguiente en cola: " + siguiente);
            } else {
                enUso = false;
                procesoActual = null;
                System.out.println("[Servidor] " + sistema + " liberó. Recurso libre.");
            }
        } else {
            System.out.println("[Servidor] " + sistema + " intentó liberar sin permiso.");
        }
    }

    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(1100);
            ServidorExclusionMutua servidor = new ServidorExclusionMutua();
            Naming.rebind("rmi://localhost:1100/ServidorExclusionMutua", servidor);
           
            DetectorFallo detector = new DetectorFallo();
            Naming.rebind("rmi://localhost:1100/ServidorExclusionMutua", detector);
            System.out.println("[Servidor] Servidor de exclusión mutua y detector listos.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
