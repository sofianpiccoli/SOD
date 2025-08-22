/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.sistdist.sensorlluvia;

/**
 *
 * @author sofianietopiccoli
 */
public class SensorLluvia {

    public static void main(String[] args) {
        HiloPrecipitacion sensor = new HiloPrecipitacion();
        sensor.start();
        try {
            Thread.sleep(15000);
        } catch (InterruptedException ex) {
            System.getLogger(SensorLluvia.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
                
    }
}
