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
    boolean lluvia;

    public HiloLluvia(Socket cl){
        clienteLluvia = cl;
        try {
            // Conexión al sensor de lluvia
            br = new BufferedReader(new InputStreamReader(clienteLluvia.getInputStream()));
        } catch (IOException ex) {
            System.getLogger(HiloHumedad.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    public boolean getLluvia() {
        return lluvia;
    }

    public void setLluvia(boolean lluvia) {
        this.lluvia = lluvia;
    }

    public void apagar(){
        on=false;
    }
    
    @Override
    public void run(){
        while (true){
            try {
                // Recibe "true/false" si llueve
                String entrada = br.readLine();
                setLluvia(Boolean.parseBoolean(entrada));
                System.out.println("Lluvia = "+getLluvia());
            } catch (IOException ex) {
                System.getLogger(HiloHumedad.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        }
    }
}
