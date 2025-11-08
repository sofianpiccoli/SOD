package com.sistdist.sistemacentral;

import java.io.PrintWriter;
/**
 *
 * @author lucianafigueroa
 */
public class HiloFertirrigacion extends Thread {

    private static final int ID_FERTIRRIGACION = SistemaCentral.ESTADO_FERTIRRIGACION; // ID 2
    private final PrintWriter valvulaPW;

    public HiloFertirrigacion(PrintWriter pw) {
        this.valvulaPW = pw;
    }

    @Override
    public void run() {
        while (true) {
            try {
                System.out.println("🧪 Fertirrigación: Analizando mezclas. Esperando 10 segundos...");
                Thread.sleep(10000);
                System.out.println("⏳ Fertirrigación SOLICITA el uso exclusivo de la bomba.");
                SistemaCentral.solicitarBomba(SistemaCentral.ESTADO_FERTIRRIGACION, 0);

                try {
                    abrirValvula(valvulaPW);
                    prepararSoluciones();

                } finally {
                    cerrarValvula(valvulaPW);
                    System.out.println("✅ Fertirrigación finaliza mezcla y LIBERA la bomba.");

                    SistemaCentral.liberarBomba(ID_FERTIRRIGACION); // Libera el recurso compartido
                }

                Thread.sleep(5000);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
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