/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sistdist.sensortemperatura;

import java.io.PrintWriter;
import java.lang.Math.*;
import java.net.Socket;

/**
 *
 * @author sofianietopiccoli
 */
public class HiloSensado extends Thread{
    private boolean on;
    private double temperatura;
    Socket cnxServidor;
    PrintWriter pw;
    
    public HiloSensado(Socket s, PrintWriter imp){
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
    
    public double generarTemperatura(){
        double cambio = (Math.random()*2 - 1);
        double t = temperatura + cambio;
        if (t > 40){
            t = 40;
        }
        return t;
    }
    
    public double getTemperatura(){
        return temperatura;
    }
    
    public void run(){
        on = true;
        temperatura=29;
        while (on){
            temperatura = generarTemperatura();
            pw.println(temperatura);
            pw.flush();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                System.getLogger(HiloSensado.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        }
    
    }
}
