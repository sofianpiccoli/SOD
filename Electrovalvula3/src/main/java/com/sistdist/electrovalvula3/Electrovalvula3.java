/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.sistdist.electrovalvula3;

/**
 *
 * @author sofianietopiccoli
 */
public class Electrovalvula3 {

    public static void main(String[] args) {
        HiloValvula electrovalvula3 = new HiloValvula(3);
            electrovalvula3.start();
            try {
                Thread.sleep(15000);
            } catch (InterruptedException ex) {
                System.getLogger(Electrovalvula3.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
            electrovalvula3.apagar();
    }
}
