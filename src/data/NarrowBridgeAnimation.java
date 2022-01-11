package data;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

public class NarrowBridgeAnimation extends JPanel {
    NarrowBridge bridge;

    NarrowBridgeAnimation(NarrowBridge bridge){
        this.bridge = bridge;
        JFrame frame = new JFrame();
        frame.setTitle("Animacja przejazdu przez most");
        frame.setSize(600, 700);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setContentPane(this);
        frame.setVisible(true);
        new Thread(() -> {
            while (true){
                repaint();
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                }
            }
        }).start();
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        int width = getWidth()/7;
        int height = getHeight()/4;
        this.setBackground(Color.black);
        Font font = new Font("MonoSpaced", Font.BOLD, 30);

        Color darkGreen = new Color(34,139,34);

        g2d.setColor(darkGreen);
        g.fillRect(getX(), getY(), width, getHeight());
        g2d.setColor(Color.red);
        g.fillRect(getX() + width+1, getY(), width, getHeight());
        g2d.setColor(Color.blue);
        g.fillRect(getX() + 2*(width+1), getY(), width, getHeight());
        g2d.setColor(Color.darkGray);
        g.fillRect(getX() + 3*(width+1), getY(), width, getHeight());
        g2d.setColor(Color.blue);
        g.fillRect(getX() + 4*(width+1), getY(), width, getHeight());
        g2d.setColor(Color.red);
        g.fillRect(getX() + 5*(width+1), getY(), width, getHeight());
        g2d.setColor(darkGreen);
        g.fillRect(getX() + 6*(width+1), getY(), width, getHeight());

        AffineTransform affineTransform = new AffineTransform();
        affineTransform.rotate(Math.toRadians(90), 0, 0);
        Font rotatedFont = font.deriveFont(affineTransform);
        g2d.setFont(rotatedFont);
        g2d.setColor(Color.gray);
        g2d.drawString("P A R K I N G" , getX() + width - 50, height);
        g2d.drawString("R O A D" , getX() + 2*(getX() + width) - 50, height);
        g2d.drawString("G A T E" , getX() + 3*(getX() + width) - 50, height);
        g2d.drawString("B R I D G E" , getX() + 4*(getX() + width) - 50, height);
        g2d.drawString("G A T E" , getX() + 5*(getX() + width) - 50, height);
        g2d.drawString("R O A D" , getX() + 6*(getX() + width) - 50, height);
        g2d.drawString("P A R K I N G" , getX() + 7*(getX() + width) - 50, height);
        g2d.dispose();
    }
}
