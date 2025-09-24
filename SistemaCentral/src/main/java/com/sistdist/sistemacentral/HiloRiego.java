/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sistdist.sistemacentral;

import java.io.PrintWriter;
import java.util.Map;

/**
 *
 * @author lucianafigueroa
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
    
    public void apagar(){
        on = false;
    }
    
    @Override
public void run() {
    boolean lloviendo = false; // flag para no repetir la acción
    
    while (on) {
        // Espera hasta que todos los sensores estén conectados
        if (temperatura == null || radiacion == null || lluvia == null) {
            System.out.println("Esperando sensores...");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            continue;
        }

        boolean L = lluvia.getLluvia();

        // Si llueve → cerrar todas las válvulas
        if (L) {
            if (!lloviendo) { // solo ejecutar la primera vez que detecta lluvia
                System.out.println("¡Está lloviendo! Cerrando todas las válvulas...");
                for (int parcela : valvulas.keySet()) {
                    abrirValvula(parcela, 0); // tiempo=0 para detener
                }
                lloviendo = true;
            }
        } else {
            lloviendo = false; // se resetea cuando deja de llover
        }

        // Por cada parcela con sensor de humedad
        for (int parcela : humedades.keySet()) {
            double H = humedades.get(parcela).getHumedad();
            double T = temperatura.getTemperatura();
            double R = radiacion.getRadiacion();
            
            // Cálculo del INR
            double inr = SistemaCentral.calculoINR(H, T, R);
            System.out.println("Parcela " + parcela + " -> INR = " + inr);

            // Si es necesario regar → abrir válvula
            if (SistemaCentral.decidirRiego(L, inr) && valvulas.containsKey(parcela)) {
                int minutos = SistemaCentral.tiempoRiego(inr);
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

    
   

    public void abrirValvula(int parcela, int tiempo) {
        if (valvulas == null) return;

        new Thread(() -> {
            if (tiempo <= 0) {
            cerrarValvula(parcela);
            return;
            }
            System.out.println("Preparando orden -> Parcela " + parcela + " tiempo=" + tiempo);
            // Espera no bloqueante del hilo principal: el hilo creado esperará a que se conecte la válvula
            while (!valvulas.containsKey(parcela)) {
                System.out.println("Esperando conexión de válvula " + parcela + "...");
                try { Thread.sleep(500); } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
            // Envía orden a la electroválvula correspondiente
            PrintWriter pw = valvulas.get(parcela);
            if (pw == null) {
                System.out.println("La válvula " + parcela + " desapareció antes de enviar la orden.");
                return;
            }
            pw.println("TIEMPO=" + tiempo);
            pw.flush();
            System.out.println("Orden enviada -> Parcela " + parcela + " regando " + tiempo + " minutos");
        }).start();
    }
    
    public void cerrarValvula(int parcela) {
        if (valvulas == null || !valvulas.containsKey(parcela)) return;

        // Orden de cierre inmediato
        PrintWriter pw = valvulas.get(parcela);
        pw.println("CERRAR");
        pw.flush();
        System.out.println("Orden enviada -> Parcela " + parcela + " detener riego por lluvia");
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

