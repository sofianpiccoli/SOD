/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package InterfacesRMI;

/**
 *
 * @author sofianietopiccoli
 */
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IExclusion extends Remote {
    void pedirBomba(String tipo) throws RemoteException;
    void liberarBomba(String tipo) throws RemoteException;
}