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
    
    public void leerDatos(){
            
        
    }
}
