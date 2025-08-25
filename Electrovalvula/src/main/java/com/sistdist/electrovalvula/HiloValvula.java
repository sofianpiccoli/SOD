/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sistdist.electrovalvula;

/**
 *
 * @author sofianietopiccoli
 */
public class HiloValvula extends Thread{
    private boolean on;
    private int parcela;
    
    public HiloValvula(int parcela){
        on = false;
        this.parcela = parcela;
    }
    
    public void encender(){
        on = true;
        System.out.println("Electroválvula de parcela "+parcela+" ACTIVA");
    }
    
    public void apagar(){
        on = false;
        while (!on){
            System.out.println("Electroválvula de parcela "+parcela+" INACTIVA");
        }
    }
    
    public void run(){
        on = true;
        while (on){
            System.out.println("Electroválvula de parcela "+parcela+" ACTIVA");
            try {
            Thread.sleep(5000);
            } catch (InterruptedException ex) {
                System.getLogger(HiloValvula.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        }
    }
}
