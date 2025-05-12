package client;

import javax.swing.SwingUtilities;
import client.view.MainWindow;

public class ClientApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainWindow());
    }
}
