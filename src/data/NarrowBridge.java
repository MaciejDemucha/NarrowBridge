/*
 *  Program: Symulacja przejazdu przez wąski most
 *     Plik: NarrowBridge.java
 *
 *  Klasa NarrowBridge reprezentuje most przez który przejeżdżają obiekty klasy
 *  Bus. Most posiada 4 ograniczenia ruchu:
 *  - 1 bus jednocześnie
 *  - 3 busy jednocześnie
 *  - 3 busy jednocześnie w jednym kierunku
 *  - bez ograniczeń
 *
 *  Klasa dziedziczy po JFrame i posiada prosty interfejs graficzny:
 *  - textArea wyświetlający komunikaty z konsoli
 *  - suwak ustawiający natężenie ruchu
 *  - pola tekstowe wyświetlające listy busów na moście i w kolejce
 *  - comboBox z opcjami ograniczenia ruchu
 *
 *    Autor: Maciej Demucha
 *    Data:  14 stycznia 2022 r.
 */

package data;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class NarrowBridge extends JFrame {
    // Lista wszystkich busów, których wątki aktualnie działają
     List<Bus> allBuses = new LinkedList<Bus>();

    // Lista busów (kolejka) oczekujących na wjazd na most
    public List<Bus> busesWaiting = new LinkedList<Bus>();

    // Lista busów poruszających się aktualnie po moście
    public List<Bus> busesOnTheBridge = new LinkedList<Bus>();

    //Zmienna określająca aktualnie ustawiony limit przejazdu
    int limitOption = 1;
    //Zmienne wykorzystywane w ruchu jednokierunkowym:
    //Liczba busów jadących w kierunku zachodnim
    int busesWest = 0;
    //Liczba busów jadących w kierunku wschodnim
    int busesEast = 0;

    // Wydruk informacji o busach oczekujących w kolejce oraz
    // aktualnie przejeżdżających przez most.
    void printBridgeInfo(Bus bus, String message){
        StringBuilder sb = new StringBuilder();
        sb.append("data.Bus["+bus.id+"->"+bus.dir+"]  ");
        sb.append(message+"\n");
        sb.append("           Na moście: ");
        for(Bus b: busesOnTheBridge) sb.append(b.id + "  ");
        sb.append("    Kolejka: ");
        for(Bus b: busesWaiting) sb.append(b.id + "  ");
        System.out.println(sb);

        //Utworzenie listy busów w kolejce w postaci Stringa
        StringBuilder waiting = new StringBuilder();
        for (Bus busWaiting: busesWaiting) {
            waiting.append(busWaiting.getId());
            waiting.append(", ");
        }

        //Utworzenie listy busów na moście w postaci Stringa
        StringBuilder onBridge = new StringBuilder();
        for (Bus busOnBridge: busesOnTheBridge) {
            onBridge.append(busOnBridge.getId());
            onBridge.append(", ");
        }
        //Ustawienie list busów w odpowiednich polach tekstowych
        this.tFieldOnBridge.setText(onBridge.toString());
        this.tFieldQueue.setText(waiting.toString());
    }

    // Procedura monitora, który wpuszcza busy na most
    synchronized void getOnTheBridge(Bus bus){
        switch (limitOption){
            //Bez ograniczeń
            case 1 -> {
            }
            //1 bus maksymalnie
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
            //3 busy maksymalnie
            }
            case 3 -> {
                while (busesOnTheBridge.size() >= 3){
                    // dodanie busa do listy oczekujących
                    busesWaiting.add(bus);
                    printBridgeInfo(bus, "CZEKA NA WJAZD");
                    try {
                        wait();
                    } catch (InterruptedException e) { }
                    // usunięcie busa z listy oczekujących.
                    busesWaiting.remove(bus);
                }
                //3 busy w jednym kierunku
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

 //Interfejs graficzny

    private static final String AUTHOR =
            "Języki programowania - Laboratorium 5\n" +
                    "Program: Symulacja przejazdu przez most\n" +
                    "Autor: Maciej Demucha 259111\n" +
                    "Data: 14 stycznia 2022 r.\n";

    //Zmienna wykorzystywana przy obliczaniu odstępu pomiędzy utworzeniem kolejnych busów
    private static int traffic = 1000;

    private JLabel limitLabel = new JLabel("Ograniczenie ruchu: ");
    private JComboBox comboBoxLimit = new JComboBox();
    private JLabel densityLabel = new JLabel("Natężenie ruchu: ");
    private JSlider sliderDensity = new JSlider(0, 5000);
    private JLabel smallLabel = new JLabel("Małe");
    private JLabel bigLabel = new JLabel("Duże");
    private JLabel queueLabel = new JLabel("W kolejce: ");
    private JLabel onBridgeLabel = new JLabel("Na moście: ");
    private JTextField tFieldOnBridge = new JTextField(33);
    private JTextField tFieldQueue = new JTextField(33);
    private JTextArea textAreaNotifications = new JTextArea(35,53);
    private JScrollPane areaScroll = new JScrollPane(textAreaNotifications);

    private JMenuBar menuBar = new JMenuBar();
    private JMenu menu = new JMenu("Menu");
    private JMenuItem author = new JMenuItem("Autor");
    private JMenuItem exit = new JMenuItem("Zakończ");

    Font font = new Font("MonoSpaced", Font.BOLD, 12);

    private JPanel Panel = new JPanel();

    public NarrowBridge(){
        super("Symulacja przejazdu przez wąski most");
        //Przekierowanie strumienia wyjściowego do textArea
        System.setOut(new CustomOutputStream(textAreaNotifications, System.out));
        System.setErr(new CustomOutputStream(textAreaNotifications, System.err));

        setLayout(new FlowLayout());

        menu.add(author);
        menuBar.add(menu);
        setJMenuBar(menuBar);

        Panel.add(limitLabel);
        Panel.add(comboBoxLimit);
        Panel.add(densityLabel);
        Panel.add(smallLabel);
        Panel.add(sliderDensity);
        Panel.add(bigLabel);
        Panel.add(queueLabel);
        Panel.add(tFieldQueue);
        Panel.add(onBridgeLabel, BorderLayout.WEST);
        Panel.add(tFieldOnBridge);
        Panel.add(areaScroll);

        tFieldOnBridge.setEditable(false);
        tFieldQueue.setEditable(false);
        textAreaNotifications.setEditable(false);
        textAreaNotifications.setFont(font);
        //Dodanie ActionListenera do opcji wyboru ograniczenia ruchu
        comboBoxLimit.addActionListener(event -> {
            JComboBox comboBoxLimit = (JComboBox) event.getSource();
            Object selected = comboBoxLimit.getSelectedItem();
            try {
                //Bez ograniczeń
                if (selected.equals(Limit.NO_LIMIT.toString())) {
                    System.out.println(" UWAGA!!! Wjazd na most bez ograniczen");
                    limitOption = 1;
                }
                //1 bus maksymalnie
                if (selected.equals(Limit.ONE_BUS.toString())) {
                    System.out.println(" UWAGA!!! Wjazd na most pojedynczo");
                    limitOption = 2;
                }
                //3 busy maksymalnie
                if (selected.equals(Limit.THREE_BUS.toString())) {
                    System.out.println(" UWAGA!!! Wjazd na most po trzy");
                    limitOption = 3;
                }
                //3 busy maksymlanie w 1 kierunku
                if (selected.equals(Limit.THREE_BUS_ONE_DIRECTION.toString())) {
                    System.out.println(" UWAGA!!! Wjazd na most po trzy w 1 kierunku");
                    limitOption = 4;
                }
            }
            catch (Exception e){
                JOptionPane.showMessageDialog(null, e.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        });

        comboBoxLimit.addItem(Limit.NO_LIMIT.toString());
        comboBoxLimit.addItem(Limit.THREE_BUS.toString());
        comboBoxLimit.addItem(Limit.THREE_BUS_ONE_DIRECTION.toString());
        comboBoxLimit.addItem(Limit.ONE_BUS.toString());

        //Wyświetlanie danych autora w menu
        author.addActionListener(event -> {
            Object source = event.getSource();
            try{
                if(source == author){
                    JOptionPane.showMessageDialog(this, AUTHOR);
                }
            }
            catch (Exception e){
                JOptionPane.showMessageDialog(this, e.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        });
        //Dodanie Changelistenera do suwaka natężenia ruchu
        sliderDensity.addChangeListener(change -> {
            JSlider source = (JSlider) change.getSource();
            if(source == sliderDensity){
                traffic = source.getValue();
            }
        });
        setContentPane(Panel);
        setSize(430, 800);
        setTitle("Symulacja przejazdu przez wąski most");
        setFont(font);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }

    //Metoda main
    public static void main(String[] args) {
        NarrowBridge bridge = new NarrowBridge();
        NarrowBridgeAnimation animation = new NarrowBridgeAnimation(bridge);

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

}
