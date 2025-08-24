/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemaferirrigacion;

/**
 *
 * @author sofianietopiccoli
 */
public class HiloElectroValvulaSistFert extends Thread{
        private boolean on;
       
    public HiloElectroValvulaSistFert(){
        on = false;
        
    }

    public void abrirValvula(){
        System.out.println("Dirigiendo agua de la bomba hacia tanques...");
    }
    
    public void prepararSoluciones(){
        System.out.println("Preparando soluciones con fertilizantes y/o agroquímicos...");
    }
    
    public void apagar(){
        on = false;
    }
    
    @Override
    public void run(){
        on = true;
        while (on){
                try {
                    abrirValvula();
                    Thread.sleep(1000);
                    prepararSoluciones();
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    System.getLogger(HiloElectroValvulaSistFert.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                System.getLogger(HiloElectroValvulaSistFert.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        }
    }
}
