package com.biblioteca;

import com.biblioteca.gui.TelaLogin;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Inicia a Interface Gráfica na thread correta do Swing
        SwingUtilities.invokeLater(() -> {
            TelaLogin login = new TelaLogin();
            login.setVisible(true);
        });
    }
}