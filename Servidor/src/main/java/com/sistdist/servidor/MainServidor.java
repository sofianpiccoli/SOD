/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.sistdist.servidor;

/**
 *
 * @author sofianietopiccoli
 */
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class MainServidor {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("ExclusionMutua", new ServidorExclusion());
            System.out.println("Servidor de Exclusión Mutua listo.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}