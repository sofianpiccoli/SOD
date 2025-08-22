/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.sistdist.sensorhumedad5;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sofianietopiccoli
 */
public class SensorHumedad5 {

    public static void main(String[] args) {
        HiloSensado sensor = new HiloSensado();
        sensor.start();
        try {
            Thread.sleep(15000);
        } catch (InterruptedException ex) {
            Logger.getLogger(SensorHumedad5.class.getName()).log(Level.SEVERE, null, ex);
        }
        sensor.apagar();
    }
}
