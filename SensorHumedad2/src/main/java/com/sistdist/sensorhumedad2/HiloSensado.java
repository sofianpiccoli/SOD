/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sistdist.sensorhumedad2;

import java.lang.Math.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sofianietopiccoli
 */
public class HiloSensado extends Thread {
    
    private boolean on;
    private int humedad;
    
    public HiloSensado(){
        on = false;
    }
    
    public int generarHumedad(){
        return (int) (Math.round(Math.random() * 100));
    }
    
    public void encender(){
        on = true;
    }
    
    public void apagar(){
        on = false;
    }
    
    public double leerHumedad(){
        return humedad;
    }
    
    public void run(){
        on = true;
        //Mide la humedad mientras esté prendido
        while(on){
            humedad = generarHumedad();
            System.out.println(humedad+"%");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                Logger.getLogger(HiloSensado.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}