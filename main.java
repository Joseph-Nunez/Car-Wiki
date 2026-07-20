import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

/**
 * Joseph Nunez
 * CEN 3024C - Software Development 1
 * main.java
 * Entry point for the GUI, collects the MySQL server address, username,
 * and password from the user at startup
 */
public class main {

    // Prompts for MySQL server address, username, and password until a working
    // connection is made, or the user cancels.
    private static CarDatabase promptForDatabase() {
        JTextField hostField = new JTextField(); // gathers server address
        JTextField usernameField = new JTextField(); // gathers SQL username
        JPasswordField passwordField = new JPasswordField(); // gathers SQL password

        JPanel panel = new JPanel(new GridLayout(3, 2, 8, 8));
        panel.add(new JLabel("Server address:"));
        panel.add(hostField);
        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);

        while (true) {
            int result = JOptionPane.showConfirmDialog(null, panel, "Connect to Car Wiki Database",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result != JOptionPane.OK_OPTION) {
                return null; // user cancelled
            }

            String host = hostField.getText().trim();
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());

            try {
                return new CarDatabase(host, username, password);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, // catches incorrect credentials error
                        "Could not connect: " + e.getMessage() + "\nPlease check your server address, username, and password.",
                        "Connection Failed", JOptionPane.ERROR_MESSAGE);
                // loop back and let them try again
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CarDatabase database = promptForDatabase();
            if (database == null) {
                System.exit(0); // user cancelled the connection prompt
            }
            new CWUserInterface(database).setVisible(true);
        });
    }
}