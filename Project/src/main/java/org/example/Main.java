package org.example;

import org.example.ui.MainFrame;
import org.example.util.DbUtil;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        DbUtil.initDatabase();
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
