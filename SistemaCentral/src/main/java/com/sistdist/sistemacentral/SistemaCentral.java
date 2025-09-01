/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.sistdist.sistemacentral;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author sofianietopiccoli
 */
public class SistemaCentral {

    public static void main(String[] args) {
        String tipoDispositivo = " ";
        Map<Integer, HiloHumedad> humedades = new HashMap<>();
        Map<Integer, PrintWriter> valvulas = new HashMap<>();
        HiloDatosCompartidos datos = new HiloDatosCompartidos();
        try {
            ServerSocket server = new ServerSocket(20000);
            while (true){
                Socket s = server.accept(); //puerto aleatorio misma dir de internet local
                // Arranco el hilo de riego una vez
                HiloRiego riego = new HiloRiego(0.5, 0.3, 0.2, datos, humedades);
                riego.setValvulas(valvulas);
                riego.start();
                BufferedReader bf = new BufferedReader(new InputStreamReader(s.getInputStream()));
                tipoDispositivo = bf.readLine();
                switch(tipoDispositivo){
                    case "sensorHumedad1":
                        System.out.println("soy el sensor de humedad 1");
                        HiloHumedad hum1 = new HiloHumedad(s);
                        hum1.start();
                        humedades.put(1, hum1); 
                        break;
                    case "sensorHumedad2":
                        System.out.println("soy el sensor de humedad 2");
                        HiloHumedad hum2 = new HiloHumedad(s);
                        hum2.start();
                        humedades.put(2, hum2); 
                        break;
                    case "sensorHumedad3":
                        System.out.println("soy el sensor de humedad 3");
                        HiloHumedad hum3 = new HiloHumedad(s);
                        hum3.start();
                        humedades.put(3, hum3); 
                        break;
                    case "sensorHumedad4":
                        System.out.println("soy el sensor de humedad 4");
                        HiloHumedad hum4 = new HiloHumedad(s);
                        hum4.start();
                        humedades.put(4, hum4); 
                        break;  
                    case "sensorHumedad5":
                        System.out.println("soy el sensor de humedad 5");
                        HiloHumedad hum5 = new HiloHumedad(s);
                        hum5.start();
                        humedades.put(5, hum5); 
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
                    case "HelectroValvula1":
                        System.out.println("soy la electrovalvula 1");
                       PrintWriter pw1 = new PrintWriter(s.getOutputStream(), true);
                       valvulas.put(1, pw1);
                       break;
                    case "HelectroValvula2":
                        System.out.println("soy la electrovalvula 2");
                       PrintWriter pw2 = new PrintWriter(s.getOutputStream(), true);
                       valvulas.put(2, pw2);
                       break;
                    case "HelectroValvula3":
                        System.out.println("soy la electrovalvula 3");
                       PrintWriter pw3 = new PrintWriter(s.getOutputStream(), true);
                       valvulas.put(3, pw3);
                       break;
                    case "HelectroValvula4":
                        System.out.println("soy la electrovalvula 4");
                       PrintWriter pw4 = new PrintWriter(s.getOutputStream(), true);
                       valvulas.put(4, pw4);
                       break;
                    case "HelectroValvula5":
                        System.out.println("soy la electrovalvula 5");
                       PrintWriter pw5 = new PrintWriter(s.getOutputStream(), true);
                       valvulas.put(5, pw5);
                       break;

                }}} catch (IOException ex) {
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
