package data;

import javax.swing.*;
import java.util.LinkedList;
import java.util.List;

public class NarrowBridge {
    // Lista wszystkich busów, których wątki aktualnie działają
     List<Bus> allBuses = new LinkedList<Bus>();

    // Lista busów (kolejka) oczekujących na wjazd na most
    public List<Bus> busesWaiting = new LinkedList<Bus>();

    // Lista busów poruszających się aktualnie po moście
    public List<Bus> busesOnTheBridge = new LinkedList<Bus>();

    int limitOption = 1;

    int busesWest = 0;
    int busesEast = 0;

    public void setLimitOption(int limitOption) {
        this.limitOption = limitOption;
    }

    // Wydruk informacji o busach oczekujących w kolejce oraz
    // aktualnie przejeżdżających przez most.
    void printBridgeInfo(Bus bus, String message, JTextField field){
        StringBuilder sb = new StringBuilder();
        sb.append("data.Bus["+bus.id+"->"+bus.dir+"]  ");
        sb.append(message+"\n");
        sb.append("           Na moście: ");
        for(Bus b: busesOnTheBridge) sb.append(b.id + "  ");
        sb.append("    Kolejka: ");
        for(Bus b: busesWaiting) sb.append(b.id + "  ");
        System.out.println(sb);
    }

    // Procedura monitora, który wpuszcza busy na most
    synchronized void getOnTheBridge(Bus bus){
        switch (limitOption){
            case 1 -> {
            }
            case 2 -> {
                // Prosty warunek wjazudu na most:
                // DOPÓKI LISTA BUSÓW NA MOŚCIE NIE JEST PUSTA
                // KOLEJNY BUS MUSI CZEKAĆ NA WJAZD
                while( !busesOnTheBridge.isEmpty()){
                    // dodanie busa do listy oczekujących
                    busesWaiting.add(bus);
                    printBridgeInfo(bus, "CZEKA NA WJAZD");
                    try {
                        wait();
                    } catch (InterruptedException e) { }
                    // usunięcie busa z listy oczekujących.
                    busesWaiting.remove(bus);
                }

            }
            case 3 -> {
                while (busesOnTheBridge.size() >= 3){
                    busesWaiting.add(bus);
                    printBridgeInfo(bus, "CZEKA NA WJAZD");
                    try {
                        wait();
                    } catch (InterruptedException e) { }
                    // usunięcie busa z listy oczekujących.
                    busesWaiting.remove(bus);
                }
            }
            case 4 -> {
                if(bus.dir.equals(BusDirection.EAST)){
                    busesEast++;
                    while(busesEast >= 3){
                        // dodanie busa do listy oczekujących
                        busesWaiting.add(bus);
                        printBridgeInfo(bus, "CZEKA NA WJAZD");
                        try {
                            wait();
                        } catch (InterruptedException e) { }
                        // usunięcie busa z listy oczekujących.
                        busesWaiting.remove(bus);
                    }
                }
                else{
                    busesWest++;
                    while(busesWest >= 3){
                        // dodanie busa do listy oczekujących
                        busesWaiting.add(bus);
                        printBridgeInfo(bus, "CZEKA NA WJAZD");
                        try {
                            wait();
                        } catch (InterruptedException e) { }
                        // usunięcie busa z listy oczekujących.
                        busesWaiting.remove(bus);
                    }
                }

            }

        }
        // dodanie busa do listy jadących przez most
        busesOnTheBridge.add(bus);
        printBridgeInfo(bus, "WJEŻDŻA NA MOST");
    }

    // Procedura monitora, która rejestruje busy opuszczające most
    // i powiadamia inne busy oczekujące w kolejce na wjazd
    synchronized void getOffTheBridge(Bus bus){
        // usunięcie busa z listy poruszających się przez most
        busesOnTheBridge.remove(bus);
        printBridgeInfo(bus, "OPUSZCZA MOST");
        // powiadomienie innych oczekujących.
        notify();
    }

}  // koniec klasy data.NarrowBridge
