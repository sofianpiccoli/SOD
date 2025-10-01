/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package com.mycompany.servidorexclusionmutua;

import java.rmi.Naming;

import interfaces.IDetectorFalla;

public class ControlRespuesta implements Runnable {

    private String hostMaestro;

    public ControlRespuesta(String hostMaestro) {
        this.hostMaestro = hostMaestro;
    }

    @Override
    public void run() {
        try {
            IDetectorFalla detector = (IDetectorFalla) Naming.lookup("//" + hostMaestro + "/ServidorMaestro");
            String mensaje = detector.dameMensaje();
            System.out.println("[ControlRespuesta] " + mensaje);
        } catch (Exception e) {
            System.out.println("[ControlRespuesta] No se pudo contactar al servidor maestro.");
        }
    }
}
