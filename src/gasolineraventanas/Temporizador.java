/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gasolineraventanas;

/**
 *
 * @author anna
 */
public class Temporizador implements Runnable {

    Monitor mon;
    static int tiempo;

    public Temporizador(Monitor mon, int tiempo) {
        this.mon = mon;
        this.tiempo = tiempo;
    }

 
    
    
    public void run() {
        System.out.println("EMPIEZA EL TEMPORIZADOR");
        for (int i = 0; i < tiempo;) {
            try {
                 tiempo--;
                Thread.sleep(1000);
               
            } catch (InterruptedException ex) {
            }
            if (mon.isFinPorGanador()) {
                System.out.println("\nSe para el temporizador por ganador");
                
                return;
            }
        }
        mon.setFinTiempo(true);
        System.out.println("------- FIN DEL TIEMPO ----------");
        

    }

    public static int getTiempo() {
        return tiempo;
    }

  
    

}
