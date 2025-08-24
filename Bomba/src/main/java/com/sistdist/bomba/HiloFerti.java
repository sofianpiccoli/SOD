/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package com.sistdist.bomba;

/**
 *
 * @author miria
 */
public class HiloFerti extends Thread{

    
    MonitorBomba b=new MonitorBomba();
    public HiloFerti(MonitorBomba bo){
        b = bo;
    }
    
    @Override
    public void run(){
        while (true) {
            b.pedirBomba("Fertirrigación");
            try {
                System.out.println("Usando bomba para fertirrigación...");
                Thread.sleep(2000); // simulamos uso de la bomba
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                b.liberarBomba("Fertirrigación");
            }
        }
    }
        
    
}
