/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package electrovalvulabomba;

/**
 *
 * @author Usuario
 */
public class ElectroValvulaBomba {

    public static void main(String[] args) {
        HiloValvula electrovalvulabomba = new HiloValvula();
        electrovalvulabomba.start();
        try {
            Thread.sleep(15000);
        } catch (InterruptedException ex) {
            System.getLogger(ElectroValvulaBomba.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        electrovalvulabomba.apagar();
    }
}
