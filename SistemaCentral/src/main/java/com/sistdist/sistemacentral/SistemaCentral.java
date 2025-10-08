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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SistemaCentral {

    private static final double W1 = 0.5;
    private static final double W2 = 0.3;
    private static final double W3 = 0.2;

    // Mapas concurrentes
    private static Map<Integer, HiloHumedad> humedades = new ConcurrentHashMap<>();
    private static Map<Integer, PrintWriter> valvulas = new ConcurrentHashMap<>();
    private static Map<Integer, HiloParcela> parcelas = new ConcurrentHashMap<>();

    private static HiloLluvia lluvia = null;
    private static HiloTemperatura temperatura = null;
    private static HiloRadiacion radiacion = null;

    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(20000);
            System.out.println("Sistema central esperando dispositivos......");

            while (true) {
                Socket s = server.accept();
                BufferedReader bf = new BufferedReader(new InputStreamReader(s.getInputStream()));
                String tipoDispositivo = bf.readLine();
               
                switch (tipoDispositivo) {
                    // Sensores de humedad
                    case "sensorHumedad1":
                        System.out.println("Sensor de humedad 1 conectado");
                        HiloHumedad hum1 = new HiloHumedad(s);
                        hum1.start();
                        humedades.put(1, hum1);
                        crearHiloParcelaSiPosible(1);
                        break;
                    case "sensorHumedad2":
                        System.out.println("Sensor de humedad 2 conectado");
                        HiloHumedad hum2 = new HiloHumedad(s);
                        hum2.start();
                        humedades.put(2, hum2);
                        crearHiloParcelaSiPosible(2);
                        break;
                    case "sensorHumedad3":
                        System.out.println("Sensor de humedad 3 conectado");
                        HiloHumedad hum3 = new HiloHumedad(s);
                        hum3.start();
                        humedades.put(3, hum3);
                        crearHiloParcelaSiPosible(3);
                        break;
                    case "sensorHumedad4":
                        System.out.println("Sensor de humedad 4 conectado");
                        HiloHumedad hum4 = new HiloHumedad(s);
                        hum4.start();
                        humedades.put(4, hum4);
                        crearHiloParcelaSiPosible(4);
                        break;
                    case "sensorHumedad5":
                        System.out.println("Sensor de humedad 5 conectado");
                        HiloHumedad hum5 = new HiloHumedad(s);
                        hum5.start();
                        humedades.put(5, hum5);
                        crearHiloParcelaSiPosible(5);
                        break;

                    // Sensores globales
                    case "sensorTemperatura":
                        System.out.println("Sensor de temperatura conectado");
                        HiloTemperatura tem = new HiloTemperatura(s);
                        tem.start();
                        temperatura = tem;
                        crearHilosParcelaExistentes();
                        break;
                    case "sensorLluvia":
                        System.out.println("Sensor de lluvia conectado");
                        HiloLluvia lluv = new HiloLluvia(s);
                        lluv.start();
                        lluvia = lluv;
                        crearHilosParcelaExistentes();
                        break;
                    case "sensorRadiacion":
                        System.out.println("Sensor de radiacion conectado");
                        HiloRadiacion rad = new HiloRadiacion(s);
                        rad.start();
                        radiacion = rad;
                        crearHilosParcelaExistentes();
                        break;

                    // Válvulas
                    case "electroValvula1":
                        System.out.println("Electrovalvula 1 conectada");
                        PrintWriter pw1 = new PrintWriter(s.getOutputStream(), true);
                        valvulas.put(1, pw1);
                        crearHiloParcelaSiPosible(1);
                        break;
                    case "electroValvula2":
                        System.out.println("Electrovalvula 2 conectada");
                        PrintWriter pw2 = new PrintWriter(s.getOutputStream(), true);
                        valvulas.put(2, pw2);
                        crearHiloParcelaSiPosible(2);
                        break;
                    case "electroValvula3":
                        System.out.println("Electrovalvula 3 conectada");
                        PrintWriter pw3 = new PrintWriter(s.getOutputStream(), true);
                        valvulas.put(3, pw3);
                        crearHiloParcelaSiPosible(3);
                        break;
                    case "electroValvula4":
                        System.out.println("Electrovalvula 4 conectada");
                        PrintWriter pw4 = new PrintWriter(s.getOutputStream(), true);
                        valvulas.put(4, pw4);
                        crearHiloParcelaSiPosible(4);
                        break;
                    case "electroValvula5":
                        System.out.println("Electrovalvula 5 conectada");
                        PrintWriter pw5 = new PrintWriter(s.getOutputStream(), true);
                        valvulas.put(5, pw5);
                        crearHiloParcelaSiPosible(5);
                        break;
                }
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // Crea el hilo de la parcela si todos los elementos están disponibles
    private static void crearHiloParcelaSiPosible(int id) {
        if (humedades.containsKey(id) && valvulas.containsKey(id) && !parcelas.containsKey(id)) {
            HiloParcela hilo = new HiloParcela(
                    id,
                    lluvia,
                    radiacion,
                    temperatura,
                    humedades.get(id),
                    valvulas.get(id)
            );
            hilo.start();
            parcelas.put(id, hilo);
        }
    }

    // Crea hilos de parcelas existentes si antes solo faltaban sensores globales
    private static void crearHilosParcelaExistentes() {
        for (int id : humedades.keySet()) {
            if (!parcelas.containsKey(id) && valvulas.containsKey(id)) {
                crearHiloParcelaSiPosible(id);
            }
        }
    }

    // Cálculo del INR
    public static double calculoINR(double H, double T, double R) {
        return W1 * (1 - H / 100) + W2 * (T / 40) + W3 * (R / 1000);
    }

    public static boolean decidirRiego(boolean L, double INR) {
        return (INR > 0.7 && !L);
    }

    public static int tiempoRiego(double inr) {
        if (inr >= 0.9) return 10;
        else if (inr >= 0.8) return 7;
        else return 5;
    }
}
