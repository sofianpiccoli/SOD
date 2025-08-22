/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.sistdist.electrovalvula4;

/**
 *
 * @author sofianietopiccoli
 */
public class Electrovalvula4 {

    public static void main(String[] args) {
        HiloValvula electrovalvula4 = new HiloValvula(4);
            electrovalvula4.start();
            try {
                Thread.sleep(15000);
            } catch (InterruptedException ex) {
                System.getLogger(Electrovalvula4.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
            electrovalvula4.apagar();
    }
}
