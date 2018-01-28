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
public class Coche implements Runnable {

    Monitor mon;
    ThreadGroup grupo;
    private int contador, cantidadLitros, litrosDiesel, litrosGasolina;
   

  public Coche(Monitor mon, ThreadGroup g, int c) {
        this.mon = mon;
        grupo = g;
        contador = c;
        System.out.println("Soy el coche " + contador + " del grupo " + grupo.getName());
    }

    
    @Override
    public void run() {
       while (!mon.isFinPorGanador()&&!mon.isFinTiempo()) {
            mon.repostarCoche(this);
            
            try {
                Thread.sleep(1500);
            } catch (InterruptedException ex) {
            }
        }
    }

    
    public int getContador() {
        return contador;
    }

    public ThreadGroup getGrupo() {
        return grupo;
    }

    public int getLitrosDiesel() {
        return litrosDiesel;
    }

    public void setLitrosDiesel(int litrosDiesel) {
        this.litrosDiesel = litrosDiesel;
    }

    public int getLitrosGasolina() {
        return litrosGasolina;
    }

    public void setLitrosGasolina(int litrosGasolina) {
        this.litrosGasolina = litrosGasolina;
    }

}
