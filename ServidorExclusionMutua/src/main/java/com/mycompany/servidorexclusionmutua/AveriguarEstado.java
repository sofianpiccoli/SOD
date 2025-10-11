/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package com.mycompany.servidorexclusionmutua;

import java.util.TimerTask;

public class AveriguarEstado extends TimerTask {

    private String hostMaestro;

    public AveriguarEstado(String hostMaestro) {
        this.hostMaestro = hostMaestro;
    }

    @Override
    public void run() {
        new Thread(new ControlRespuesta(hostMaestro)).start();
    }
}
