/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package com.sistdist.bomba;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


/**
 *
 * @author miria
 */
public class MonitorBomba {

    private final ReentrantLock lock;
    private final Condition disponible;
    private boolean ocupada;

    public MonitorBomba() {
        this.lock = new ReentrantLock();
        this.disponible = lock.newCondition();
        this.ocupada = false;
    }

    // Cualquier sistema (riego o fertirrigación) pide la bomba
    public void pedirBomba(String sistema) {
        lock.lock();
        try {
            while (ocupada) { // si ya está ocupada, esperar
                disponible.await();
            }
            ocupada = true;
            System.out.println("Bomba asignada a: " + sistema);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
    }

    // El sistema libera la bomba
    public void liberarBomba(String sistema) {
        lock.lock();
        try {
            ocupada = false;
            System.out.println("Bomba liberada por: " + sistema);
            disponible.signal(); // despertar a quien espere
        } finally {
            lock.unlock();
        }
    }
}

