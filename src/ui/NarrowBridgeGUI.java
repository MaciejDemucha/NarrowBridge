package ui;

import data.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NarrowBridgeGUI extends  JFrame implements ActionListener, ChangeListener {
    private static final String AUTHOR =
            "Języki programowania - Laboratorium 5\n" +
                    "Program: Symulacja przejazdu przez most\n" +
                    "Autor: Maciej Demucha 259111\n" +
                    "Data: 14 stycznia 2022 r.\n";

    private static int traffic = 1000;
    NarrowBridge bridge;

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

        NarrowBridgeGUI(NarrowBridge bridge){
        this.bridge = bridge;
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
        comboBoxLimit.addActionListener(event -> {
            JComboBox comboBoxLimit = (JComboBox) event.getSource();
            Object selected = comboBoxLimit.getSelectedItem();
            try {
                if (selected.equals(Limit.NO_LIMIT.toString())) {
                    System.out.println(" UWAGA!!! Wjazd na most bez ograniczen");
                    this.bridge.setLimitOption(1);
                }
                if (selected.equals(Limit.ONE_BUS.toString())) {
                    System.out.println(" UWAGA!!! Wjazd na most pojedynczo");
                    this.bridge.setLimitOption(2);
                }
                if (selected.equals(Limit.THREE_BUS.toString())) {
                    System.out.println(" UWAGA!!! Wjazd na most po trzy");
                    this.bridge.setLimitOption(3);
                }
                if (selected.equals(Limit.THREE_BUS_ONE_DIRECTION.toString())) {
                    System.out.println(" UWAGA!!! Wjazd na most po trzy w 1 kierunku");
                    this.bridge.setLimitOption(4);
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
        sliderDensity.addChangeListener(this);
        setContentPane(Panel);
        setSize(430, 800);
        setTitle("Symulacja przejazdu przez wąski most");
        setFont(font);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }

    public static void main(String[] args) {
        NarrowBridge bridge = new NarrowBridge();
        NarrowBridgeGUI frame = new NarrowBridgeGUI(bridge);

        // Zadaniem tej pętli jest tworzenie kolejnych busów,
        // które mają przewozić przez most pasażerów:
        // Przerwy pomiędzy kolejnymi busami są generowane losowo.
        while (true) {
            StringBuilder waiting = new StringBuilder();
            for (Bus busWaiting: bridge.busesWaiting) {
                waiting.append(busWaiting.getId());
                waiting.append(", ");
            }

            StringBuilder onBridge = new StringBuilder();
            for (Bus busOnBridge: bridge.busesOnTheBridge) {
                onBridge.append(busOnBridge.getId());
                onBridge.append(", ");
            }

            frame.tFieldOnBridge.setText(onBridge.toString());
            frame.tFieldQueue.setText(waiting.toString());

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

    @Override
    public void actionPerformed(ActionEvent event) {

    }

    @Override
    public void stateChanged(ChangeEvent change) {
        JSlider source = (JSlider) change.getSource();
        if(source == sliderDensity){
            traffic = source.getValue();
        }
    }
}
