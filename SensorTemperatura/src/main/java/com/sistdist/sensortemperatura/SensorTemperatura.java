/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.sistdist.sensortemperatura;

import java.time.Duration;

/**
 *
 * @author sofianietopiccoli
 */
public class SensorTemperatura {

    public static void main(String[] args) {
        HiloSensado sensorT = new HiloSensado();
        sensorT.start();
        try {
            Thread.sleep(15000);
        } catch (InterruptedException ex) {
            System.getLogger(SensorTemperatura.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
}
