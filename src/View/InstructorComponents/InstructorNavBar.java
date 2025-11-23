package View.InstructorComponents;

import Model.User.Instructor;
import View.StyledComponents.*;

import javax.swing.*;
import java.awt.*;

public class InstructorNavBar extends JPanel {
    private Instructor instructor;
    private STField searchField;
    private SBtn searchBtn;
    private SBtn refreshButton;
    private SBtn logoutBtn;

    public InstructorNavBar(Instructor instructor) {
        this.instructor = instructor;
        setLayout(new BorderLayout(0, 8));
        setBackground(StyleColors.BACKGROUND);

        // Matches StudentNavBar Styling exactly
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

        // Lower section: Search field and search button only
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

        // Welcome label with instructor name
        JLabel welcomeLabel = new JLabel("Welcome, " + instructor.getUsername());
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        welcomeLabel.setForeground(StyleColors.TEXT);
        gbc.gridx = 0;
        gbc.gridy = 0;
        upperPanel.add(welcomeLabel, gbc);

        // Instructor role label (smaller, italic)
        JLabel roleLabel = new JLabel("Instructor");
        roleLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        roleLabel.setForeground(StyleColors.TEXT.darker());
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 0, 0);
        upperPanel.add(roleLabel, gbc);

        // Spacer to push logout button to the right
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
        gbc.weightx = 1.0;
        gbc.weighty = 0;

        // Search field (spans most of the width)
        searchField = new STField("Search courses...");
        // Add focus listener to clear placeholder on focus
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
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        lowerPanel.add(searchField, gbc);

        // Search button (to the right of search field)
        searchBtn = new SBtn("Search");
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.VERTICAL;
        lowerPanel.add(searchBtn, gbc);

        // Refresh button
        refreshButton = new SBtn("Refresh");
        gbc.gridx = 2;
        gbc.gridy = 0;
        lowerPanel.add(refreshButton, gbc);

        return lowerPanel;
    }

    // Getter methods for buttons and search field to add action listeners
    public STField getSearchField() {
        return searchField;
    }

    public SBtn getSearchBtn() {
        return searchBtn;
    }

    public SBtn getRefreshBtn() {
        return refreshButton;
    }

    public SBtn getLogoutBtn() {
        return logoutBtn;
    }

    public Instructor getInstructor() {
        return instructor;
    }

    public String getSearchText() {
        return searchField.getText();
    }

    public void addSearchButtonListener(java.awt.event.ActionListener listener) {
        searchBtn.addActionListener(listener);
    }

    public void addRefreshButtonListener(java.awt.event.ActionListener listener) {
        refreshButton.addActionListener(listener);
    }

    public void clearSearchText() {
        searchField.setText("Search courses...");
    }
}