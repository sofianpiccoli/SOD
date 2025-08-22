/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.sistdist.electrovalvula5;

/**
 *
 * @author sofianietopiccoli
 */
public class Electrovalvula5 {

    public static void main(String[] args) {
        HiloValvula electrovalvula5 = new HiloValvula(5);
            electrovalvula5.start();
            try {
                Thread.sleep(15000);
            } catch (InterruptedException ex) {
                System.getLogger(Electrovalvula5.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
            electrovalvula5.apagar();
    }
}
