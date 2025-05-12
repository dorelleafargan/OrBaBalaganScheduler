package client.view;

import client.network.ClientSocketService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import data.Lead;
import model.LocalDateAdapter;
import model.Response;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ViewAllLeadsPanel extends JPanel {

    private final JTable table;
    private final DefaultTableModel tableModel;
    private final Gson gson;
    private final List<Lead> currentLeads;

    public ViewAllLeadsPanel() {
        setLayout(new BorderLayout());
        setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();

        currentLeads = new ArrayList<>();

        String[] columns = {"מס' לקוח", "שם", "טלפון", "משך", "כתובת", "עדיפות", "הערות", "תאריך יצירה", "שיבוץ"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        JScrollPane scrollPane = new JScrollPane(table);
        JButton refreshButton = new JButton("🔄 רענן");
        JButton editButton = new JButton("✏️ ערוך ליד");

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(refreshButton);
        bottomPanel.add(editButton);

        refreshButton.addActionListener(e -> loadLeads());
        editButton.addActionListener(e -> openEditDialog());

        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        loadLeads();
    }

    private void loadLeads() {
        try {
            ClientSocketService socketService = new ClientSocketService();
            Response response = socketService.sendRequest("lead/getAll", "");

            if (response.getStatusCode() != 200) {
                JOptionPane.showMessageDialog(this, "⚠️ שגיאה: " + response.getMessage());
                return;
            }

            Lead[] leads = gson.fromJson(response.getBody(), Lead[].class);
            tableModel.setRowCount(0);
            currentLeads.clear();

            for (Lead l : leads) {
                currentLeads.add(l);
                tableModel.addRow(new Object[]{
                        l.getClientNum(),
                        l.getId(),
                        l.getPhone(),
                        l.getDuration(),
                        l.getCustomer() != null ? l.getCustomer().getAddress() : "לא צוינה",
                        l.getPriority(),
                        l.getNotes(),
                        l.getCreationDate(),
                        l.getScheduledDate()
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "שגיאה בטעינה: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void openEditDialog() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "בחר ליד לעריכה.");
            return;
        }

        Lead selectedLead = currentLeads.get(selectedRow);
        new EditLeadDialog((Frame) SwingUtilities.getWindowAncestor(this), selectedLead);
        loadLeads(); // טען מחדש אחרי עריכה
    }
}
