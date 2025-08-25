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
 * @author sofianietopiccoli
 */
public class HiloHumedad extends Thread{
    //private double h1;
    //private double h2;
    //private double h3;
    //private double h4;
    //private double h5;
    private boolean on;
    
    Socket clienteHumedad;
    BufferedReader br;
    double humedad;
    
    /*public HiloHumedad(){
        on=false;
    }*/
    
    public HiloHumedad(Socket ch){
        clienteHumedad = ch;
        try {
            br = new BufferedReader(new InputStreamReader(clienteHumedad.getInputStream()));
        } catch (IOException ex) {
            System.getLogger(HiloHumedad.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
    
    public double getHumedad() {
        return humedad;
    }

    public void setHumedad(double humedad) {
        this.humedad = humedad;
    }

    /*public double getH1() {
        return h1;
    }

    public double getH2() {
        return h2;
    }

    public double getH3() {
        return h3;
    }

    public double getH4() {
        return h4;
    }

    public double getH5() {
        return h5;
    }
    */
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
                setHumedad(Double.parseDouble(entrada));
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
