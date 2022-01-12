/*
 *  Program: Symulacja przejazdu przez wąski most
 *     Plik: Bus.java
 *
 *  Obiekty klasy Bus reprezentują busy przejeżdżające przez most.
 *
 *    Autor: Maciej Demucha
 *    Data:  14 stycznia 2022 r.
 */

package data;

import java.awt.*;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Bus implements Runnable {

    // Stałe określające minimalny i maksymalny czas
    // oczeiwania na nowych pasażerów.
    public static final int MIN_BOARDING_TIME = 1000;
    public static final int MAX_BOARDING_TIME = 10000;

    // Stała określająca czas dojazdu busa do mostu.
    public static final int GETTING_TO_BRIDGE_TIME = 500;

    // Stała określająca czas przejazdu przez most.
    public static final int CROSSING_BRIDGE_TIME = 3000;

    // Stała określająca czas przjezdu od mostu do końcowego parkingu.
    public static final int GETTING_PARKING_TIME = 500;

    // Stała określająca czas wysiadania pasażerów z busa
    public static final int UNLOADING_TIME = 500;


    // Liczba wszystkich busów, które zostału utworzone
    // od początku działania programu
    private static int numberOfBuses = 0;




    // Metoda usypia wątek na podany czas w milisekundach
    public static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
        }
    }

    // Metoda usypia wątek na losowo dobrany czas
    // z przedziału [min, max) milsekund
    public static void sleep(int min_millis, int max_milis) {
        sleep(ThreadLocalRandom.current().nextInt(min_millis, max_milis));
    }


    // Referencja na obiekt reprezentujący most.
    NarrowBridge bridge;

    // Unikalny identyfikator każdego busa.
    // Jako identyfikator zostanie użyty numer busa,
    // który został utworzony od początku działania programu
    int id;
    int x;
    int y;
    int r = 15;

    // Kierunek jazdy busa nadany w sposób losowy
    BusDirection dir;


    public Bus(NarrowBridge bridge){
        this.bridge = bridge;
        this.id = ++numberOfBuses;
        if (ThreadLocalRandom.current().nextInt(0, 2) == 0)
            this.dir = BusDirection.EAST;
        else this.dir = BusDirection.WEST;

        if(this.dir.equals(BusDirection.EAST)){
            this.x = 20;
        }
        else{
            this.x = 570;
        }

        Random rand = new Random();
        this.y = rand.nextInt(650-r);
    }


    // Wydruk w konsoli informacji o stanie busa
    void printBusInfo(String message){
        System.out.println("data.Bus[" + id + "->"+dir+"]: " + message);
    }

    //Metoda zwracająca numer busa
    public String getId() {
        return String.valueOf(id);
    }

    // Symulacja oczekiwania na nowych pasażerów.
    void boarding() {
        printBusInfo("Czeka na nowych pasażerów");
        sleep(MIN_BOARDING_TIME, MAX_BOARDING_TIME);
    }

    // Symulacja dojazdu ze stacji początkowej do mostu
    void goToTheBridge() {
        printBusInfo("Jazda w stronę mostu");
        sleep(GETTING_TO_BRIDGE_TIME);
    }

    // Symulacja przejazdu przez most
    void rideTheBridge(){
        printBusInfo("Przejazd przez most");
        sleep(CROSSING_BRIDGE_TIME);
    }

    // Symulacja przejazdu od mostu do końcowego parkingu
    void goToTheParking(){
        printBusInfo("Jazda w stronę końcowego parkingu");
        sleep(GETTING_PARKING_TIME);
    }

    // Symulacja opuszczenia pojazdu na przystanku końcowym
    void unloading(){
        printBusInfo("Rozładunek pasażerów");
        sleep(UNLOADING_TIME);
    }


    // Metoda realizuje "cykl życia" pojedynczego busa
    public void run() {
        bridge.allBuses.add(this);

        // oczekiwanie na nowych pasażerów
        boarding();

        // jazda w kierunku mostu
        goToTheBridge();

        //Wjazd na most
        bridge.getOnTheBridge(this);

        // przejazd przez most
        rideTheBridge();

        bridge.getOffTheBridge(this);

        // jazda w kierunku parkingu końcowego
        goToTheParking();

        // wypuszczenie pasażerów
        unloading();
        bridge.allBuses.remove(this);

        // koniec "życia" wątku
        bridge.allBuses.remove(this);
    }

    void draw(Graphics2D g){

        if(this.dir.equals(BusDirection.EAST)){
            g.setColor(Color.red);
        }
        else{
            g.setColor(Color.green);
        }
        g.fillOval(this.x-r, this.y-r, 2*r, 2*r);
        g.setColor(Color.black);
        g.drawString(this.getId(), this.x, this.y);
    }

}
