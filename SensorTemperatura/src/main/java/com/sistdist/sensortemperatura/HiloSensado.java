/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sistdist.sensortemperatura;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.Math.*;
import java.net.Socket;

/**
 *
 * @author miria
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
        temperatura = 29;

        try {
            while (on){
                temperatura = generarTemperatura();

                pw.println(temperatura);
                pw.flush();
                if (pw.checkError()) {
                    System.err.println("❌ HiloSensado: Error de escritura detectado. El servidor ha caído. Terminando...");
                    on = false;
                    break;
                }
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ex) {
                    System.getLogger(HiloSensado.class.getName()).log(System.Logger.Level.WARNING, "Hilo interrumpido.", ex);
                    on = false;
                }
            }
        } finally {
            try {
                if (cnxServidor != null && !cnxServidor.isClosed()) {
                    cnxServidor.close();
                    System.out.println("🔌 HiloSensado: Socket cerrado.");
                }
            } catch (IOException e) {
                System.err.println("Error al cerrar el socket: " + e.getMessage());
            }
        }
    }
}
