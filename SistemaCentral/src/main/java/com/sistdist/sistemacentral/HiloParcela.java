/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package com.sistdist.sistemacentral;

import java.io.PrintWriter;

import java.io.PrintWriter;

public class HiloParcela extends Thread {
    private final int idParcela;
    private final HiloLluvia lluvia;
    private final HiloRadiacion radiacion;
    private final HiloTemperatura temperatura;
    private final HiloHumedad humedad;
    private final PrintWriter valvula;
    private boolean regando = false;
    private boolean activo = true;

    public HiloParcela(int idParcela,
                       HiloLluvia lluvia,
                       HiloRadiacion radiacion,
                       HiloTemperatura temperatura,
                       HiloHumedad humedad,
                       PrintWriter valvula) {
        this.idParcela = idParcela;
        this.lluvia = lluvia;
        this.radiacion = radiacion;
        this.temperatura = temperatura;
        this.humedad = humedad;
        this.valvula = valvula;
    }

    public boolean estaRegando() {
        return regando;
    }

    public void detenerPorLluvia() {
        if (regando) {
            valvula.println("CERRAR");
            valvula.flush();
            regando = false;
            System.out.println("🌧️ Parcela " + idParcela + " detuvo el riego por lluvia.");
        }
    }

    public void detener() {
        activo = false;
    }

    @Override
    public void run() {
        while (activo) {
            try {
                if (lluvia == null || radiacion == null || temperatura == null || humedad == null) {
                    Thread.sleep(2000);
                    continue;
                }

                boolean llueve = lluvia.getLluvia();
                if (llueve) {
                    detenerPorLluvia();
                    Thread.sleep(5000);
                    continue;
                }

                double H = humedad.getHumedad();
                double T = temperatura.getTemperatura();
                double R = radiacion.getRadiacion();

                double inr = SistemaCentral.calculoINR(H, T, R);
                System.out.println("INR="+inr+" parcela "+idParcela);

                if (SistemaCentral.decidirRiego(llueve, inr)) {
                    int minutos = SistemaCentral.tiempoRiego(inr);
                    valvula.println("TIEMPO=" + minutos);
                    valvula.flush();
                    regando = true;
                    System.out.println("💧 Parcela " + idParcela + " inicia riego por " + minutos + " minutos.");
                } else {
                    if (regando) {
                        valvula.println("CERRAR");
                        valvula.flush();
                        regando = false;
                        System.out.println("🚫 Parcela " + idParcela + " detuvo riego (INR bajo o sin necesidad).");
                    }
                }

                Thread.sleep(5000);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
