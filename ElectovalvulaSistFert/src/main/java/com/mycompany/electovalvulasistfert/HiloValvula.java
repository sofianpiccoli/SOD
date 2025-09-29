/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.electovalvulasistfert;

import java.io.BufferedReader;
import java.io.IOException;

/**
 *
 * @author sofianietopiccoli
 */
public class HiloValvula extends Thread {

    private boolean on = true;
    private BufferedReader br;

    public HiloValvula(BufferedReader br) {
        this.br = br;
    }

    public void apagar() {
        on = false;
        System.out.println("Electrovalvula Fertirrigacion INACTIVA");
    }

    @Override
    public void run() {
        System.out.println("Electrovalvula Fertirrigacion esperando ordenes...");
        while (on) {
            try {
                String orden = br.readLine();
                if (orden == null) {
                    break;
                }

                switch (orden) {
                    case "ABRIR":
                        System.out.println("\nElectrovalvula Fertirrigacion ACTIVA (abierta)");
                        break;
                    case "CERRAR":
                        System.out.println("Electrovalvula Fertirrigacion INACTIVA (cerrada)");
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }
}
