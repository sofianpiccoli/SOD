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
                    case "sensorHumedad":
                        System.out.println("pasa por aqui");
                        HiloHumedad hum = new HiloHumedad(s);
                        hum.start();
                        break;
                    case "sensorTemperatura":
                        System.out.println("pasa por aqui");
                        HiloTemperatura tem = new HiloTemperatura(s);
                        tem.start();
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
