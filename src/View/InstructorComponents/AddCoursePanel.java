package View.InstructorComponents;

import View.StyledComponents.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

public class AddCoursePanel {
    private JPanel rootPanel;

    private SLabel titleLabel;
    private STField titleTextField;

    private SLabel descLabel;
    private JTextArea descTextArea;

    private SLabel imageLabel;
    private SBtn uploadImageBtn;
    private JLabel selectedFileLabel; // Shows the name of the selected file
    private File selectedImageFile;   // The actual file object

    private SBtn addButton;

    public AddCoursePanel() {
        // --- Instantiation ---

        // Title
        titleLabel = new SLabel("Course Title: ");
        titleTextField = new STField(20);

        // Description
        descLabel = new SLabel("Course Description: ");
        descTextArea = new JTextArea(6, 20);
        descTextArea.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        descTextArea.setLineWrap(true);
        descTextArea.setWrapStyleWord(true);
        descTextArea.setBackground(StyleColors.CARD);
        descTextArea.setForeground(StyleColors.TEXT);
        descTextArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(StyleColors.ACCENT, 1, true),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));

        SScrollPane descScroll = new SScrollPane(descTextArea);
        descScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        descScroll.setBorder(null);

        // Image Upload
        imageLabel = new SLabel("Course Image: ");
        uploadImageBtn = new SBtn("Upload Image");
        selectedFileLabel = new SLabel("No file selected");
        selectedFileLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        selectedFileLabel.setForeground(StyleColors.TEXT.darker());

        uploadImageBtn.addActionListener(e -> chooseImage());

        // Add Button
        addButton = new SBtn("Add Course");

        // --- Root Panel Layout ---
        rootPanel = new JPanel(new GridBagLayout());
        rootPanel.setBackground(StyleColors.BACKGROUND);
        Border panelBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(StyleColors.ACCENT, 2),
                "Add New Course",
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

        // Row 0: Title label
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        rootPanel.add(titleLabel, gbc);

        // Row 1: Title input
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        rootPanel.add(titleTextField, gbc);

        // Row 2: Description label
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        rootPanel.add(descLabel, gbc);

        // Row 3: Description area
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        rootPanel.add(descScroll, gbc);

        // Row 4: Image Upload Section
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        rootPanel.add(imageLabel, gbc);

        // Panel for button and label
        JPanel imageUploadPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        imageUploadPanel.setOpaque(false);
        imageUploadPanel.add(uploadImageBtn);
        imageUploadPanel.add(selectedFileLabel);

        gbc.gridx = 0; gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        rootPanel.add(imageUploadPanel, gbc);

        // Row 6: Add button
        gbc.gridx = 0; gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        rootPanel.add(addButton, gbc);
    }

    private void chooseImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Course Image");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        // Filter for images
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpg", "png", "jpeg", "gif");
        fileChooser.setFileFilter(filter);

        int result = fileChooser.showOpenDialog(rootPanel);
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedImageFile = fileChooser.getSelectedFile();
            selectedFileLabel.setText(selectedImageFile.getName());
            selectedFileLabel.setForeground(StyleColors.TEXT);
        }
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

    public String getDescriptionText() {
        return descTextArea.getText();
    }

    public void setDescriptionText(String text) {
        descTextArea.setText(text);
    }

    public File getSelectedImageFile() {
        return selectedImageFile;
    }

    public SBtn getAddButton() {
        return addButton;
    }
}