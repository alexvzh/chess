package me.alex.main;

import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import me.alex.scene.SceneManager;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        FlatMacDarkLaf.setup();

        JFrame frame = new JFrame();

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setTitle("chess");

        frame.getContentPane().add(SceneManager.getInstance(frame).getCurrentScene());

        frame.pack();

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }
}