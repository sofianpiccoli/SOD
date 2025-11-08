/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sistdist.sistemarespaldo2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 *
 * @author dgera
 */
public class HiloHumedad extends Thread {

    private boolean on = true;;

    Socket clienteHumedad;
    BufferedReader br;
    double humedad;

    public HiloHumedad(Socket ch) {
        clienteHumedad = ch;
        try {
            br = new BufferedReader(new InputStreamReader(clienteHumedad.getInputStream()));
        } catch (IOException ex) {
            System.getLogger(HiloHumedad.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    public double getHumedad() {
        return humedad;
    }

    public void setHumedad(double humedad) {
        this.humedad = humedad;
    }

    public void apagar() {
        on = false;
    }


    @Override
    public void run() {
        try {
            while (on) {
                try {
                    String entrada = br.readLine();
                    setHumedad(Double.parseDouble(entrada));
                    System.out.println("Humedad = " + getHumedad());
                } catch (IOException ex) {
                    System.getLogger(HiloHumedad.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                }
            }
        }finally {
            try {
                if (clienteHumedad != null && !clienteHumedad.isClosed()) {
                    clienteHumedad.close();
                    System.out.println("🔌 HiloHumedad: Socket cerrado y recursos liberados.");
                }
            } catch (IOException e) {
                System.err.println("Error al cerrar el socket del sensor de humedad.");
            }
        }
    }
}
