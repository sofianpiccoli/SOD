/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sistdist.sistemacentral;

/**
 *
 * @author sofianietopiccoli
 */
public class HiloRiego extends Thread{
        private double w1;
        private double w2;
        private double w3;
        private boolean on;
        
        private HiloDatosCompartidos datos;
        private HiloHumedad hum;
       
    public HiloRiego(double w1, double w2, double w3, HiloDatosCompartidos datos, HiloHumedad hum){
        this.w1 = w1;
        this.w2 = w2;
        this.w3 = w3;
        this.datos = datos;
        this.hum = hum;
        on = false;
        
    }
    public double calculoINR(double H, double T, double R){
        double inr = w1*(1-H/100) + w2*(T/40) + w3*(R/1000);
        return inr;
    }
    
    public boolean decidirRiego(boolean L, double INR){
        return (INR > 0.7 && L == false);
    }
    
    public void abrirValvula(int valvula){
        System.out.println("Parcela "+valvula+" regándose");
    }
    
    public void apagar(){
        on = false;
    }
    
    
        @Override
    public void run(){
        on = true;
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
                        abrirValvula(i);
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
        }
    }
}
