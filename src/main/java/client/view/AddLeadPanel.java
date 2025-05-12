package client.view;

import client.network.ClientSocketService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import data.Lead;
import model.LocalDateAdapter;
import model.Response;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class AddLeadPanel extends JPanel {

    private final JTextField clientNumField;
    private final JTextField nameField;
    private final JTextField phoneField;
    private final JTextField durationDaysField;
    private final JTextField addressField;
    private final JTextField priorityField;
    private final JTextArea notesArea;
    private final JButton submitButton;

    public AddLeadPanel() {
        setLayout(new BorderLayout());
        setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        JPanel form = new JPanel(new GridLayout(7, 2, 10, 10));
        form.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        clientNumField = createTextField();
        nameField = createTextField();
        phoneField = createTextField();
        durationDaysField = createTextField();
        addressField = createTextField();
        priorityField = createTextField();
        notesArea = new JTextArea(3, 20);
        notesArea.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        notesArea.setLineWrap(true);
        notesArea.setWrapStyleWord(true);

        submitButton = new JButton("➕ הוסף ליד");

        form.add(createLabel("מס' לקוח:"));
        form.add(clientNumField);
        form.add(createLabel("שם ושם משפחה:"));
        form.add(nameField);
        form.add(createLabel("מס' פלאפון:"));
        form.add(phoneField);
        form.add(createLabel("משך (בימים):"));
        form.add(durationDaysField);
        form.add(createLabel("כתובת מלאה:"));
        form.add(addressField);
        form.add(createLabel("איכות/עדיפות:"));
        form.add(priorityField);
        form.add(createLabel("הערות:"));
        form.add(new JScrollPane(notesArea));

        add(form, BorderLayout.CENTER);
        add(submitButton, BorderLayout.SOUTH);

        submitButton.addActionListener(e -> sendLeadToServer());
    }

    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setHorizontalAlignment(SwingConstants.RIGHT);
        return field;
    }

    private JLabel createLabel(String text) {
        return new JLabel(text, SwingConstants.RIGHT);
    }

    private void sendLeadToServer() {
        try {
            int clientNum = Integer.parseInt(clientNumField.getText().trim());
            String name = nameField.getText().trim();
            String phone = phoneField.getText().trim();
            int durationDays = Integer.parseInt(durationDaysField.getText().trim());
            String address = addressField.getText().trim();
            int priority = Integer.parseInt(priorityField.getText().trim());
            String notes = notesArea.getText().trim();
            LocalDate creationDate = LocalDate.now();
            int durationMinutes = durationDays * 1440;

            Lead lead = new Lead(clientNum, name, phone, priority, creationDate);
            lead.setDuration(durationMinutes);
            lead.setNotes(notes + " | כתובת: " + address);

            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                    .create();

            String json = gson.toJson(lead);
            ClientSocketService socketService = new ClientSocketService();
            Response response = socketService.sendRequest("lead/add", json);

            if (response.getStatusCode() == 200) {
                JOptionPane.showMessageDialog(this, "✅ הליד נוסף בהצלחה!");
            } else {
                JOptionPane.showMessageDialog(this, "❌ שגיאה: " + response.getMessage());
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "שגיאה: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
