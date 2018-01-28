/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gasolineraventanas;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author anna
 */
public class Monitor {

    Gasolinero gasolinero;
    Temporizador temp;
    static ArrayList<Coche> coches = new ArrayList();
    VentanaGasolinera ventana;
    static ThreadGroup diesel = new ThreadGroup("diesel");
    static ThreadGroup gasolina = new ThreadGroup("gasolina");
    static int tiempoJuego, cantidadCoches, depositoGasolina, depositoDiesel, litrosGrupoDiesel, litrosGrupoGasolina, contDiesel = 0, contGasolina = 0, litros;

    static boolean turnoGasolinero = true, ganadorDiesel, controlLitros;
    boolean finTiempo, finPorGanador;
    static String nombreGanador = " SIN GANADOR";

    public Monitor() {
        prepararInicio();
        arrancarHilos();
    }

    public Monitor(int i) {

    }

    private void prepararInicio() {
        gasolinero = new Gasolinero(this);
        temp = new Temporizador(this, tiempoJuego);

        for (int i = 1; i <= cantidadCoches; i++) {
            //IMPAR
            if (i % 2 != 0) {
                contDiesel++;
                coches.add(new Coche(this, diesel, contDiesel));

            } else {
                //PAR
                contGasolina++;
                coches.add(new Coche(this, gasolina, contGasolina));
            }
        }

    }

    private void arrancarHilos() {
        Thread tempori = new Thread(temp);
        tempori.start();
        Thread gasolin = new Thread(this.gasolinero);
        gasolin.start();

        for (Coche coche : coches) {
            Thread car = new Thread(coche);
            car.start();
        }

    }

    public synchronized void repostarGasolinera() {
        if (turnoGasolinero && !finTiempo) {
            System.out.println("\t\tVOY A REPOSTAR!!!!!");
            depositoDiesel += 50;
            depositoGasolina += 50;

            turnoGasolinero = false;
            controlLitros = false;
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                Logger.getLogger(Monitor.class.getName()).log(Level.SEVERE, null, ex);
            }
            notifyAll();
        }
    }

    private int calcularRepostaje() {
        return (int) ((Math.random() * 20) + 1);

    }

    public synchronized void repostarCoche(Coche coche) {
        if (!finPorGanador && !finTiempo) {
            if (turnoGasolinero) {
                try {
                    System.out.println("\n\tCoche del grupo " + coche.getGrupo().getName() + " "
                            + coche.getContador() + ", no es tu turno");
                    wait();
                } catch (InterruptedException ex) {
                }
            } else {

                litros = calcularRepostaje();
                if (coche.getGrupo().getName().equalsIgnoreCase("diesel")) {
                    if (depositoDiesel > litros) {
                        depositoDiesel -= litros;
                        litrosGrupoDiesel += litros;
                        coche.setLitrosDiesel(coche.getLitrosDiesel() + litros);
                        System.out.println("\tEl coche " + coche.getContador() + " del grupo "
                                + coche.getGrupo().getName() + " rellenó " + litros + ", y lleva acumulados "
                                + coche.getLitrosDiesel() + " litros");

                    } else {
                        turnoGasolinero = true;
                        litrosGrupoDiesel += depositoDiesel;
                        coche.setLitrosDiesel(coche.getLitrosDiesel() + depositoDiesel);
                        depositoDiesel = 0;
                        System.out.println("\tEl deposito no tenia suficiente diesel. El coche " + coche.getContador() + " del grupo "
                                + coche.getGrupo().getName() + " rellenó " + litros + ", y lleva acumulados "
                                + coche.getLitrosDiesel() + " litros");

                        //CAMBIAMOS LA BOOLEANA YA QUE EL DEPOSITO ESTÁ A 0
                        turnoGasolinero = true;

                    }

                } else {
                    if (depositoGasolina > litros) {

                        litrosGrupoGasolina += litros;
                        depositoGasolina -= litros;
                        coche.setLitrosGasolina(coche.getLitrosGasolina() + litros);
                        System.out.println("\tEl coche " + coche.getContador() + " del grupo "
                                + coche.getGrupo().getName() + " rellenó " + litros + ", y lleva acumulados "
                                + coche.getLitrosGasolina() + " litros");

                    } else {
                        controlLitros = true;
                        litrosGrupoGasolina += depositoGasolina;
                        coche.setLitrosGasolina(coche.getLitrosGasolina() + depositoGasolina);
                        depositoGasolina = 0;
                        System.out.println("\tEl deposito no tenia suficiente gasolina. El coche " + coche.getContador() + " del grupo "
                                + coche.getGrupo().getName() + " rellenó " + litros + ", y lleva acumulados "
                                + coche.getLitrosGasolina() + " litros");

                        //CAMBIAMOS LA BOOLEANA YA QUE EL DEPOSITO ESTÁ A 0
                        turnoGasolinero = true;
                    }
                }
                saberSiHayGanador(coche);

            }
            notifyAll();
        }

    }

    public void saberSiHayGanador(Coche coche) {
        if (litrosGrupoDiesel >= 250 || litrosGrupoGasolina >= 250) {
            if (coche.getGrupo().getName().equalsIgnoreCase("diesel")) {
                System.out.println(coche.getGrupo().getName() + ", GANAN POR LLEGAR A " + litrosGrupoDiesel + " litros");
                nombreGanador = "DIESEL";
                ganadorDiesel = true;

            } else if (coche.getGrupo().getName().equalsIgnoreCase("gasolina")) {
                System.out.println(coche.getGrupo().getName() + ", GANAN POR LLEGAR A " + litrosGrupoGasolina + " litros");
                nombreGanador = "GASOLINA";
            }

            finPorGanador = true;

        }

    }

    public static String getNombreGanador() {
        return nombreGanador;
    }

    public static void setNombreGanador(String nombreGanador) {
        Monitor.nombreGanador = nombreGanador;
    }

    public static boolean isControlLitros() {
        return controlLitros;
    }

    public static void setDiesel(ThreadGroup diesel) {
        Monitor.diesel = diesel;
    }

    public static void setGasolina(ThreadGroup gasolina) {
        Monitor.gasolina = gasolina;
    }

    public static int getTiempoJuego() {
        return tiempoJuego;
    }

    public static void setTiempoJuego(int tiempoJuego) {
        Monitor.tiempoJuego = tiempoJuego;
    }

    public static int getCantidadCoches() {
        return cantidadCoches;
    }

    public static void setCantidadCoches(int cantidadCoches) {
        Monitor.cantidadCoches = cantidadCoches;
    }

    public static int getDepositoGasolina() {
        return depositoGasolina;
    }

    public static void setDepositoGasolina(int depositoGasolina) {
        Monitor.depositoGasolina = depositoGasolina;
    }

    public static int getDepositoDiesel() {
        return depositoDiesel;
    }

    public static void setDepositoDiesel(int depositoDiesel) {
        Monitor.depositoDiesel = depositoDiesel;
    }

    public static int getLitrosGrupoDiesel() {
        return litrosGrupoDiesel;
    }

    public static void setLitrosGrupoDiesel(int litrosGrupoDiesel) {
        Monitor.litrosGrupoDiesel = litrosGrupoDiesel;
    }

    public static int getLitrosGrupoGasolina() {
        return litrosGrupoGasolina;
    }

    public static void setLitrosGrupoGasolina(int litrosGrupoGasolina) {
        Monitor.litrosGrupoGasolina = litrosGrupoGasolina;
    }

    public static int getContDiesel() {
        return contDiesel;
    }

    public static void setContDiesel(int contDiesel) {
        Monitor.contDiesel = contDiesel;
    }

    public static int getContGasolina() {
        return contGasolina;
    }

    public static void setContGasolina(int contGasolina) {
        Monitor.contGasolina = contGasolina;
    }

    public static int getLitros() {
        return litros;
    }

    public static void setLitros(int litros) {
        Monitor.litros = litros;
    }

    public static boolean isTurnoGasolinero() {
        return turnoGasolinero;
    }

    public static void setTurnoGasolinero(boolean turnoGasolinero) {
        Monitor.turnoGasolinero = turnoGasolinero;
    }

    public boolean isFinTiempo() {
        return finTiempo;
    }

    public void setFinTiempo(boolean finTiempo) {
        this.finTiempo = finTiempo;
    }

    public boolean isFinPorGanador() {
        return finPorGanador;
    }

    public void setFinPorGanador(boolean finPorGanador) {
        this.finPorGanador = finPorGanador;
    }

    public static boolean isGanadorDiesel() {
        return ganadorDiesel;
    }

    public static void setGanadorDiesel(boolean ganadorDiesel) {
        Monitor.ganadorDiesel = ganadorDiesel;
    }

}
