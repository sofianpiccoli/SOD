/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sistdist.sistemacentral;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 *
 * @author Estudiantes
 */
public class HiloTemperatura extends Thread{
    private boolean on;
    
    Socket clienteTemperatura;
    BufferedReader br;
    double temperatura;
    
    public HiloTemperatura(Socket ct){
        clienteTemperatura = ct;
        try {
            br = new BufferedReader(new InputStreamReader(clienteTemperatura.getInputStream()));
        } catch (IOException ex) {
            System.getLogger(HiloHumedad.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    public double getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(double temperatura) {
        this.temperatura = temperatura;
    }
    
    public void apagar(){
        on=false;
    }
    
    public int generarValorAleatorio(){
        return (int) (Math.round(Math.random() * 100));
    }
    
    public void obtenerDatos(){
        
    }
    
    @Override
    public void run(){
        while (true){
            try {
                String entrada = br.readLine();
                setTemperatura(Double.parseDouble(entrada));
                System.out.println(getTemperatura());
            } catch (IOException ex) {
                System.getLogger(HiloHumedad.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
          
            /*try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                System.getLogger(HiloHumedad.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }*/
            
        }
    }

}
