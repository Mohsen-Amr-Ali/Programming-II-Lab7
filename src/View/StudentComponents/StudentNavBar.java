package View.StudentComponents;

import Model.User.Student;
import View.StyledComponents.*;

import javax.swing.*;
import java.awt.*;

public class StudentNavBar extends JPanel {
    private Student student;
    private SBtn enrolledCoursesBtn;
    private SBtn availableCoursesBtn;
    private SBtn myStatsBtn; // New Button
    private STField searchField;
    private SBtn searchBtn;
    private SBtn logoutBtn;

    public StudentNavBar(Student student) {
        this.student = student;
        setLayout(new BorderLayout(0, 8));
        setBackground(StyleColors.BACKGROUND);

        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(15, 17, 17, 17),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(StyleColors.ACCENT, 2, true),
                        BorderFactory.createEmptyBorder(15, 15, 15, 15)
                )
        ));

        JPanel upperSection = createUpperSection();
        add(upperSection, BorderLayout.NORTH);

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

        JLabel welcomeLabel = new JLabel("Welcome, " + student.getUsername());
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        welcomeLabel.setForeground(StyleColors.TEXT);
        gbc.gridx = 0;
        gbc.gridy = 0;
        upperPanel.add(welcomeLabel, gbc);

        JLabel roleLabel = new JLabel("Student");
        roleLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        roleLabel.setForeground(StyleColors.TEXT.darker());
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 0, 0);
        upperPanel.add(roleLabel, gbc);

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        upperPanel.add(Box.createHorizontalGlue(), gbc);

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
        gbc.weightx = 0;
        gbc.weighty = 0;

        // 1. Enrolled Courses
        enrolledCoursesBtn = new SBtn("Enrolled Courses");
        gbc.gridx = 0;
        gbc.gridy = 0;
        lowerPanel.add(enrolledCoursesBtn, gbc);

        // 2. Available Courses
        availableCoursesBtn = new SBtn("Available Courses");
        gbc.gridx = 1;
        gbc.gridy = 0;
        lowerPanel.add(availableCoursesBtn, gbc);

        // 3. My Stats (New)
        myStatsBtn = new SBtn("My Stats");
        gbc.gridx = 2;
        gbc.gridy = 0;
        lowerPanel.add(myStatsBtn, gbc);

        // 4. Search Field (Spans remaining width)
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

        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.weightx = 1.0; // Takes up remaining space
        lowerPanel.add(searchField, gbc);

        // 5. Search Button
        searchBtn = new SBtn("Search");
        gbc.gridx = 4;
        gbc.gridy = 0;
        gbc.weightx = 0;
        lowerPanel.add(searchBtn, gbc);

        return lowerPanel;
    }

    // Getter methods and listeners
    public SBtn getEnrolledCoursesBtn() { return enrolledCoursesBtn; }
    public SBtn getAvailableCoursesBtn() { return availableCoursesBtn; }
    public SBtn getMyStatsBtn() { return myStatsBtn; }
    public STField getSearchField() { return searchField; }
    public SBtn getSearchBtn() { return searchBtn; }
    public SBtn getLogoutBtn() { return logoutBtn; }

    public void addEnrolledCoursesButtonListener(java.awt.event.ActionListener listener) {
        enrolledCoursesBtn.addActionListener(listener);
    }

    public void addAvailableCoursesButtonListener(java.awt.event.ActionListener listener) {
        availableCoursesBtn.addActionListener(listener);
    }

    public void addMyStatsButtonListener(java.awt.event.ActionListener listener) {
        myStatsBtn.addActionListener(listener);
    }

    public void addSearchButtonListener(java.awt.event.ActionListener listener) {
        searchBtn.addActionListener(listener);
    }

    public String getSearchText() {
        String text = searchField.getText();
        if (text.equals("Search courses...")) {
            return "";
        }
        return text;
    }

    public void clearSearchText() {
        searchField.setText("Search courses...");
    }

    public Student getStudent() {
        return student;
    }
}