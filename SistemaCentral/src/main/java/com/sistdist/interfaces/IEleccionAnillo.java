package com.sistdist.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
/**
 *
 * @author lucianafigueroa
 */
public interface IEleccionAnillo extends Remote {

    void iniciarEleccion(List<Integer> idsParticipantes) throws RemoteException;
    void coordinadorElegido(int idCoordinador, String direccionCoordinador) throws RemoteException;
    void estoyVivo(int id) throws RemoteException;
    Boolean soyCoordinador() throws RemoteException;
}