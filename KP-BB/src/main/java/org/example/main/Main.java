package org.example.main;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame window = new JFrame();
        GamePanel WindSett = new GamePanel();
        window.add(WindSett);
        window.pack();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        window.setLocationRelativeTo(null);
        window.setVisible(true);
        WindSett.StartGame();
    }
}