/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemafertirrigacion;

import java.io.PrintWriter;
import java.util.Map;

/**
 *
 * @author lucianafigue
 */
public class HiloFertirrigacion extends Thread{
    private boolean on = true;
    private Map<Integer, PrintWriter> valvulas;
    
    public HiloFertirrigacion(Map<Integer, PrintWriter> valvulas) {
        this.valvulas = valvulas;
    }
    
    public void apagar() { on = false; }

    @Override
    public void run() {
        while (on) {
            // Simula un ciclo de fertirrigación cada cierto tiempo
            if (valvulas.containsKey(1)) {
                PrintWriter pw = valvulas.get(1);
                abrirValvula(pw);
                prepararSoluciones();
                cerrarValvula(pw);
            }

            try {
                Thread.sleep(10000); // espera 10 segundos antes del próximo ciclo
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    private void abrirValvula(PrintWriter pw) {
        pw.println("ABRIR");
        System.out.println("\nOrden enviada -> Abriendo valvula de fertirrigacion");
    }

    private void prepararSoluciones() {
        System.out.println("Preparando soluciones con fertilizantes/agroquimicos...");
        try {
            Thread.sleep(3000); // simula el tiempo de preparación
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void cerrarValvula(PrintWriter pw) {
        pw.println("CERRAR");
        System.out.println("Orden enviada -> Cerrando valvula de fertirrigacion");
    }
}
