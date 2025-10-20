package com.sistdist.sistemarespaldo1;

import com.sistdist.interfaces.IDetectorFalla;
import com.sistdist.interfaces.IEleccionAnillo;
import com.sistdist.interfaces.IServicioExclusionMutua;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author dgera
 */

public class SistemaRespaldo1 extends UnicastRemoteObject implements IDetectorFalla, IEleccionAnillo {

    private static final int MI_ID = 2;
    private static final int MI_PUERTO_RMI = 9001;
    private static final int ID_MAESTRO = 1;
    private static final int ID_SC = 3;
    private static final String DIRECCION_MAESTRO_RMI = "rmi://localhost:9000/SistemaCentralRMI";
    private static Boolean M_CAIDO = false;
    private static Boolean S_CAIDO = false;
    private static Boolean coordinador = false;

    private static final String DIRECCION_SUCESOR = "rmi://localhost:9002/ServidorRespaldo3";
    private static IEleccionAnillo sucesor = null;
    private static Integer idCoordinadorActual = null;

    private static IDetectorFalla maestroMonitor = null;
    private static final long TIEMPO_HEARTBEAT_MS = 3000;
    private Timer timer;

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
    private static HiloFertirrigacion fertirrigacion = null;

    public static final int ESTADO_RIEGO = 1;
    public static final int ESTADO_FERTIRRIGACION = 2;

    private static SistemaRespaldo1 instanciaRMI;

    private static ServerSocket servidorAplicacion = null;

    public SistemaRespaldo1() throws RemoteException {
        super(MI_PUERTO_RMI);
        int condition = 1;
        while (condition < 4) {
            int puerto = obtenerPuertoRMI(condition);
            try {
                IEleccionAnillo servidor = (IEleccionAnillo) Naming.lookup(obtenerDireccion(puerto));
                servidor.estoyVivo(2);
                if (servidor.soyCoordinador()) {
                    idCoordinadorActual = condition;
                }
                condition = condition + 2;
            } catch (Exception e) {
                System.out.println("ERROR: No se encuentra el servidor con puerto: " + puerto);
                if (condition == 1) {
                    M_CAIDO = true;
                } else {
                    S_CAIDO = true;
                }
                condition = condition + 2;
            }
        }
    }

    @Override
    public void pulso() throws RemoteException {
    }

    @Override
    public void estoyVivo(int id) throws RemoteException {
        if (id == 1) {
            M_CAIDO = false;
            idCoordinadorActual = 1;
            coordinador = false;
            if (servidorAplicacion != null && !servidorAplicacion.isClosed()) {
                try {
                    servidorAplicacion.close();
                    servidorAplicacion = null;
                    System.out.println("🛑 CERRANDO SERVIDOR SOCKET (20000) forzadamente. Servidor central de vuelta levantado.");
                    detenerTodosLosRecursos();
                } catch (IOException ignored) {
                }
            }
        } else {
            S_CAIDO = false;
        }
    }


    private void detenerTodosLosRecursos() {
        if (temperatura != null) {
            temperatura.apagar();
        }
        if (lluvia != null) {
            lluvia.apagar();
        }
        if (radiacion != null) {
            radiacion.apagar();
        }
        if (fertirrigacion != null) {
            fertirrigacion.apagar();
        }
        for (HiloParcela hilo : parcelas.values()) {
            if (hilo != null && hilo.isAlive()) {
                hilo.detener();
            }
        }
        parcelas.clear();
        for (HiloHumedad hilo : humedades.values()) {
            hilo.apagar();
        }
        humedades.clear();
        for (PrintWriter pw : valvulas.values()) {
            if (pw != null) {
                pw.close();
            }
        }
        valvulas.clear();
        temperatura = null;
        lluvia = null;
        radiacion = null;
    }

    @Override
    public Boolean soyCoordinador() {
        return coordinador;
    }

    private static void conectarSucesor() {
        if (sucesor != null) {
            return;
        }

        try {
            if (S_CAIDO) {
                sucesor = (IEleccionAnillo) Naming.lookup(DIRECCION_MAESTRO_RMI);
                M_CAIDO = false;
            }

            if (M_CAIDO) {
                sucesor = (IEleccionAnillo) Naming.lookup(DIRECCION_SUCESOR);
                S_CAIDO = false;
            }
        } catch (Exception e) {
            System.err.println("❌ ERROR: No se pudo conectar con ningún sucesor.");
            sucesor = null;
            S_CAIDO = true;
            M_CAIDO = true;

        }
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
    public void iniciarEleccion(List<Integer> idsParticipantes, Integer maestroElegido) throws RemoteException {
        if (S_CAIDO && M_CAIDO) {
            coordinadorElegido(MI_ID, "rmi://localhost:9001/ServidorRespaldo2");
            return;
        }
        conectarSucesor();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
        if (idsParticipantes.contains(MI_ID)) {
            int puertoNuevoCoordinador = obtenerPuertoRMI(maestroElegido);
            String dirNuevoCoordinador = obtenerDireccion(puertoNuevoCoordinador);

            System.out.println("\n🎉 ELECCIÓN FINALIZADA. El nuevo COORDINADOR es ID: " + maestroElegido + ".");
            if (sucesor != null) {
                sucesor.coordinadorElegido(maestroElegido, dirNuevoCoordinador);
            }
            coordinadorElegido(maestroElegido, dirNuevoCoordinador);
        } else {
            idsParticipantes.add(MI_ID);
            System.out.println("🔄 Elección: Respaldo " + MI_ID + " reenvía mensaje. Participantes: " + idsParticipantes);
            try {
                if (sucesor != null) {
                    sucesor.iniciarEleccion(idsParticipantes, maestroElegido > MI_ID ? maestroElegido : MI_ID);
                }
            } catch (Exception e) {
                System.err.println("❌ ERROR RMI: Sucesor en " + DIRECCION_SUCESOR + " falló. Anillo roto.");
            }
        }
    }

    @Override
    public void coordinadorElegido(int idCoordinador, String direccionCoordinador) throws RemoteException {

        if (idCoordinadorActual != null && idCoordinadorActual == idCoordinador) {
            return;
        }

        idCoordinadorActual = idCoordinador;

        if (idCoordinador == MI_ID) {
            System.out.println("\n👑 ¡VICTORIA! SOY EL NUEVO SISTEMA CENTRAL (ID: " + MI_ID + ").");
            System.out.println("🚀 ASUMIENDO CONTROL: INICIANDO SERVIDOR SOCKET EN PUERTO 20000...");
            coordinador = true;
            timer.cancel();
            new Thread(() -> {
                try {
                    servidorEM.liberarRecursos();
                    iniciarServidorAplicacion();
                } catch (IOException e) {
                    System.err.println("ERROR CRÍTICO al iniciar la aplicación: " + e.getMessage());
                    detenerTodosLosRecursos();
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException ex) {

                    }
                    iniciarMonitorMaestro();
                }
            }).start();

        } else if (idCoordinador == ID_MAESTRO) {
            System.out.println("⬇️ El Maestro (ID 1) se ha recuperado. Reanudando monitoreo...");
            coordinador = false;
            iniciarMonitorMaestro();
        } else {
            System.out.println("⬇️ ROL: Coordinador cambiado a ID: " + idCoordinador);
            coordinador = false;
            iniciarMonitorMaestro();
        }

        try {
            Thread.sleep(2000);
            System.out.println("Propagando anuncio de nuevo coordinador al siguiente nodo del anillo...");
            sucesor = null;
            conectarSucesor();
            if (sucesor != null) {
                sucesor.coordinadorElegido(idCoordinador, direccionCoordinador);
            }
        } catch (Exception e) {
            System.err.println("❌ ERROR RMI: Sucesor en " + DIRECCION_SUCESOR + " falló al recibir anuncio.");
        }
    }

    private void iniciarMonitorMaestro() {
        try {
            Thread.sleep(5000);
            if (idCoordinadorActual != null) {
                System.out.println("Monitor Heartbeat iniciado. Chequeando a SC Maestro (ID: " + idCoordinadorActual + ")");
            }
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    chequearMaestro();
                }
            }, 0, TIEMPO_HEARTBEAT_MS);
        } catch (Exception e) {
        }
    }

    private void chequearMaestro() {
        try {
            if (maestroMonitor == null) {
                int puertoMaestro = obtenerPuertoRMI(idCoordinadorActual);
                maestroMonitor = (IDetectorFalla) Naming.lookup(obtenerDireccion(puertoMaestro));
            }
            System.out.println("Pulso enviado a servidor maestro (ID: " + idCoordinadorActual + ")");
            maestroMonitor.pulso();
        } catch (Exception e) {
            System.err.println("\n🚨 FALLA DETECTADA: Servidor central ha fallado.");
            timer.cancel();
            maestroMonitor = null;
            try {
                Thread.sleep(2000);
                if (idCoordinadorActual == null) {
                    M_CAIDO = true;
                    S_CAIDO = true;
                    instanciaRMI.iniciarEleccion(new ArrayList<Integer>(), MI_ID);
                    return;
                }

                if (idCoordinadorActual == ID_SC) {
                    S_CAIDO = true;
                    System.out.println(S_CAIDO + " " + M_CAIDO);
                    System.out.println("🚀 INICIANDO ALGORITMO DE ELECCIÓN (ANILLO)...");
                    instanciaRMI.iniciarEleccion(new ArrayList<Integer>(), MI_ID);
                    return;
                }

                M_CAIDO = true;
                sucesor = null;
                conectarSucesor();
                if (sucesor == null) {
                    S_CAIDO = true;
                    instanciaRMI.iniciarEleccion(new ArrayList<Integer>(), MI_ID);
                }
            } catch (RemoteException ex) {
                System.err.println("Fallo al iniciar la elección RMI.");
            } catch (Exception exception) {
                System.err.println("Fallo de ejecución.");
            }
        }
    }


    private void iniciarServidorAplicacion() throws IOException {
        try {
            servidorAplicacion = new ServerSocket(20000);
            System.out.println("Servidor Socket APLICACIÓN (Coordinador) esperando dispositivos en puerto 20000......");

            while (M_CAIDO) {
                Socket s = servidorAplicacion.accept();
                BufferedReader bf = new BufferedReader(new InputStreamReader(s.getInputStream()));
                String tipoDispositivo = bf.readLine();

                switch (tipoDispositivo) {
                    // Sensores de humedad
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
                        HiloFertirrigacion hf = new HiloFertirrigacion(pwFert, s);
                        hf.start();
                        fertirrigacion = hf;
                        break;
                }
            }

            System.out.println("Servidor central nuevamente levantado. Cediendo control...");
            servidorAplicacion.close();
            servidorAplicacion = null;
        } catch (IOException ex) {
            throw ex;
        }
    }


    public static void main(String[] args) {

        try {
            conectarServidorEM();
            instanciaRMI = new SistemaRespaldo1();
            LocateRegistry.createRegistry(MI_PUERTO_RMI);
            Naming.rebind("rmi://localhost:" + MI_PUERTO_RMI + "/ServidorRespaldo" + MI_ID, instanciaRMI);
            System.out.println("✅ Sistema Respaldo 1 (ID: " + MI_ID + ") RMI listo en puerto " + MI_PUERTO_RMI);
            conectarSucesor();
            instanciaRMI.iniciarMonitorMaestro();
        } catch (IOException ex) {
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
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }

    }

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

    public static void solicitarBomba(int solicitanteId, int idParcela) throws InterruptedException {
        if (servidorEM == null) {
            throw new InterruptedException("Conexión al Servidor EM no disponible. Fallo crítico.");
        }

        try {
            if (solicitanteId == ESTADO_RIEGO) {
                servidorEM.solicitarBombaRiego(idParcela);
            } else if (solicitanteId == ESTADO_FERTIRRIGACION) {
                servidorEM.solicitarBombaFertirrigacion();
            }
        } catch (RemoteException e) {
            System.err.println("Error RMI al solicitar bomba: " + e.getMessage());
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