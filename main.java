import javax.swing.*;

/**
 * Joseph Nunez
 * CEN 3024C - Software Development 1
 * July 5, 2026
 * main.java
 * Runs the GUI
 */
public class main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CWUserInterface().setVisible(true));
    }
}