/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.sistemaferirrigacion;

/**
 *
 * @author dgera
 */
public class SistemaFerirrigacion {

    public static void main(String[] args) {
        HiloElectroValvulaSistFert ferti = new HiloElectroValvulaSistFert();
        ferti.start();
         try {
            Thread.sleep(15000);
        } catch (InterruptedException ex) {
            System.getLogger(SistemaFerirrigacion.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        ferti.apagar();
    }
}
