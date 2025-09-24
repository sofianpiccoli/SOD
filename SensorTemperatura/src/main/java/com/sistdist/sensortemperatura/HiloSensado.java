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
    private boolean on; // Indica si el sensor está encendido
    private double temperatura; // Última lectura de temperatura
    Socket cnxServidor; // Conexión al sistema central
    PrintWriter pw; // Canal de salida para enviar datos
    
    public HiloSensado(Socket s, PrintWriter imp){
        on = false; // Inicialmente apagado
        cnxServidor = s; // Guarda la conexión
        pw = imp;   // Guarda el canal de escritura
    }
    
    public void encender(){
        on = true;
    }
    
    public void apagar(){
        on = false;
    }
    
    // Genera la próxima temperatura a enviar, con una pequeña variación aleatoria
    public double generarTemperatura(){
        double cambio = (Math.random()*2 - 1); // Variación entre -1 y +1
        double t = temperatura + cambio; // Se suma al valor actual
        if (t > 40){ // Límite máximo = 40°C
            t = 40;
        }
        return t;
    }
    
    public double getTemperatura(){
        return temperatura;  // Devuelve el valor actual
    }
    
    public void run(){
        on = true;
        temperatura=29; // Temperatura inicial
        while (on){
            // Genera un nuevo valor de temperatura
            temperatura = generarTemperatura();
            
            // Envía la lectura al sistema central
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
