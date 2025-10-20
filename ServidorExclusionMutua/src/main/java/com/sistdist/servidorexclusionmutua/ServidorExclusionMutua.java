package com.sistdist.servidorexclusionmutua;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author lucianafigueroa
 */
public class ServidorExclusionMutua {

    public static void main(String[] args) {
        String nombreServidor = "ServidorMaestro";
        int nroPuerto = 9000;
        int nroPuertoExcMutua = 10000;

        try {
            LocateRegistry.createRegistry(nroPuertoExcMutua);
            System.out.println("Registry de Exclusion Mutua listo en el puerto " + nroPuertoExcMutua);
            ServerExclusionMutuaRMI serverEM = new ServerExclusionMutuaRMI();
            Naming.rebind("rmi://localhost:" + nroPuertoExcMutua  + "/servidorCentralEM", serverEM);
            System.out.println("✅ Servidor de Exclusion Mutua para la bomba registrado y listo.");

        } catch (RemoteException ex) {
            Logger.getLogger(ServidorExclusionMutua.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(ServidorExclusionMutua.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}