/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package com.sistdist.bomba;

/**
 *
 * @author miria
 */
import interfacesRMI.IExclusion;
import java.rmi.Naming;

public class MainBomba {
    public static void main(String[] args) {
        try {
            IExclusion servidor = (IExclusion) Naming.lookup("//localhost/ServidorExclusionMutua");
            HiloRi hiloRiego = new HiloRi(servidor);
            hiloRiego.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}