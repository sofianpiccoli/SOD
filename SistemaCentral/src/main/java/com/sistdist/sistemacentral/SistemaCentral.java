package com.sistdist.sistemacentral;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.NotBoundException;
import java.net.MalformedURLException;

import com.sistdist.interfaces.IDetectorFalla;
import com.sistdist.interfaces.IEleccionAnillo;
import com.sistdist.interfaces.IServicioExclusionMutua;
/**
 *
 * @author lucianafigueroa
 */
public class SistemaCentral extends UnicastRemoteObject implements IDetectorFalla, IEleccionAnillo {

    private static final int ID_MAESTRO = 1;
    private static final int PUERTO_RMI_SC = 9000;
    private static Boolean coordinador = true;

    private static final String DIRECCION_SUCESOR = "rmi://localhost:9001/ServidorRespaldo2";
    private static final String DIRECCION_SUCESOR2 = "rmi://localhost:9001/ServidorRespaldo3";
    private static IEleccionAnillo sucesor = null;
    private static int idCoordinadorActual = ID_MAESTRO;
    private static Boolean S_CAIDO = false;
    private static Boolean S2_CAIDO = false;

    private static final double W1 = 0.5;
    private static final double W2 = 0.3;
    private static final double W3 = 0.2;

    private static IServicioExclusionMutua servidorEM = null;

    private static Map<Integer, HiloHumedad> humedades = new ConcurrentHashMap<>();
    private static Map<Integer, PrintWriter> valvulas = new ConcurrentHashMap<>();
    private static Map<Integer, HiloParcela> parcelas = new ConcurrentHashMap<>();

    private static HiloLluvia lluvia = null;
    private static HiloTemperatura temperatura = null;
    private static HiloRadiacion radiacion = null;

    private static final int ESTADO_LIBRE = 0;
    public static final int ESTADO_RIEGO = 1;
    public static final int ESTADO_FERTIRRIGACION = 2;

    public SistemaCentral() throws RemoteException {
        super(PUERTO_RMI_SC);
        int condition = 2;
        while (condition < 4) {
            int puerto = obtenerPuertoRMI(condition);
            try {
                IEleccionAnillo servidor = (IEleccionAnillo) Naming.lookup(obtenerDireccion(puerto));
                servidor.estoyVivo(1);
                condition = condition + 1;
            } catch (Exception e) {
                System.out.println("ERROR: No se encuentra el servidor con puerto: " + puerto);
                condition = condition + 1;
            }
        }
    }

    @Override
    public void pulso() throws RemoteException {
    }

    @Override
    public void estoyVivo(int id) throws RemoteException {
        if (id == 1) {
            S_CAIDO = false;
        } else {
            S2_CAIDO = false;
        }
    }

    @Override
    public Boolean soyCoordinador() {
        return coordinador;
    }

    private int obtenerPuertoRMI(int id) {
        return 9000 + (id - 1);
    }

    private String obtenerDireccion(int puerto) {
        if (puerto == 9000) {
            return "rmi://localhost:9000/SistemaCentralRMI";
        } else if (puerto == 9001) {
            return "rmi://localhost:9001/ServidorRespaldo2";
        } else {
            return "rmi://localhost:9002/ServidorRespaldo3";
        }
    }

    @Override
    public void iniciarEleccion(List<Integer> idsParticipantes) throws RemoteException {
        if (idsParticipantes.contains(ID_MAESTRO)) {
            int nuevoCoordinadorID = Collections.max(idsParticipantes);
            String dirNuevoCoordinador = "rmi://localhost:" + (9000 + (nuevoCoordinadorID - 1)) + "/SistemaCentralRMI";
            System.out.println("\n⚠️ Elección completada. Coordinador (ID: " + nuevoCoordinadorID + ") elegido.");
            if (sucesor != null) {
                sucesor.coordinadorElegido(nuevoCoordinadorID, dirNuevoCoordinador);
            }
            coordinadorElegido(nuevoCoordinadorID, dirNuevoCoordinador);
        } else {
            idsParticipantes.add(ID_MAESTRO);
            System.out.println("🔄 Elección: Sistema Central reenvía mensaje. Participantes: " + idsParticipantes);
            try {
                if (sucesor != null) {
                    sucesor.iniciarEleccion(idsParticipantes);
                }
            } catch (Exception e) {
                System.err.println("❌ ERROR RMI: Sucesore en " + DIRECCION_SUCESOR + " falló. Anillo roto.");
            }
        }
    }

    @Override
    public void coordinadorElegido(int idCoordinador, String direccionCoordinador) throws RemoteException {
        if (idCoordinadorActual == idCoordinador) {
            return;
        }
        idCoordinadorActual = idCoordinador;
        if (idCoordinador != ID_MAESTRO) {
            System.out.println("⬇️ ROL: He sido destituido. Nuevo SC (Coordinador) ID: " + idCoordinador);
        } else {
            System.out.println("👑 ROL: Soy el Coordinador Principal (ID: 1).");
        }
        try {
            if (sucesor != null) {
                sucesor.coordinadorElegido(idCoordinador, direccionCoordinador);
            }
        } catch (Exception e) {
            System.err.println("❌ ERROR RMI: Sucesore en " + DIRECCION_SUCESOR + " falló al recibir anuncio.");
        }
    }

    public static void main(String[] args) {
        try {
            Thread.sleep(5000);
            SistemaCentral instanciaRMI = new SistemaCentral();
            LocateRegistry.createRegistry(PUERTO_RMI_SC);
            Naming.rebind("rmi://localhost:" + PUERTO_RMI_SC + "/SistemaCentralRMI", instanciaRMI);
            System.out.println("✅ Sistema Central RMI (ID: " + ID_MAESTRO + ") para monitoreo/elección listo en puerto 9000");

            conectarServidorEM();
            servidorEM.liberarRecursos();
            ServerSocket server = new ServerSocket(20000);
            System.out.println("Sistema central esperando dispositivos......");

            while (true) {
                Socket s = server.accept();
                BufferedReader bf = new BufferedReader(new InputStreamReader(s.getInputStream()));
                String tipoDispositivo = bf.readLine();

                switch (tipoDispositivo) {
                    case "sensorHumedad1":
                        System.out.println("Sensor de humedad 1 conectado");
                        HiloHumedad hum1 = new HiloHumedad(s);
                        hum1.start();
                        humedades.put(1, hum1);
                        crearHiloParcelaSiPosible(1);
                        break;
                    case "sensorHumedad2":
                        System.out.println("Sensor de humedad 2 conectado");
                        HiloHumedad hum2 = new HiloHumedad(s);
                        hum2.start();
                        humedades.put(2, hum2);
                        crearHiloParcelaSiPosible(2);
                        break;
                    case "sensorHumedad3":
                        System.out.println("Sensor de humedad 3 conectado");
                        HiloHumedad hum3 = new HiloHumedad(s);
                        hum3.start();
                        humedades.put(3, hum3);
                        crearHiloParcelaSiPosible(3);
                        break;
                    case "sensorHumedad4":
                        System.out.println("Sensor de humedad 4 conectado");
                        HiloHumedad hum4 = new HiloHumedad(s);
                        hum4.start();
                        humedades.put(4, hum4);
                        crearHiloParcelaSiPosible(4);
                        break;
                    case "sensorHumedad5":
                        System.out.println("Sensor de humedad 5 conectado");
                        HiloHumedad hum5 = new HiloHumedad(s);
                        hum5.start();
                        humedades.put(5, hum5);
                        crearHiloParcelaSiPosible(5);
                        break;

                    // Sensores globales
                    case "sensorTemperatura":
                        System.out.println("Sensor de temperatura conectado");
                        HiloTemperatura tem = new HiloTemperatura(s);
                        tem.start();
                        temperatura = tem;
                        crearHilosParcelaExistentes();
                        break;
                    case "sensorLluvia":
                        System.out.println("Sensor de lluvia conectado");
                        HiloLluvia lluv = new HiloLluvia(s);
                        lluv.start();
                        lluvia = lluv;
                        crearHilosParcelaExistentes();
                        break;
                    case "sensorRadiacion":
                        System.out.println("Sensor de radiacion conectado");
                        HiloRadiacion rad = new HiloRadiacion(s);
                        rad.start();
                        radiacion = rad;
                        crearHilosParcelaExistentes();
                        break;

                    // Válvulas
                    case "electroValvula1":
                        System.out.println("Electrovalvula 1 conectada");
                        PrintWriter pw1 = new PrintWriter(s.getOutputStream(), true);
                        valvulas.put(1, pw1);
                        crearHiloParcelaSiPosible(1);
                        break;
                    case "electroValvula2":
                        System.out.println("Electrovalvula 2 conectada");
                        PrintWriter pw2 = new PrintWriter(s.getOutputStream(), true);
                        valvulas.put(2, pw2);
                        crearHiloParcelaSiPosible(2);
                        break;
                    case "electroValvula3":
                        System.out.println("Electrovalvula 3 conectada");
                        PrintWriter pw3 = new PrintWriter(s.getOutputStream(), true);
                        valvulas.put(3, pw3);
                        crearHiloParcelaSiPosible(3);
                        break;
                    case "electroValvula4":
                        System.out.println("Electrovalvula 4 conectada");
                        PrintWriter pw4 = new PrintWriter(s.getOutputStream(), true);
                        valvulas.put(4, pw4);
                        crearHiloParcelaSiPosible(4);
                        break;
                    case "electroValvula5":
                        System.out.println("Electrovalvula 5 conectada");
                        PrintWriter pw5 = new PrintWriter(s.getOutputStream(), true);
                        valvulas.put(5, pw5);
                        crearHiloParcelaSiPosible(5);
                        break;

                    case "electroValvulaFertirrigacion":
                        System.out.println("Electroválvula de Fertirrigación conectada.");
                        PrintWriter pwFert = new PrintWriter(s.getOutputStream(), true);
                        HiloFertirrigacion hf = new HiloFertirrigacion(pwFert);
                        hf.start();
                        break;
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void conectarServidorEM() {
        try {
            servidorEM = (IServicioExclusionMutua) Naming.lookup("rmi://localhost:10000/servidorCentralEM");
            servidorEM.liberarRecursos();
            System.out.println("✅ Conectado al Servidor de Exclusión Mutua RMI.");
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            System.err.println("❌ ERROR RMI: Fallo al conectar con el Servidor EM en 10000. Asegúrese que el ServidorEM esté corriendo.");
            System.exit(1);
        }
    }

    private static void crearHiloParcelaSiPosible(int id) {
        if (!parcelas.containsKey(id) && humedades.containsKey(id) && valvulas.containsKey(id)) {
            HiloParcela hilo = new HiloParcela(
                    id,
                    lluvia,
                    radiacion,
                    temperatura,
                    humedades.get(id),
                    valvulas.get(id)
            );
            hilo.start();
            parcelas.put(id, hilo);
        }

    }

    // Crea hilos de parcelas existentes si antes solo faltaban sensores globales
    private static void crearHilosParcelaExistentes() {
        for (int id : humedades.keySet()) {
            if (!parcelas.containsKey(id) && valvulas.containsKey(id)) {
                crearHiloParcelaSiPosible(id);
            }
        }
    }

    // --- LÓGICA DE RIEGO (SIN CAMBIOS) ---
    public static double calculoINR(double H, double T, double R) {
        return W1 * (1 - H / 100) + W2 * (T / 40) + W3 * (R / 1000);
    }

    public static boolean decidirRiego(boolean L, double INR) {
        return (INR > 0.7 && !L);
    }

    public static int tiempoRiego(double inr) {
        if (inr >= 0.9) return 10;
        else if (inr >= 0.8) return 7;
        else return 5;
    }

    // --- ENCAPSULACIÓN DE LA LLAMADA REMOTA (REEMPLAZANDO LA LÓGICA LOCAL) ---

    /**
     * Llama al método remoto para solicitar la bomba.
     * La firma fue modificada para incluir idParcela, necesario para RIEGO remoto.
     */
    public static void solicitarBomba(int solicitanteId, int idParcela) throws InterruptedException {
        if (servidorEM == null) {
            throw new InterruptedException("Conexión al Servidor EM no disponible. Fallo crítico.");
        }

        try {
            if (solicitanteId == ESTADO_RIEGO) {
                // Riego llama a su método remoto (requiere idParcela)
                servidorEM.solicitarBombaRiego(idParcela);
            } else if (solicitanteId == ESTADO_FERTIRRIGACION) {
                // Fertirrigación llama a su método remoto
                servidorEM.solicitarBombaFertirrigacion();
            }
        } catch (RemoteException e) {
            System.err.println("Error RMI al solicitar bomba: " + e.getMessage());
            // Se propaga como InterruptedException para que el hilo se comporte como si se hubiera bloqueado
            throw new InterruptedException("Fallo de comunicación RMI.");
        }
    }

    public static void liberarBomba(int liberadorId) {
        if (servidorEM == null) {
            System.err.println("Error al liberar: Conexión al Servidor EM no disponible.");
            return;
        }
        try {
            servidorEM.liberarBomba(liberadorId);
        } catch (RemoteException e) {
            System.err.println("Error RMI al liberar bomba: " + e.getMessage());
        }
    }

    public static void notificarLluvia() {
        System.out.println("\n🚨 [ALERTA GLOBAL] Deteniendo riego por LLUVIA detectada.");

        for (HiloParcela hilo : parcelas.values()) {
            if (hilo != null && hilo.estaRegando()) {
                hilo.detenerPorLluvia();
            }
        }
    }
}