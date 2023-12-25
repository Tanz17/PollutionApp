package ru.tanz;

public class Main{
    static Main instance;
    MainFrame mainFrame;

    public static void main(String[] args) {
        instance = new Main();
        instance.run();

    }
    private void run() {
        mainFrame = new MainFrame("That Program", 900, 1000);
    }
}
