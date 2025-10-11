/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package com.mycompany.servidorexclusionmutua;
import interfaces.IDetectorFalla;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class DetectorFallo extends UnicastRemoteObject implements IDetectorFalla {

    private boolean activo = true;

    public DetectorFallo() throws RemoteException {
        super();
    }

    @Override
    public String dameMensaje() throws RemoteException {
        return activo ? "Servidor maestro activo" : "Servidor maestro caído";
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
