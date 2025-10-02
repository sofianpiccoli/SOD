/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package com.sistdist.bomba;

/**
 *
 * @author miria
 */
import interfacesRMI.IExclusion;
import java.rmi.Naming;

public class HiloRi extends Thread {

    private IExclusion servidor;

    public HiloRi(IExclusion servidor) {
        this.servidor = servidor;
    }

    @Override
    public void run() {
        while (true) {
            try {
                servidor.pedirBomba("Riego");
                System.out.println("Usando bomba para riego...");
                Thread.sleep(2000);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    servidor.liberarBomba("Riego");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
/*public class HiloRi extends Thread{

    MonitorBomba b=new MonitorBomba();
    public HiloRi(MonitorBomba bo){
        b = bo;
    }
    
    @Override
    public void run(){
        while (true) {
            b.pedirBomba("Riego");
            try {
                System.out.println("Usando bomba para riego...");
                Thread.sleep(2000); // simulamos uso de la bomba
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                b.liberarBomba("Riego");
            }
        }
    }
}
*/