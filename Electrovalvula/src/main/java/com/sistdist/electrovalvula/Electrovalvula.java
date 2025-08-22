/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.sistdist.electrovalvula;

/**
 *
 * @author sofianietopiccoli
 */
public class Electrovalvula {
    

    public static void main(String[] args) {
        HiloValvula electrovalvula1 = new HiloValvula(1);
        electrovalvula1.start();
        try {
            Thread.sleep(15000);
        } catch (InterruptedException ex) {
            System.getLogger(Electrovalvula.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        electrovalvula1.apagar();
    }
}
