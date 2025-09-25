/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sistdist.sensorradiacion;

import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author sofianietopiccoli
 */
public class HiloSensado extends Thread{
    private boolean on;
    private double radiacion; // Última lectura de radiación
    Socket cnxServidor;   // Conexión al sistema central
    PrintWriter pw;   // Canal de salida para enviar datos
    
    public HiloSensado(Socket s, PrintWriter imp){
        on = false;
        cnxServidor = s;   // Guarda la conexión al servidor
        pw = imp;   // Guarda el canal de escritura
    }
    
    public void encender(){
        on = true;
    }
    
    public void apagar(){
        on = false;
    }
    
    // Genera un valor de radiación aleatorio entre 0 y 1000
    public double generarRadiacion(){
        return (Math.random() * 1000);
    }
    
    public double getRadiacion(){
        return radiacion;
    }
    
    @Override
    public void run(){
        on = true;
        while (on){
            // Genera y guarda un nuevo valor de radiación
            radiacion = generarRadiacion();
            
            // Envía el valor al sistema central
            pw.println(radiacion);
            pw.flush();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                System.getLogger(HiloSensado.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        }
    }
}
