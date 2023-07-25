package org.example;

import java.awt.Component;
import javax.swing.JFrame;

public class Main {
    public Main() {
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setSize(700, 700);
        frame.setDefaultCloseOperation(3);
        frame.setLocationRelativeTo((Component)null);
        frame.setResizable(false);
        frame.setTitle("Paint IO");
        new Game(frame);
    }
}
