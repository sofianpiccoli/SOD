/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.sistemafertirrigacion;

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
 * @author lucianafigue
 */
public class SistemaFertirrigacion {

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
}
