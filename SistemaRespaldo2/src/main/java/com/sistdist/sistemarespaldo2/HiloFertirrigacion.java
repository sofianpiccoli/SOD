package com.sistdist.sistemarespaldo2;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
/**
 *
 * @author dgera
 */
public class HiloFertirrigacion extends Thread {
    private static Boolean on = true;

    Socket clienteFertirrigacion;
    private static final int ID_FERTIRRIGACION = SistemaRespaldo2.ESTADO_FERTIRRIGACION;
    private final PrintWriter valvulaPW;

    public HiloFertirrigacion(PrintWriter pw, Socket cf) {
        this.valvulaPW = pw;
        this.clienteFertirrigacion = cf;
    }

    public void apagar() {
        on = false;
    }

    @Override
    public void run() {
        try {
            while (on) {
                try {
                    System.out.println("🧪 Fertirrigación: Analizando mezclas. Esperando 10 segundos...");
                    Thread.sleep(10000);
                    System.out.println("⏳ Fertirrigación SOLICITA el uso exclusivo de la bomba.");
                    SistemaRespaldo2.solicitarBomba(SistemaRespaldo2.ESTADO_FERTIRRIGACION, 0);

                    try {
                        abrirValvula(valvulaPW);
                        prepararSoluciones();

                    } finally {
                        cerrarValvula(valvulaPW);
                        System.out.println("✅ Fertirrigación finaliza mezcla y LIBERA la bomba.");

                        SistemaRespaldo2.liberarBomba(ID_FERTIRRIGACION);
                    }

                    Thread.sleep(5000);

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        } finally {
            try {
                if (clienteFertirrigacion != null && !clienteFertirrigacion.isClosed()) {
                    clienteFertirrigacion.close();
                    System.out.println("🔌 HiloFertirrigacion: Socket cerrado y recursos liberados.");
                }
            } catch (IOException e) {
                System.err.println("Error al cerrar el socket del sensor de fertirrigacion.");
            }
        }
    }

    private void abrirValvula(PrintWriter pw) {
        if (pw != null) {
            pw.println("ABRIR");
            System.out.println("\n[Fertirrigacion] Orden enviada -> Abriendo valvula de fertirrigacion");
        }
    }

    private void prepararSoluciones() throws InterruptedException {
        System.out.println("[Fertirrigacion] Preparando soluciones con fertilizantes/agroquimicos. Usando bomba (9s)...");
        Thread.sleep(9000);
    }

    private void cerrarValvula(PrintWriter pw) {
        if (pw != null) {
            pw.println("CERRAR");
            System.out.println("Orden enviada -> Cerrando valvula de fertirrigacion");
        }
    }
}