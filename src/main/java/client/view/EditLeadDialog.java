package client.view;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import data.Lead;
import model.LocalDateAdapter;
import model.Response;
import client.network.ClientSocketService;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class EditLeadDialog extends JDialog {

    private final JTextField nameField;
    private final JTextField phoneField;
    private final JTextField durationDaysField;
    private final JTextField addressField;
    private final JTextField priorityField;
    private final JTextArea notesArea;
    private final JTextField dateField;

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create();

    public EditLeadDialog(Frame parent, Lead lead) {
        super(parent, "עריכת ליד", true);
        setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(7, 2, 10, 10));
        nameField = new JTextField(lead.getId());
        phoneField = new JTextField(lead.getPhone());
        durationDaysField = new JTextField(String.valueOf(lead.getDuration() / 1440));
        addressField = new JTextField(lead.getCustomer() != null ? lead.getCustomer().getAddress() : "");
        priorityField = new JTextField(String.valueOf(lead.getPriority()));
        notesArea = new JTextArea(lead.getNotes(), 3, 20);
        dateField = new JTextField(lead.getScheduledDate() != null ? lead.getScheduledDate().toString() : "");

        form.add(new JLabel("שם ושם משפחה:"));
        form.add(nameField);
        form.add(new JLabel("טלפון:"));
        form.add(phoneField);
        form.add(new JLabel("משך (בימים):"));
        form.add(durationDaysField);
        form.add(new JLabel("כתובת מלאה:"));
        form.add(addressField);
        form.add(new JLabel("איכות/עדיפות:"));
        form.add(priorityField);
        form.add(new JLabel("הערות:"));
        form.add(new JScrollPane(notesArea));
        form.add(new JLabel("תאריך שיבוץ (YYYY-MM-DD):"));
        form.add(dateField);

        JButton saveButton = new JButton("שמור");
        saveButton.addActionListener(e -> saveChanges(lead));

        add(form, BorderLayout.CENTER);
        add(saveButton, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    private void saveChanges(Lead lead) {
        try {
            int durationMinutes = Integer.parseInt(durationDaysField.getText().trim()) * 1440;
            String scheduledDate = dateField.getText().trim();

            Map<String, String> updateMap = new HashMap<>();
            updateMap.put("id", lead.getId());
            updateMap.put("name", nameField.getText().trim());
            updateMap.put("phone", phoneField.getText().trim());
            updateMap.put("duration", String.valueOf(durationMinutes));
            updateMap.put("priority", priorityField.getText().trim());
            updateMap.put("notes", notesArea.getText().trim());
            updateMap.put("address", addressField.getText().trim());
            updateMap.put("date", scheduledDate);

            String json = gson.toJson(updateMap);
            ClientSocketService socketService = new ClientSocketService();
            Response response = socketService.sendRequest("lead/updateAll", json);

            if (response.getStatusCode() == 200) {
                JOptionPane.showMessageDialog(this, "✅ הליד עודכן בהצלחה!");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "❌ שגיאה: " + response.getMessage());
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "שגיאה בשמירה: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
