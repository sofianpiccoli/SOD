/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.sistdist.sistemafertirrigacion;

/**
 *
 * @author sofianietopiccoli
 */
import interfacesRMI.IExclusion;
import java.rmi.Naming;

public class SistemaFertirrigacion {
    public static void main(String[] args) {
        try {
            IExclusion servidor = (IExclusion) Naming.lookup("//localhost/ServidorExclusionMutua");
            HiloFerti hiloFerti = new HiloFerti(servidor);
            hiloFerti.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}