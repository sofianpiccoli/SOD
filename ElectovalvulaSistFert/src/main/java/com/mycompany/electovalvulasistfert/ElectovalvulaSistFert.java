/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.electovalvulasistfert;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author dgera
 */
public class ElectovalvulaSistFert {

    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 21000);
            PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Me identifico ante el sistema central de fertirrigación
            pw.println("valvulaFert");

            HiloValvula valvula = new HiloValvula(br);
            valvula.start();

        } catch (IOException ex) {
            System.out.println("Error conectando la valvula: " + ex.getMessage());
        }
    }
}
