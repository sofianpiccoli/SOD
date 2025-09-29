/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sistdist.sensortemperatura;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;


/**
 *
 * @author sofianietopiccoli
 */
public class HiloSensado extends Thread{
    private boolean on; // Indica si el sensor está encendido
    private double temperatura = 25.0; // Última lectura de temperatura
    Socket cnxServidor; // Conexión al sistema central
    PrintWriter pw; // Canal de salida para enviar datos
    private Random rand = new Random();
    
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
    
    // Genera la próxima temperatura a enviar con Distribución Normal
    public double generarTemperatura() {
        double media = 25;     // Temperatura promedio
        double desviacion = 5; // Qué tan dispersos son los valores

        double t = media + rand.nextGaussian() * desviacion;

        // Limitar valores extremos
        if (t < 15) t = 15;
        if (t > 40) t = 40;

        return t;
    }
    
    // Genera la próxima temperatura a enviar, con una pequeña variación aleatoria
    /*public double generarTemperatura(){
        double cambio = (Math.random()*2 - 1); // Variación entre -1 y +1
        double t = temperatura + cambio; // Se suma al valor actual
        if (t > 40){ // Límite máximo = 40°C
            t = 40;
        }
        return t;
    }*/
    
    public double getTemperatura(){
        return temperatura;  // Devuelve el valor actual
    }
    
    public void run(){
        on = true;
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
