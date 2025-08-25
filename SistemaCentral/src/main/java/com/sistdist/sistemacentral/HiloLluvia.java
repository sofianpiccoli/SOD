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
public class HiloLluvia extends Thread{
    private boolean on;
    
    Socket clienteLluvia;
    BufferedReader br;
    double lluvia;

    public HiloLluvia(Socket cl){
        clienteLluvia = cl;
        try {
            br = new BufferedReader(new InputStreamReader(clienteLluvia.getInputStream()));
        } catch (IOException ex) {
            System.getLogger(HiloHumedad.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    public double getLluvia() {
        return lluvia;
    }

    public void setLluvia(double lluvia) {
        this.lluvia = lluvia;
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
                setLluvia(Double.parseDouble(entrada));
            } catch (IOException ex) {
                System.getLogger(HiloHumedad.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
            /*por ahora genera aleatoriamente los valores 
            //hasta que pueda obtener datos del sensor
            h1= generarValorAleatorio();
            h2= generarValorAleatorio();
            h3= generarValorAleatorio();
            h4= generarValorAleatorio();
            h5= generarValorAleatorio(); */
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                System.getLogger(HiloHumedad.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
            
        }
    }
}
