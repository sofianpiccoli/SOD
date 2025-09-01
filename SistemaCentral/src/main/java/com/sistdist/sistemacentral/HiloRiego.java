/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sistdist.sistemacentral;

import java.io.PrintWriter;
import java.util.Map;

/**
 *
 * @author sofianietopiccoli
 */
public class HiloRiego extends Thread{
    private double w1, w2, w3;
    private boolean on;
    private HiloLluvia lluvia;
    private HiloRadiacion radiacion;
    private HiloTemperatura temperatura;
    private Map<Integer, HiloHumedad> humedades;
    private Map<Integer, PrintWriter> valvulas;

    public HiloRiego(double w1, double w2, double w3, 
                    HiloLluvia lluvia, HiloRadiacion radiacion, HiloTemperatura temperatura, 
                    Map<Integer, HiloHumedad> humedades, Map<Integer, PrintWriter> valvulas) {
        this.w1 = w1;
        this.w2 = w2;
        this.w3 = w3;
        this.on = true;
        this.lluvia = lluvia;
        this.radiacion = radiacion;
        this.temperatura = temperatura;
        this.humedades = humedades;
        this.valvulas = valvulas;
    }

    
    /*public HiloRiego(double w1, double w2, double w3, 
                     HiloDatosCompartidos datos,
                     Map<Integer, HiloHumedad> humedades) {
        this.w1 = w1;
        this.w2 = w2;
        this.w3 = w3;
        this.datos = datos;
        this.humedades = humedades;
        this.on = true;
    }*/

    public HiloLluvia getLluvia() {
        return lluvia;
    }

    public void setLluvia(HiloLluvia lluvia) {
        this.lluvia = lluvia;
    }

    public HiloRadiacion getRadiacion() {
        return radiacion;
    }

    public void setRadiacion(HiloRadiacion radiacion) {
        this.radiacion = radiacion;
    }

    public HiloTemperatura getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(HiloTemperatura temperatura) {
        this.temperatura = temperatura;
    }
    
    

    public void setValvulas(Map<Integer, PrintWriter> valvulas) {
        this.valvulas = valvulas;
    }
    
    
    public double calculoINR(double H, double T, double R){
        double inr = w1*(1-H/100) + w2*(T/40) + w3*(R/1000);
        return inr;
    }
    
    public boolean decidirRiego(boolean L, double INR){
        return (INR > 0.7 && L == false);
    }
    
    public int tiempoRiego(double inr){
        int minutos;
        if (inr >= 0.9) {
            minutos = 10;
        } else if (inr >= 0.8) {
                minutos = 7;
        } else {
                minutos = 5;
        }
        return minutos;
    }
    
    /*public void abrirValvula(int valvula, int tiempo){
        System.out.println("Parcela "+valvula+" regándose por "+tiempo+" minutos");
    }*/
    
    public void apagar(){
        on = false;
    }
    
    
        @Override
    public void run() {
        while (on) {
            if (temperatura == null || radiacion == null || lluvia == null) {
            System.out.println("Esperando sensores...");
            try {
                Thread.sleep(2000); // espero un poco y sigo chequeando
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            continue; // vuelve al inicio del while
            }
            
            
            for (int parcela : humedades.keySet()) {
                double H = humedades.get(parcela).getHumedad();
                double T = temperatura.getTemperatura();
                double R = radiacion.getRadiacion();
                boolean L = lluvia.getLluvia();
                double inr = calculoINR(H, T, R);
                System.out.println("INR parcela " + parcela + " = " + inr);

                if (decidirRiego(L, inr) && valvulas.containsKey(parcela)) {
                    int minutos = tiempoRiego(inr);
                    abrirValvula(parcela, minutos);
                }
            }

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void abrirValvula(int parcela, int tiempo) {
        if (valvulas == null) return;

        // Espera hasta que la válvula se conecte
        while (!valvulas.containsKey(parcela)) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        PrintWriter pw = valvulas.get(parcela);
        pw.println("TIEMPO=" + tiempo);
        pw.flush();
        System.out.println("Orden enviada -> Parcela " + parcela + " regando " + tiempo + " minutos");
    }
}
    //public void run(){
        /*on = true;
        while (on){
            for (int i = 1; i <=5; i++){
                try {
                    double H = switch (i){
                        case 1 -> hum.getH1();
                        case 2 -> hum.getH2();
                        case 3 -> hum.getH3();
                        case 4 -> hum.getH4();
                        case 5 -> hum.getH5();
                        default -> 0;
                    };
                    double inr = calculoINR(H, datos.getTemperatura(), datos.getRadiacion());
                    System.out.println("INR"+i+" = "+inr);
                    if (decidirRiego(datos.isLluvia(), inr)){
                        abrirValvula(i, tiempoRiego(inr));
                    }
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    System.getLogger(HiloRiego.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                }
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                System.getLogger(HiloRiego.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        }*/
   // }

