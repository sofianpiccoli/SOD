/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sistdist.sensortemperatura;

import java.lang.Math.*;

/**
 *
 * @author sofianietopiccoli
 */
public class HiloSensado extends Thread{
    private boolean on;
    private double temperatura;
    
    public HiloSensado(){
        on = false;
    }
    
    public void encender(){
        on = true;
    }
    
    public void apagar(){
        on = false;
    }
    
    public double generarTemperatura(){
        double cambio = (Math.random()*2 - 1);
        double t = temperatura + cambio;
        if (t > 40){
            t = 40;
        }
        return t;
    }
    
    public double leerTemperatura(){
        return temperatura;
    }
    
    public void run(){
        on = true;
        temperatura=25;
        while (on){
            temperatura = generarTemperatura();
            System.out.println(temperatura+" °C");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                System.getLogger(HiloSensado.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        }
    
    }
}
