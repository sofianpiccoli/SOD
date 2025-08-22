/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.sistdist.electrovalvula2;

/**
 *
 * @author sofianietopiccoli
 */
public class Electrovalvula2 {
    

    public static void main(String[] args) {
        HiloValvula electrovalvula2 = new HiloValvula(2);
            electrovalvula2.start();
            try {
                Thread.sleep(15000);
            } catch (InterruptedException ex) {
                System.getLogger(Electrovalvula2.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
            electrovalvula2.apagar();
    }
}
