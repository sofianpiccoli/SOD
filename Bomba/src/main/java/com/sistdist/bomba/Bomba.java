/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package com.sistdist.bomba;

/**
 *
 * @author miria
 */
public class Bomba {

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        MonitorBomba bom = new MonitorBomba();
        HiloFerti ferti=new HiloFerti(bom);
        HiloRi ri=new HiloRi(bom);
        
        
        ferti.start();
        ri.start();
        
    }
}
