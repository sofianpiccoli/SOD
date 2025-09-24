/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sistdist.sensorlluvia;

import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author sofianietopiccoli
 */
public class HiloPrecipitacion extends Thread {
    
    private boolean on;
    private boolean lluvia; // Última lectura (true = llueve, false = no llueve)
    Socket cnxServidor; // Conexión al sistema central
    PrintWriter pw;  // Canal de salida
    
    public HiloPrecipitacion(Socket s, PrintWriter imp){
        on = false;
        cnxServidor = s;
        pw = imp;
    }
    
    public void encender(){
        on = true;
    }
    
    public void apagar(){
        on = false;
    }
    
    // Genera lluvia con probabilidad del 30%
    public boolean generarLluvia(){
        boolean L = false;
        if (Math.random() < 0.3) { // 30% de chance de que "llueva"
            L = true;
        }
        return L; 
    }
    
    public void run(){
        on=true;
        while (on){
            // Genera nueva lectura
            lluvia = generarLluvia();
            
            // Envía el valor al sistema central
            pw.println(lluvia);
            pw.flush();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                System.getLogger(HiloPrecipitacion.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        }
    }
}
