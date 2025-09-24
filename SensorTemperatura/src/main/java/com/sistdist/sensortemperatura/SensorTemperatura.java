/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.sistdist.sensortemperatura;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.Duration;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sofianietopiccoli
 */
public class SensorTemperatura {

    public static void main(String[] args) {
        InetAddress IPServidor;
        PrintWriter pw;
        try {
            // Conecta al sistema central en localhost:20000
            IPServidor = InetAddress.getByName("127.0.0.1"); //localhost
            Socket cliente = new Socket(IPServidor, 20000);
            
            // Se identifica como sensor de temperatura
            pw = new PrintWriter(cliente.getOutputStream());
            pw.println("sensorTemperatura");
            pw.flush();
            
            // Crea un hilo que simula las lecturas y envía datos al sistema central
            HiloSensado sensorT = new HiloSensado(cliente, pw);
            sensorT.start();
        } catch (UnknownHostException ex) {
            Logger.getLogger(SensorTemperatura.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SensorTemperatura.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
