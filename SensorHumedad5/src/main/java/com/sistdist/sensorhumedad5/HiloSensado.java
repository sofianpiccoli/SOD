/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sistdist.sensorhumedad5;

import java.io.PrintWriter;
import java.lang.Math.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sofianietopiccoli
 */
public class HiloSensado extends Thread {
    
    private boolean on;
    private double humedad;  // Última lectura de humedad (%)
    Socket cnxServidor;   // Conexión con el sistema central
    PrintWriter pw;   // Canal de salida
    
    public HiloSensado(Socket s, PrintWriter imp){
        on = false;
        cnxServidor = s;   // Guarda la conexión al servidor
        pw = imp;    // Guarda el canal de escritura
    }
    
     // Genera un valor de humedad entre 0 y 100 % (redondeado)
    public double generarHumedad(){
        return (Math.round(Math.random() * 100));
    }
    
    public void encender(){
        on = true;
    }
    
    public void apagar(){
        on = false;
    }
    
    public double getHumedad(){
        return humedad;
    }
    
    public void run(){
        on = true;
        //Mide la humedad mientras esté prendido
        while(on){
            humedad = generarHumedad();  // Calcula nueva humedad
            System.out.println(getHumedad());
            // Envía el valor al sistema central
            pw.println(humedad);
            pw.flush();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                Logger.getLogger(HiloSensado.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}