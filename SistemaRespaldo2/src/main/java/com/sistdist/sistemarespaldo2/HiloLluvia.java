
package com.sistdist.sistemarespaldo2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 *
 * @author dgera
 */
public class HiloLluvia extends Thread {
    private boolean on = true;
    ;

    Socket clienteLluvia;
    BufferedReader br;
    boolean lluvia;

    public HiloLluvia(Socket cl) {
        clienteLluvia = cl;
        try {
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

    public void apagar() {
        on = false;
    }

    @Override
    public void run() {
        try {
            while (on) {
                try {
                    String entrada = br.readLine();
                    boolean nuevaLluvia = Boolean.parseBoolean(entrada);
                    setLluvia(nuevaLluvia);
                    System.out.println("Lluvia = " + getLluvia());
                    if (nuevaLluvia) {
                        SistemaRespaldo2.notificarLluvia();
                    }
                } catch (IOException ex) {
                    System.getLogger(HiloLluvia.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                    break;
                }
            }
        } finally {
            try {
                if (clienteLluvia != null && !clienteLluvia.isClosed()) {
                    clienteLluvia.close();
                    System.out.println("🔌 HiloLluvia: Socket cerrado y recursos liberados.");
                }
            } catch (IOException e) {
                System.err.println("Error al cerrar el socket del sensor de lluvia.");
            }
        }
    }
}
