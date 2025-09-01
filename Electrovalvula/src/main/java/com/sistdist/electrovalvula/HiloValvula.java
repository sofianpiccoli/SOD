/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sistdist.electrovalvula;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sofianietopiccoli
 */
public class HiloValvula extends Thread{
    private int parcela;
    private Socket socket;
    private BufferedReader br;

    public HiloValvula(Socket socket, int parcela) throws IOException {
        this.parcela = parcela;
        this.socket = socket;
        this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    @Override
    public void run() {
        try {
            while (true) {
                String mensaje = br.readLine();  // espero una orden del sistema central
                if (mensaje != null && mensaje.startsWith("TIEMPO=")) {
                    int tiempo = Integer.parseInt(mensaje.split("=")[1]);
                    activarValvula(tiempo);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(HiloValvula.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void activarValvula(int minutos) {
        System.out.println("Electroválvula parcela " + parcela + 
                           " regando durante " + minutos + " minutos...");
        try {
            Thread.sleep(minutos * 1000); // por simplicidad 1s = 1 minuto
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Electroválvula parcela " + parcela + " apagada.");
    }
}
