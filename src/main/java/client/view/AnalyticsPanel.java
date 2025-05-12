package client.view;

import client.network.ClientSocketService;
import model.Response;

import javax.swing.*;
import java.awt.*;

public class AnalyticsPanel extends JPanel {

    private final JTextArea outputArea;
    private final JButton refreshButton;

    public AnalyticsPanel() {
        setLayout(new BorderLayout());
        setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);


        outputArea = new JTextArea(15, 50);
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        outputArea.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        outputArea.setAlignmentX(RIGHT_ALIGNMENT);
        refreshButton = new JButton("🔄 רענן אנליטיקה");
        refreshButton.addActionListener(e -> loadAnalytics());

        add(new JScrollPane(outputArea), BorderLayout.CENTER);
        add(refreshButton, BorderLayout.SOUTH);

        loadAnalytics();
    }

    private void loadAnalytics() {
        try {
            ClientSocketService socketService = new ClientSocketService();
            Response response = socketService.sendRequest("lead/analytics", "");

            if (response.getStatusCode() == 200) {
                outputArea.setText(response.getBody());
            } else {
                outputArea.setText("⚠️ שגיאה: " + response.getMessage());
            }
        } catch (Exception e) {
            outputArea.setText("❌ שגיאה כללית: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
