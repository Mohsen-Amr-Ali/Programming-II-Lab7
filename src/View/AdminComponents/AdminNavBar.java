package View.AdminComponents;

import Model.User.Admin;
import View.StyledComponents.*;

import javax.swing.*;
import java.awt.*;

public class AdminNavBar extends JPanel {
    private Admin admin;
    private SBtn logoutBtn;
    private STField searchField;
    private SBtn searchBtn;
    private SBtn refreshBtn;
    private SBtn dashboardBtn;
    private SBtn analyticsBtn;

    public AdminNavBar(Admin admin) {
        this.admin = admin;
        setLayout(new BorderLayout(0, 8));
        setBackground(StyleColors.BACKGROUND);

        // Consistent styling with other NavBars
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(15, 17, 17, 17),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(StyleColors.ACCENT, 2, true),
                        BorderFactory.createEmptyBorder(15, 15, 15, 15)
                )
        ));

        // Upper section: Welcome message and logout button
        JPanel upperSection = createUpperSection();
        add(upperSection, BorderLayout.NORTH);

        // Lower section: Navigation and Search
        JPanel lowerSection = createLowerSection();
        add(lowerSection, BorderLayout.CENTER);
    }

    private JPanel createUpperSection() {
        JPanel upperPanel = new JPanel(new GridBagLayout());
        upperPanel.setBackground(StyleColors.BACKGROUND);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 0, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 0;

        // Welcome label
        JLabel welcomeLabel = new JLabel("Welcome, " + admin.getUsername());
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        welcomeLabel.setForeground(StyleColors.TEXT);
        gbc.gridx = 0;
        gbc.gridy = 0;
        upperPanel.add(welcomeLabel, gbc);

        // Role label
        JLabel roleLabel = new JLabel("Administrator");
        roleLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        roleLabel.setForeground(StyleColors.ACCENT); // Highlight Admin role
        gbc.gridx = 1;
        gbc.gridy = 0;
        upperPanel.add(roleLabel, gbc);

        // Spacer
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        upperPanel.add(Box.createHorizontalGlue(), gbc);

        // Logout button
        logoutBtn = new SBtn("Logout");
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        upperPanel.add(logoutBtn, gbc);

        return upperPanel;
    }

    private JPanel createLowerSection() {
        JPanel lowerPanel = new JPanel(new GridBagLayout());
        lowerPanel.setBackground(StyleColors.BACKGROUND);
        lowerPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;

        // Navigation Buttons
        dashboardBtn = new SBtn("Dashboard");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        lowerPanel.add(dashboardBtn, gbc);

        analyticsBtn = new SBtn("Platform Analytics");
        gbc.gridx = 1;
        gbc.gridy = 0;
        lowerPanel.add(analyticsBtn, gbc);

        // Search Field (Takes up remaining space)
        searchField = new STField("Search courses...");
        searchField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (searchField.getText().equals("Search courses...")) {
                    searchField.setText("");
                }
            }
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText("Search courses...");
                }
            }
        });

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        lowerPanel.add(searchField, gbc);

        // Search Button
        searchBtn = new SBtn("Search");
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.weightx = 0;
        lowerPanel.add(searchBtn, gbc);

        // Refresh Button
        refreshBtn = new SBtn("Refresh");
        gbc.gridx = 4;
        gbc.gridy = 0;
        lowerPanel.add(refreshBtn, gbc);

        return lowerPanel;
    }

    public SBtn getLogoutBtn() { return logoutBtn; }
    public SBtn getSearchBtn() { return searchBtn; }
    public SBtn getRefreshBtn() { return refreshBtn; }
    public SBtn getDashboardBtn() { return dashboardBtn; }
    public SBtn getAnalyticsBtn() { return analyticsBtn; }
    public String getSearchText() { return searchField.getText(); }

    public void addSearchListener(java.awt.event.ActionListener l) {
        searchBtn.addActionListener(l);
    }

    public void addRefreshListener(java.awt.event.ActionListener l) {
        refreshBtn.addActionListener(l);
    }

    public void clearSearch() {
        searchField.setText("Search courses...");
    }
}