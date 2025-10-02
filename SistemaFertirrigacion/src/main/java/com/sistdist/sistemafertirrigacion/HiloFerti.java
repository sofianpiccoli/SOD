/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sistdist.sistemafertirrigacion;

/**
 *
 * @author sofianietopiccoli
 */
import interfacesRMI.IExclusion;
import java.rmi.Naming;

public class HiloFerti extends Thread {

    private IExclusion servidor;

    public HiloFerti(IExclusion servidor) {
        this.servidor = servidor;
    }

    @Override
    public void run() {
        while (true) {
            try {
                servidor.pedirBomba("Fertirrigación");
                System.out.println("Usando bomba para fertirrigación...");
                Thread.sleep(2000);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    servidor.liberarBomba("Fertirrigación");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}