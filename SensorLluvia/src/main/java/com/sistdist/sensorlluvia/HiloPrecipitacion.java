/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sistdist.sensorlluvia;

/**
 *
 * @author sofianietopiccoli
 */
public class HiloPrecipitacion extends Thread {
    private boolean on;
    private boolean lluvia;
    
    public HiloPrecipitacion(){
        on = true;
    }
    
    public void encender(){
        on = true;
    }
    
    public void apagar(){
        on = false;
    }
    
    public boolean generarLluvia(){
        boolean L = false;
        if (Math.random() < 0.3) {
            L = true;
        }
        return L; 
    }
    
    public void run(){
        on=true;
        while (on){
            lluvia = generarLluvia();
            System.out.println("Llueve = "+lluvia);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                System.getLogger(HiloPrecipitacion.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        }
    }
}
