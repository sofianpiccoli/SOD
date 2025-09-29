/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.sistdist.sensorhumedad;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sofianietopiccoli
 */
public class SensorHumedad {
    
    
    public static void main(String[] args) throws IOException {
        InetAddress IPServidor;
        PrintWriter pw;
        try {
            // Se conecta al Sistema Central en la dirección localhost y puerto 20000
            IPServidor = InetAddress.getByName("127.0.0.1"); //localhost
            Socket cliente = new Socket(IPServidor, 20000);
            
            // Se prepara un canal de escritura hacia el Sistema Central
            pw = new PrintWriter(cliente.getOutputStream());
            
            // Se envía la identificación del sensor (ej: "sensorHumedad1")
            pw.println("sensorHumedad1");
            
            // flush fuerza el envío inmediato del mensaje (evita que quede en el buffer)
            pw.flush();
            
            // Inicia el hilo que genera y envía datos simulados del sensor al Sistema Central
            HiloSensado sensor = new HiloSensado(cliente, pw);
            sensor.start();
        } catch (UnknownHostException ex) {
            Logger.getLogger(SensorHumedad.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SensorHumedad.class.getName()).log(Level.SEVERE, null, ex);
        }/*catch (UnknownHostException ex) {
            System.getLogger(SensorHumedad.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SensorHumedad.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        
    }
}