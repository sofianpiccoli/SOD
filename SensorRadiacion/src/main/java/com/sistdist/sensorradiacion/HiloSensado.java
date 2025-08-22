/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sistdist.sensorradiacion;

/**
 *
 * @author sofianietopiccoli
 */
public class HiloSensado extends Thread{
    private boolean on;
    private double radiacion;
    
    public HiloSensado() {
        on = false;
    }
    
    public void encender(){
        on = true;
    }
    
    public double generarRadiacion(){
        return (Math.random() * 1000);
    }
    
    public double leerRadiacion(){
        return radiacion;
    }
    
    @Override
    public void run(){
        on = true;
        while (on){
            radiacion = generarRadiacion();
            System.out.println("Radiacion: "+radiacion+" W/m²");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                System.getLogger(HiloSensado.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        }
    }
}
