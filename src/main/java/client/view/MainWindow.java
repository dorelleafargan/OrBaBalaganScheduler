package client.view;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {

    private final CardLayout cardLayout;
    private final JPanel contentPanel;

    public MainWindow() {
        super("📋 אור בבלגן - ניהול לידים");

        setLayout(new BorderLayout());
        setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        // סט לוגו בראש החלון
        setIconImage(new ImageIcon("assets/logo.png").getImage());

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        contentPanel.add(new AddLeadPanel(), "add");
        contentPanel.add(new ViewAllLeadsPanel(), "view");
        contentPanel.add(new AnalyticsPanel(), "analytics");

        JPanel menu = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        menu.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        JButton addButton = new JButton("➕ הוסף ליד");
        JButton viewButton = new JButton("📋 כל הלידים");
        JButton analyticsButton = new JButton("📈 אנליטיקה");

        addButton.addActionListener(e -> cardLayout.show(contentPanel, "add"));
        viewButton.addActionListener(e -> cardLayout.show(contentPanel, "view"));
        analyticsButton.addActionListener(e -> cardLayout.show(contentPanel, "analytics"));

        menu.add(addButton);
        menu.add(viewButton);
        menu.add(analyticsButton);

        // 👇 יצירת רקע עם התמונה החדשה
        JPanel backgroundPanel = new JPanel() {
            Image bg = new ImageIcon("assets/logo.jpg").getImage();
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
            }
        };

        backgroundPanel.setLayout(new BorderLayout());
        backgroundPanel.add(menu, BorderLayout.NORTH);
        backgroundPanel.add(contentPanel, BorderLayout.CENTER);

        setContentPane(backgroundPanel);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
