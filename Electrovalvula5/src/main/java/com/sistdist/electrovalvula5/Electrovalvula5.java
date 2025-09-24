/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.sistdist.electrovalvula5;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sofianietopiccoli
 */
public class Electrovalvula5 {

    public static void main(String[] args) {
        try {
            // Se conecta al sistema central e informa su ID (ej: electroValvula1)
            InetAddress IPServidor = InetAddress.getByName("127.0.0.1");
            Socket cliente = new Socket(IPServidor, 20000);

            // Me identifico con el sistema central
            PrintWriter pw = new PrintWriter(cliente.getOutputStream(), true);
            pw.println("electroValvula5"); 
            pw.flush();

            // Inicia el hilo que escucha órdenes del sistema central
            HiloValvula valvula = new HiloValvula(cliente, 2);
            valvula.start();

        } catch (IOException ex) {
            Logger.getLogger(Electrovalvula5.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
