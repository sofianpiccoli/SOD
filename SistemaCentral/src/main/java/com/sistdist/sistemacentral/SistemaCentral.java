/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.sistdist.sistemacentral;

/**
 *
 * @author sofianietopiccoli
 */
public class SistemaCentral {

    public static void main(String[] args) {
        HiloDatosCompartidos datos = new HiloDatosCompartidos();
        HiloHumedad hum = new HiloHumedad();
        HiloRiego riego = new HiloRiego(0.5, 0.3, 0.2, datos, hum);
        datos.start();
        hum.start();
        riego.start();
        try {
            Thread.sleep(15000);
        } catch (InterruptedException ex) {
            System.getLogger(SistemaCentral.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        riego.apagar();
    }
}
