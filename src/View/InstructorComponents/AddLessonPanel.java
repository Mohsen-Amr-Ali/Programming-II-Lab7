package View.InstructorComponents;

import View.StyledComponents.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

public class AddLessonPanel {
    protected JPanel rootPanel;

    protected SLabel titleLabel;
    protected STField titleTextField;

    protected SLabel contentTypeLabel;
    protected JRadioButton typeTextRadio;
    protected JRadioButton typeFileRadio;
    protected ButtonGroup typeGroup;

    protected JPanel contentInputContainer;
    protected CardLayout contentCardLayout;
    protected JTextArea contentTextArea;
    protected JScrollPane contentScroll;
    protected JPanel fileUploadPanel;
    protected SBtn uploadFileBtn;
    protected JLabel selectedFileLabel;
    protected File selectedContentFile;

    protected SLabel positionLabel;
    protected SComboBox<Integer> positionComboBox;
    protected SCheckBox addAtEndCheckBox;
    protected SBtn addButton;

    public AddLessonPanel() {
        // --- Instantiation ---

        titleLabel = new SLabel("Lesson Title: ");
        titleTextField = new STField(20);

        contentTypeLabel = new SLabel("Content Type: ");
        typeTextRadio = new JRadioButton("Text");
        typeFileRadio = new JRadioButton("File Upload");
        styleRadioButton(typeTextRadio);
        styleRadioButton(typeFileRadio);
        typeTextRadio.setSelected(true);

        typeGroup = new ButtonGroup();
        typeGroup.add(typeTextRadio);
        typeGroup.add(typeFileRadio);

        // Content Cards
        contentCardLayout = new CardLayout();
        contentInputContainer = new JPanel(contentCardLayout);
        contentInputContainer.setOpaque(false);

        contentTextArea = new JTextArea(6, 20);
        contentTextArea.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        contentTextArea.setLineWrap(true);
        contentTextArea.setWrapStyleWord(true);
        contentTextArea.setBackground(StyleColors.CARD);
        contentTextArea.setForeground(StyleColors.TEXT);
        contentTextArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(StyleColors.ACCENT, 1, true),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        contentScroll = new SScrollPane(contentTextArea);
        contentScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        contentScroll.setBorder(null);

        fileUploadPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        fileUploadPanel.setOpaque(false);
        uploadFileBtn = new SBtn("Select Text File");
        selectedFileLabel = new SLabel("No file selected");
        selectedFileLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        selectedFileLabel.setForeground(StyleColors.TEXT.darker());
        selectedFileLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        fileUploadPanel.add(uploadFileBtn);
        fileUploadPanel.add(selectedFileLabel);

        contentInputContainer.add(contentScroll, "TEXT");
        contentInputContainer.add(fileUploadPanel, "FILE");

        typeTextRadio.addActionListener(e -> contentCardLayout.show(contentInputContainer, "TEXT"));
        typeFileRadio.addActionListener(e -> contentCardLayout.show(contentInputContainer, "FILE"));

        uploadFileBtn.addActionListener(e -> chooseFile());

        // Position Section
        positionLabel = new SLabel("Position: ");
        positionComboBox = new SComboBox<>();
        positionComboBox.addItem(1);

        // FIX: Set specific disabled color so it's visible
        // This overrides the default "grayed out" look that makes it invisible on dark backgrounds
        UIManager.put("ComboBox.disabledForeground", StyleColors.TEXT.darker());
        positionComboBox.setEnabled(false);

        addAtEndCheckBox = new SCheckBox();
        addAtEndCheckBox.setText("Add at the end of the course");
        addAtEndCheckBox.setSelected(true);

        addAtEndCheckBox.addActionListener(e -> {
            positionComboBox.setEnabled(!addAtEndCheckBox.isSelected());
        });

        addButton = new SBtn("Add Lesson");

        // --- Root Panel Layout ---
        rootPanel = new JPanel(new GridBagLayout());
        rootPanel.setBackground(StyleColors.BACKGROUND);
        Border panelBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(StyleColors.ACCENT, 2),
                "Add New Lesson",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 16),
                StyleColors.ACCENT
        );
        rootPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(8, 10, 10, 10),
                BorderFactory.createCompoundBorder(panelBorder,
                        BorderFactory.createEmptyBorder(6, 8, 8, 8)
                ))
        );

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(7, 7, 7, 7);

        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        rootPanel.add(titleLabel, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        rootPanel.add(titleTextField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JPanel typePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        typePanel.setOpaque(false);
        typePanel.add(contentTypeLabel);
        typePanel.add(Box.createHorizontalStrut(10));
        typePanel.add(typeTextRadio);
        typePanel.add(Box.createHorizontalStrut(10));
        typePanel.add(typeFileRadio);
        rootPanel.add(typePanel, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        rootPanel.add(contentInputContainer, gbc);

        JPanel posPanel = new JPanel();
        posPanel.setLayout(new BoxLayout(posPanel, BoxLayout.X_AXIS));
        posPanel.setOpaque(false);
        posPanel.add(Box.createHorizontalGlue());
        posPanel.add(positionLabel);
        posPanel.add(Box.createHorizontalStrut(10));
        posPanel.add(positionComboBox);
        posPanel.add(Box.createHorizontalStrut(20));
        posPanel.add(addAtEndCheckBox);
        posPanel.add(Box.createHorizontalGlue());

        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        rootPanel.add(posPanel, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        rootPanel.add(addButton, gbc);
    }

    private void styleRadioButton(JRadioButton rb) {
        rb.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        rb.setForeground(StyleColors.TEXT);
        rb.setBackground(StyleColors.BACKGROUND);
        rb.setFocusPainted(false);
        rb.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private void chooseFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Content File");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files", "txt", "md", "json"));

        int result = fileChooser.showOpenDialog(rootPanel);
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedContentFile = fileChooser.getSelectedFile();
            selectedFileLabel.setText(selectedContentFile.getName());
            selectedFileLabel.setForeground(StyleColors.TEXT);
        }
    }

    public void setLessonCount(int currentLessonCount) {
        positionComboBox.removeAllItems();
        for (int i = 1; i <= currentLessonCount + 1; i++) {
            positionComboBox.addItem(i);
        }
        if (positionComboBox.getItemCount() > 0) {
            positionComboBox.setSelectedIndex(positionComboBox.getItemCount() - 1);
        }
    }

    public JPanel getRootPanel() { return rootPanel; }
    public String getTitleText() { return titleTextField.getText(); }
    public void setTitleText(String text) { titleTextField.setText(text); }
    public boolean isFileMode() { return typeFileRadio.isSelected(); }
    public String getContentText() { return contentTextArea.getText(); }
    public void setContentText(String text) { contentTextArea.setText(text); }
    public File getSelectedContentFile() { return selectedContentFile; }
    public int getSelectedPosition() {
        if (addAtEndCheckBox.isSelected()) {
            return -1;
        }
        Integer pos = (Integer) positionComboBox.getSelectedItem();
        return (pos != null) ? pos - 1 : -1;
    }
    public boolean isAddAtEndChecked() { return addAtEndCheckBox.isSelected(); }
    public void setAddAtEndChecked(boolean checked) {
        addAtEndCheckBox.setSelected(checked);
        positionComboBox.setEnabled(!checked);
    }
    public SBtn getAddButton() { return addButton; }
}