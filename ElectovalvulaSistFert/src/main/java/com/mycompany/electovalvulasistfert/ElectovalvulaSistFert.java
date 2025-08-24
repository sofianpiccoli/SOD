/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.electovalvulasistfert;

/**
 *
 * @author dgera
 */
public class ElectovalvulaSistFert {

     public static void main(String[] args) {
        HiloValvula electrovalvulaSF = new HiloValvula();
        electrovalvulaSF.start();
        try {
            Thread.sleep(15000);
        } catch (InterruptedException ex) {
            System.getLogger(ElectovalvulaSistFert.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        electrovalvulaSF.apagar();
    }
}
