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
public class Gasolinero implements Runnable{

    Monitor mon;
    private int veces=0;

    public Gasolinero(Monitor mon) {
        this.mon = mon;
        System.out.println("GASOLINERO");
    }
    
    @Override
    public void run() {
         while (!mon.isFinPorGanador()&&!mon.isFinTiempo()) {
            try {
                mon.repostarGasolinera();
                veces++;
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
            }
        }
    }

    public int getVeces() {
        return veces;
    }

}
