/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.sistdist.sensorradiacion;

/**
 *
 * @author sofianietopiccoli
 */
public class SensorRadiacion {

    public static void main(String[] args) {
        HiloSensado sensorR  = new HiloSensado();
        sensorR.start();
        try {
            Thread.sleep(15000);
        } catch (InterruptedException ex) {
            System.getLogger(SensorRadiacion.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
}
