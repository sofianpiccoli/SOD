/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.sistemafertirrigacion;
import interfaces.*;
import java.io.*;
import java.net.*;
import java.rmi.Naming;
import java.util.*;


public class SistemaFertirrigacion {

    public static void main(String[] args) {
        Map<Integer, PrintWriter> valvulasFert = new HashMap<>();

        try {
            IServidorExclusion servidorExclusion =(IServidorExclusion) Naming.lookup("rmi://localhost:1100/ServidorExclusionMutua");
            HiloFertirrigacion ferti = new HiloFertirrigacion(valvulasFert) {
                @Override
                public void run() {
                    while (true) {
                        try {
                            // Intentamos usar la bomba para fertirrigación
                            boolean acceso = servidorExclusion.requestAccess("Fertirrigacion");
                            if (acceso) {
                                System.out.println("[Fertirrigacion] Acceso concedido a la bomba. Fertirrigación en ejecución...");
                                Thread.sleep(5000); // simulación de tiempo de fertirrigación
                                System.out.println("[Fertirrigacion] Fertirrigación finalizada. Liberando bomba...");
                                servidorExclusion.releaseAccess("Fertirrigacion");
                            } else {
                                System.out.println("[Fertirrigacion] Bomba ocupada. Reintentando en 3s...");
                                Thread.sleep(3000);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            };

            ferti.start();

            // Servidor para recibir conexiones de válvulas
            try (ServerSocket server = new ServerSocket(21000)) {
                while (true) {
                    Socket s = server.accept();
                    BufferedReader bf = new BufferedReader(new InputStreamReader(s.getInputStream()));
                    String tipoDispositivo = bf.readLine();
                    if ("valvulaFert".equals(tipoDispositivo)) {
                        PrintWriter pw = new PrintWriter(s.getOutputStream(), true);
                        valvulasFert.put(1, pw);
                    }
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}















/**
 *
 * @author lucianafigue
 */
/*public class SistemaFertirrigacion {

    public static void main(String[] args) {
        Map<Integer, PrintWriter> valvulasFert = new HashMap<>();
        HiloFertirrigacion ferti = new HiloFertirrigacion(valvulasFert);
        ferti.start();
        
        try (ServerSocket server = new ServerSocket(21000)) {
            while (true) {
                Socket s = server.accept();
                BufferedReader bf = new BufferedReader(new InputStreamReader(s.getInputStream()));
                String tipoDispositivo = bf.readLine();

                switch (tipoDispositivo) {
                    case "valvulaFert":
                        System.out.println("Soy la valvula del sistema de fertirrigacion");
                        PrintWriter pw = new PrintWriter(s.getOutputStream(), true);
                        valvulasFert.put(1, pw);
                        break;
                }
            }
        } catch (IOException ex) {
            System.getLogger(SistemaFertirrigacion.class.getName())
                  .log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
}*/
