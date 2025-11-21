package View.InstructorComponents;

import View.StyledComponents.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class AddLessonPanel {
    protected JPanel rootPanel;
    protected SLabel titleLabel;
    protected STField titleTextField;
    protected SLabel contentLabel;
    protected JTextArea contentTextArea;
    protected SLabel positionLabel;
    protected STField positionTextField;
    protected SCheckBox addAtEndCheckBox;
    protected SBtn addButton;

    public AddLessonPanel() {
        // Theme Colors
        Color bg = new Color(25, 25, 35);
        Color card = new Color(40, 40, 60);
        Color text = new Color(220, 220, 235);
        Color accent = new Color(110, 90, 230);

        // Instantiation
        titleLabel = new SLabel("Lesson Title: ");
        titleTextField = new STField(20);
        contentLabel = new SLabel("Lesson Content: ");
        contentTextArea = new JTextArea(6, 20);
        contentTextArea.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        contentTextArea.setLineWrap(true);
        contentTextArea.setWrapStyleWord(true);
        contentTextArea.setBackground(card);
        contentTextArea.setForeground(text);
        contentTextArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(accent, 1, true),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));

        JScrollPane contentScroll = new JScrollPane(contentTextArea);
        contentScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        contentScroll.setBorder(null);
        contentScroll.getViewport().setBackground(card);

        positionLabel = new SLabel("Position: ");
        positionTextField = new STField(5);
        addAtEndCheckBox = new SCheckBox();
        addAtEndCheckBox.setText("Add at the end of the course");
        addButton = new SBtn("Add Lesson");

        // Root Panel
        rootPanel = new JPanel(new GridBagLayout());
        rootPanel.setBackground(bg);
        Border panelBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(accent, 2),
                "Add New Lesson",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 16),
                accent
        );
        rootPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(8, 10, 10, 10),
                BorderFactory.createCompoundBorder(panelBorder,
                        BorderFactory.createEmptyBorder(6, 8, 8, 8)
                ))
        );

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(7, 7, 7, 7);

        // Row 0: Title label
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        rootPanel.add(titleLabel, gbc);

        // Row 1: Title input (full width)
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        rootPanel.add(titleTextField, gbc);

        // Row 2: Content label
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        rootPanel.add(contentLabel, gbc);

        // Row 3: Content area (full width, max height)
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        rootPanel.add(contentScroll, gbc);

    // Row 4: Position label, field, and checkbox in a horizontal panel, centered
    JPanel posAndCheckPanel = new JPanel();
    posAndCheckPanel.setLayout(new BoxLayout(posAndCheckPanel, BoxLayout.X_AXIS));
    posAndCheckPanel.setOpaque(false);
    posAndCheckPanel.add(Box.createHorizontalGlue());
    posAndCheckPanel.add(positionLabel);
    posAndCheckPanel.add(Box.createHorizontalStrut(6));
    posAndCheckPanel.add(positionTextField);
    posAndCheckPanel.add(Box.createHorizontalStrut(18));
    posAndCheckPanel.add(addAtEndCheckBox);
    posAndCheckPanel.add(Box.createHorizontalGlue());

    gbc.gridx = 0; gbc.gridy = 4;
    gbc.gridwidth = 2;
    gbc.weightx = 1.0;
    gbc.weighty = 0;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.anchor = GridBagConstraints.CENTER;
    rootPanel.add(posAndCheckPanel, gbc);

    // Row 5: Add button
    gbc.gridx = 0; gbc.gridy = 5;
    gbc.gridwidth = 2;
    gbc.anchor = GridBagConstraints.CENTER;
    rootPanel.add(addButton, gbc);
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    public String getTitleText() {
        return titleTextField.getText();
    }
    public void setTitleText(String text) {
        titleTextField.setText(text);
    }
    public String getContentText() {
        return contentTextArea.getText();
    }
    public void setContentText(String text) {
        contentTextArea.setText(text);
    }
    public String getPositionText() {
        return positionTextField.getText();
    }
    public void setPositionText(String text) {
        positionTextField.setText(text);
    }
    public boolean isAddAtEndChecked() {
        return addAtEndCheckBox.isSelected();
    }
    public void setAddAtEndChecked(boolean checked) {
        addAtEndCheckBox.setSelected(checked);
    }
}