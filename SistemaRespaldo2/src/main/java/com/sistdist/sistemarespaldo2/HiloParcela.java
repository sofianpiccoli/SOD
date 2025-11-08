package com.sistdist.sistemarespaldo2;

import java.io.PrintWriter;

/**
 *
 * @author dgera
 */
public class HiloParcela extends Thread {
    private final int idParcela;
    private final HiloLluvia lluvia;
    private final HiloRadiacion radiacion;
    private final HiloTemperatura temperatura;
    private final HiloHumedad humedad;
    private final PrintWriter valvula;

    private boolean regando = false;
    private boolean activo = true;
    private boolean liberacionManual = false;

    public HiloParcela(int idParcela,
                       HiloLluvia lluvia,
                       HiloRadiacion radiacion,
                       HiloTemperatura temperatura,
                       HiloHumedad humedad,
                       PrintWriter valvula) {
        this.idParcela = idParcela;
        this.lluvia = lluvia;
        this.radiacion = radiacion;
        this.temperatura = temperatura;
        this.humedad = humedad;
        this.valvula = valvula;
    }

    public boolean estaRegando() {
        return regando;
    }


    public void detenerPorLluvia() {
        if (regando) {
            valvula.println("CERRAR");
            valvula.flush();
            regando = false;
            liberacionManual = true;
            System.out.println("🌧️ Parcela " + idParcela + " detuvo el riego por lluvia.");
            this.interrupt();
            try {
                SistemaRespaldo2.liberarBomba(1);
            } catch (Exception e) {
                System.err.println("Error al liberar la bomba por lluvia: " + e.getMessage());
            }
        }
    }

    public void detener() {
        activo = false;
    }

    @Override
    public void run() {
        while (activo) {
            liberacionManual = false;
            try {
                if (lluvia == null || radiacion == null || temperatura == null || humedad == null) {
                    Thread.sleep(2000);
                    continue;
                }

                boolean llueve = lluvia.getLluvia();

                if (llueve) {
                    Thread.sleep(5000);
                    continue;
                }

                double H = humedad.getHumedad();
                double T = temperatura.getTemperatura();
                double R = radiacion.getRadiacion();

                double inr = SistemaRespaldo2.calculoINR(H, T, R);
                System.out.println("INR=" + inr + " parcela " + idParcela);

                if (SistemaRespaldo2.decidirRiego(llueve, inr)) {
                    try {
                        System.out.println("⏳ Parcela " + idParcela + " SOLICITA la bomba para riego...");
                        SistemaRespaldo2.solicitarBomba(SistemaRespaldo2.ESTADO_RIEGO, idParcela);

                        int minutos = SistemaRespaldo2.tiempoRiego(inr);
                        long tiempoEnMs = (long) minutos * 1000 * 5;

                        valvula.println("TIEMPO=" + minutos);
                        valvula.flush();
                        regando = true;
                        System.out.println("💧 Parcela " + idParcela + " ADQUIERE la bomba e inicia riego por " + minutos + " minutos. INR: " + inr);

                        Thread.sleep(tiempoEnMs);

                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();

                    } finally {
                        if (regando && !liberacionManual) {
                            valvula.println("CERRAR");
                            valvula.flush();
                            regando = false;
                            System.out.println("✅ Parcela " + idParcela + " finaliza riego y LIBERA la bomba.");
                            SistemaRespaldo2.liberarBomba(1);
                        } else if (liberacionManual) {
                            System.out.println("☔ Parcela " + idParcela + ": Liberación ya manejada por interrupción de lluvia.");
                            Thread.interrupted();
                        }
                    }
                } else {
                    if (regando) {
                        System.out.println("🚫 Parcela " + idParcela + " NO requiere riego. Esperando...");
                    }
                }

                Thread.sleep(5000);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}