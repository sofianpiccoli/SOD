package com.sistdist.sistemarespaldo2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 *
 * @author dgera
 */

public class HiloRadiacion extends Thread {
    private boolean on = true;
    ;

    Socket clienteRadiacion;
    BufferedReader br;
    double radiacion;

    public HiloRadiacion(Socket cr) {
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

    public void apagar() {
        on = false;
    }

    @Override
    public void run() {
        try {
            while (on) {
                try {
                    String entrada = br.readLine();
                    setRadiacion(Double.parseDouble(entrada));
                    System.out.println("Radiacion = " + getRadiacion());
                } catch (IOException ex) {
                    System.getLogger(HiloHumedad.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                }
            }
        } finally {
            try {
                if (clienteRadiacion != null && !clienteRadiacion.isClosed()) {
                    clienteRadiacion.close();
                    System.out.println("🔌 HiloRadiacion: Socket cerrado y recursos liberados.");
                }
            } catch (IOException e) {
                System.err.println("Error al cerrar el socket del sensor de radiacion.");
            }
        }
    }
}
