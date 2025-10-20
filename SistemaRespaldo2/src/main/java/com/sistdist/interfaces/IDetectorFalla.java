package com.sistdist.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author dgera
 */

public interface IDetectorFalla extends Remote {

    void pulso() throws RemoteException;
}