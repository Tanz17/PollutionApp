package ru.tanz;

import javax.swing.*;
import java.awt.*;

public class Main{
    static Main instance;
    MainFrame mainFrame;
    JFrame frame;

    public static void main(String[] args) {
        instance = new Main();
        instance.run();

    }
    private void run() {
        SwingUtilities.invokeLater(this::initializePane);
    }
    private void initializePane(){
        try {
            UIManager.setLookAndFeel("com.formdev.flatlaf.FlatDarkLaf");
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }
        frame = new JFrame("That Program");
        mainFrame = new MainFrame(frame);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(mainFrame);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);

    }
}
