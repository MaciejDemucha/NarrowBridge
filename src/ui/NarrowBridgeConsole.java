package ui;

import data.Bus;
import data.NarrowBridge;

/*
 *  Symulacja problemu przejazdu przez wąski most
 *  Wersja konsolowa
 *
 *  Autor: Paweł Rogaliński
 *   Data: 1 grudnia 2019 r.
 */

public class NarrowBridgeConsole{

    // Parametr TRAFFIC określa natężenie ruchu busów.
    // Może przyjmować wartości z przedziału [0, 5000]
    //    0 - bardzo małe natężenie (nowy bus co 5500 ms)
    // 5000 - bardzo duże natężenie (nowy bus co 500 ms )
    private static int traffic = 1000;

    public static void setTraffic(int traffic) {
        NarrowBridgeConsole.traffic = traffic;
    }

    public static void main(String[] args) {
        NarrowBridge bridge = new NarrowBridge();

        // Zadaniem tej pętli jest tworzenie kolejnych busów,
        // które mają przewozić przez most pasażerów:
        // Przerwy pomiędzy kolejnymi busami są generowane losowo.
        while (true) {
            // Utworzenie nowego busa i uruchomienie wątku,
            // który symuluje przejazd busa przez most.
            Bus bus = new Bus(bridge);
            new Thread(bus).start();

            // Przerwa przed utworzeniem kolejnego busa
            try {
                Thread.sleep(5500 - traffic);
            } catch (InterruptedException e) {
            }
        }
    }

}  // koniec klasy ui.NarrowBridgeConsole



