/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.sistdist.sistemacentral;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author sofianietopiccoli
 */
public class SistemaCentral {

    public static void main(String[] args) {
        String tipoDispositivo = " ";
        try {
            ServerSocket server = new ServerSocket(20000);
            while (true){
                Socket s = server.accept(); //puerto aleatorio misma dir de internet local
                BufferedReader bf = new BufferedReader(new InputStreamReader(s.getInputStream()));
                tipoDispositivo = bf.readLine();
                switch(tipoDispositivo){
                    case "sensorHumedad1":
                        HiloHumedad hum1 = new HiloHumedad(s);
                        hum1.start();
                        break;
                    case "sensorHumedad2":
                        HiloHumedad hum2 = new HiloHumedad(s);
                        hum2.start();
                        break;
                    case "sensorHumedad3":
                        HiloHumedad hum3 = new HiloHumedad(s);
                        hum3.start();
                        break;
                    case "sensorHumedad4":
                        HiloHumedad hum4 = new HiloHumedad(s);
                        hum4.start();
                        break;  
                    case "sensorHumedad5":
                        HiloHumedad hum5 = new HiloHumedad(s);
                        hum5.start();
                        break;
                    case "sensorTemperatura":
                        HiloTemperatura tem = new HiloTemperatura(s);
                        tem.start();
                        break;
                    case "sensorLluvia":
                        HiloLluvia lluv = new HiloLluvia(s);
                        lluv.start();
                        break;
                    case "sensorRadiacion":
                        HiloRadiacion rad = new HiloRadiacion(s);
                        rad.start();
                        break;
                }
                  
            }
        } catch (IOException ex) {
            System.getLogger(SistemaCentral.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        
        /*HiloDatosCompartidos datos = new HiloDatosCompartidos();
        HiloHumedad hum = new HiloHumedad();
        HiloRiego riego = new HiloRiego(0.5, 0.3, 0.2, datos, hum);
        datos.start();
        hum.start();
        riego.start();
        try {
            Thread.sleep(15000);
        } catch (InterruptedException ex) {
            System.getLogger(SistemaCentral.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        riego.apagar();
        hum.apagar();
        datos.apagar();
        */
    }
}
