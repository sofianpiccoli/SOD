/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.electrovalvulabomba;

/**
 *
 * @author sofianietopiccoli
 */
public class HiloValvula extends Thread{
    private boolean on;
    
    public HiloValvula(){
        on = false;
    }
    
    public void encender(){
        on = true;
        System.out.println("Electroválvula de bomba ACTIVA");
    }
    
    public void apagar(){
        on = false;
        System.out.println("Electroválvula de bomba INACTIVA");
    }
    
    public void run(){
        on = true;
        while (on){
            System.out.println("Electroválvula de bomba ACTIVA");
            try {
            Thread.sleep(5000);
            } catch (InterruptedException ex) {
                System.getLogger(HiloValvula.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        }
    }
}
