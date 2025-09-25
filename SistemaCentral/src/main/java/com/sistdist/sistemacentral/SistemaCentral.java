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
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author lucianafigueroa
 */
public class SistemaCentral {
    
    // Pesos para el cálculo de INR
    private static final double W1 = 0.5;
    private static final double W2 = 0.3;
    private static final double W3 = 0.2;

    public static void main(String[] args) {
        
        // Mapas concurrentes para manejar datos en paralelo
        // Guarda los sensores de humedad por parcela 
        Map<Integer, HiloHumedad> humedades = new ConcurrentHashMap<>(); 
        // Guarda las conexiones a las electrovalvulas por parcela
        Map<Integer, PrintWriter> valvulas = new ConcurrentHashMap<>(); 
        
        String tipoDispositivo = " ";
        
        // Se inicializan hilos para los sensores principales
        HiloLluvia lluvia = null;
        HiloRadiacion radiacion = null;
        HiloTemperatura temperatura = null;

        try {
            // Servidor central en el puerto 20000
            ServerSocket server = new ServerSocket(20000);
            
            // Hilo principal que decide riego. Arranco el hilo de riego una vez
            HiloRiego riego = new HiloRiego(0.5, 0.3, 0.2, lluvia, radiacion, temperatura, humedades, valvulas);
            riego.setValvulas(valvulas);
            riego.start();
            
            // Espera conexiones de sensores y válvulas
            while (true) {
                Socket s = server.accept(); //puerto aleatorio misma dir de internet local
                BufferedReader bf = new BufferedReader(new InputStreamReader(s.getInputStream()));
                tipoDispositivo = bf.readLine(); // Identificación del dispositivo
                
                // Dependiendo del tipo de dispositivo conectado, se crea un hilo
                switch (tipoDispositivo) {
                    case "sensorHumedad1":
                        System.out.println("soy el sensor de humedad 1");
                        HiloHumedad hum1 = new HiloHumedad(s);
                        hum1.start();
                        // Se almacena el sensor de la parcela 1
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
                        riego.setTemperatura(tem);
                        break;
                    case "sensorLluvia":
                        HiloLluvia lluv = new HiloLluvia(s);
                        lluv.start();
                        riego.setLluvia(lluv);
                        break;
                    case "sensorRadiacion":
                        HiloRadiacion rad = new HiloRadiacion(s);
                        rad.start();
                        riego.setRadiacion(rad);
                        break;
                    case "electroValvula1":
                        System.out.println("soy la electrovalvula 1");
                        PrintWriter pw1 = new PrintWriter(s.getOutputStream(), true);
                        // Almacena la conexión de la válvula 1
                        valvulas.put(1, pw1);
                        break;
                    case "electroValvula2":
                        System.out.println("soy la electrovalvula 2");
                        PrintWriter pw2 = new PrintWriter(s.getOutputStream(), true);
                        valvulas.put(2, pw2);
                        break;
                    case "electroValvula3":
                        System.out.println("soy la electrovalvula 3");
                        PrintWriter pw3 = new PrintWriter(s.getOutputStream(), true);
                        valvulas.put(3, pw3);
                        break;
                    case "electroValvula4":
                        System.out.println("soy la electrovalvula 4");
                        PrintWriter pw4 = new PrintWriter(s.getOutputStream(), true);
                        valvulas.put(4, pw4);
                        break;
                    case "electroValvula5":
                        System.out.println("soy la electrovalvula 5");
                        PrintWriter pw5 = new PrintWriter(s.getOutputStream(), true);
                        valvulas.put(5, pw5);
                        break;

                }
            }
        } catch (IOException ex) {
            System.getLogger(SistemaCentral.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }

    }

    // Cálculo del INR (usa humedad, temperatura y radiación)
    public static double calculoINR(double H, double T, double R) {
        return W1 * (1 - H / 100) + W2 * (T / 40) + W3 * (R / 1000);
    }

    // Decide si se riega o no (según INR y lluvia)
    public static boolean decidirRiego(boolean L, double INR) {
        return (INR > 0.7 && !L);
    }

    // Determina cuánto tiempo regar (dependiendo del INR)
    public static int tiempoRiego(double inr) {
        if (inr >= 0.9) {
            return 10;
        } else if (inr >= 0.8) {
            return 7;
        } else {
            return 5;
        }
    }
}
