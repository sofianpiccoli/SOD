/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sistdist.servidor;

/**
 *
 * @author sofianietopiccoli
 */
import InterfacesRMI.IExclusion;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ServidorExclusion extends UnicastRemoteObject implements IExclusion {

    private boolean bombaOcupada = false;

    protected ServidorExclusion() throws RemoteException {
        super();
    }

    @Override
    public synchronized void pedirBomba(String tipo) throws RemoteException {
        System.out.println(tipo + " quiere usar la bomba.");
        while (bombaOcupada) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        bombaOcupada = true;
        System.out.println(tipo + " está usando la bomba.");
    }

    @Override
    public synchronized void liberarBomba(String tipo) throws RemoteException {
        bombaOcupada = false;
        System.out.println(tipo + " liberó la bomba.");
        notifyAll();
    }
}