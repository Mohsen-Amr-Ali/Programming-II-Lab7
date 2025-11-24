package View.CommonComponents;

import Model.Course.Lesson;
import Model.FileManager;
import View.StyledComponents.SLabel;
import View.StyledComponents.SScrollPane;
import View.StyledComponents.StyleColors;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class LessonViewPanel extends JPanel {
    // Set the parent path for all file/image operations
    private static final String parentPath = "C:\\Users\\malak\\Desktop\\Programming II\\Programming-II-Lab7\\src\\";

    public LessonViewPanel(Lesson lesson, int number) {
        setLayout(new BorderLayout(0, 0));
        setBackground(StyleColors.BACKGROUND);

        // Consistent Styling with padding around the whole panel
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(StyleColors.ACCENT, 2, true),
                        BorderFactory.createEmptyBorder(24, 24, 24, 24)
                )
        ));

        // Title and number
        SLabel titleLabel = new SLabel("Lesson " + number + ": " + lesson.getTitle());
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setHorizontalAlignment(SwingConstants.LEFT);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(StyleColors.BACKGROUND);

        // FIX: Add Padding (EmptyBorder) between the label and the content below
        topPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        topPanel.add(titleLabel);

        add(topPanel, BorderLayout.NORTH);

        // --- Resolve Content ---
        String displayContent;
        String contentPath = lesson.getContent();

        if (contentPath != null && !contentPath.isEmpty()) {
            // Construct the full, absolute path to the lesson file
            String fullPath = parentPath + "Database\\Assets\\" + contentPath;
            displayContent = FileManager.readBinaryToText(fullPath);
            if (displayContent == null) {
                // If reading fails, show an error message with the full path for debugging
                displayContent = "Error: Could not load lesson content from " + fullPath;
            }
        } else {
            // If content is empty or null, display a message
            displayContent = "No content available for this lesson.";
        }

        // Content area (scrollable)
        JTextArea contentArea = new JTextArea(displayContent);
        contentArea.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        contentArea.setForeground(StyleColors.TEXT);
        contentArea.setBackground(StyleColors.BACKGROUND);
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        contentArea.setEditable(false);
        contentArea.setFocusable(false);
        contentArea.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));
        contentArea.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Use SScrollPane
        SScrollPane scrollPane = new SScrollPane(contentArea);
        add(scrollPane, BorderLayout.CENTER);
    }
}