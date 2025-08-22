/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.sistdist.sensorhumedad;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sofianietopiccoli
 */
public class SensorHumedad {
    public static void main(String[] args) {
        HiloSensado sensor = new HiloSensado();
        sensor.start();
        try {
            Thread.sleep(15000);
        } catch (InterruptedException ex) {
            Logger.getLogger(SensorHumedad.class.getName()).log(Level.SEVERE, null, ex);
        }
        sensor.apagar();
    }
}
