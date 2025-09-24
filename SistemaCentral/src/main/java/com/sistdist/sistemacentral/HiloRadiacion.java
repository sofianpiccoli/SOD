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
 * @author lucianafigueroa
 */
public class HiloRadiacion extends Thread{
    private boolean on;
    
    Socket clienteRadiacion;
    BufferedReader br;
    double radiacion;

    public HiloRadiacion(Socket cr){
        clienteRadiacion = cr;
        try {
            // Conexión al sensor de radiación 
            br = new BufferedReader(new InputStreamReader(clienteRadiacion.getInputStream()));
        } catch (IOException ex) {
            System.getLogger(HiloHumedad.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    public double getRadiacion() {
        return radiacion;
    }

    public void setRadiacion(double radiacion) {
        this.radiacion = radiacion;
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
                // Lee e imprime la radiación recibida
                String entrada = br.readLine();
                setRadiacion(Double.parseDouble(entrada));
                System.out.println("Radiacion = "+getRadiacion());
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
            /*try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                System.getLogger(HiloHumedad.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }*/
            
        }
    }
}
