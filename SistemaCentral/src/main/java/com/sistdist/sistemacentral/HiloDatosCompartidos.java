/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sistdist.sistemacentral;

/**
 *
 * @author sofianietopiccoli
 */
public class HiloDatosCompartidos extends Thread {
    private double temperatura;
    private double radiacion;
    private boolean lluvia;
    private boolean on;
    
    public HiloDatosCompartidos(){
        this.on = false;
    }

    public double getTemperatura() {
        return temperatura;
    }

    public double getRadiacion() {
        return radiacion;
    }

    public boolean isLluvia() {
        return lluvia;
    }
    
    public void apagar(){
        on=false;
    }
    
    public void leerDatos(){
        
    }
    
    public double generarTemperaturaAleatoria(){
        double cambio = (Math.random()*2 - 1);
        double t = temperatura + cambio;
        if (t > 40){
            t = 40;
        }
        return t;
    }
    
    public double generarRadiacionAleatoria(){
        return (Math.random() * 1000);
    }
    
    public boolean generarLluviaAleatoria(){
        boolean L = false;
        if (Math.random() < 0.3) {
            L = true;
        }
        return L; 
    }
    
    @Override
    public void run(){
        on = true;
        temperatura=30;
        while (on){
            temperatura = generarTemperaturaAleatoria();
            radiacion = generarRadiacionAleatoria();
            lluvia = generarLluviaAleatoria();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                System.getLogger(HiloDatosCompartidos.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        }
    }
}
