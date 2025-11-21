package View.CommonComponents;

import Controller.CourseController;
import Model.Course.Course;
import View.StyledComponents.StyleColors;

import javax.swing.*;
import java.awt.*;


public class CourseOverviewPanel extends JPanel {
    Course course;
    String instructorName;
    public CourseOverviewPanel(Course course) {
        this.course = course;
        this.instructorName = new CourseController().getInstructorName(course.getInstructorId());
        setLayout(new BorderLayout(0, 0));
        setBackground(StyleColors.BACKGROUND);
        // Add 10px margin outside the border, then the colored border, then padding inside
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(StyleColors.ACCENT, 2, true),
                        BorderFactory.createEmptyBorder(24, 24, 24, 24)
                )
        ));

        // Title label
        JLabel titleLabel = new JLabel(course.getTitle());
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(StyleColors.TEXT);
        titleLabel.setHorizontalAlignment(SwingConstants.LEFT);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Panel for title and instructor to control spacing
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(StyleColors.BACKGROUND);
        topPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        topPanel.add(titleLabel);

        JLabel instructorLabel = new JLabel();
        instructorLabel.setText("<html><b><i>Instructor: </i></b><i>" + instructorName + "</i></html>");
        instructorLabel.setFont(new Font("Segoe UI", Font.ITALIC | Font.BOLD, 15));
        instructorLabel.setForeground(StyleColors.TEXT);
        instructorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        // Reduce vertical space between title and instructor
        instructorLabel.setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
        topPanel.add(instructorLabel);

        add(topPanel, BorderLayout.NORTH);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(StyleColors.BACKGROUND);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(18, 0, 0, 0)); // Increase space between instructor and description

        JLabel descLabel = new JLabel("Description:");
        descLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        descLabel.setForeground(StyleColors.TEXT);
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        descLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        infoPanel.add(descLabel);

    JTextArea descriptionArea = new JTextArea(course.getDescription());
    descriptionArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    descriptionArea.setForeground(StyleColors.TEXT);
    descriptionArea.setBackground(StyleColors.BACKGROUND);
    descriptionArea.setLineWrap(true);
    descriptionArea.setWrapStyleWord(true);
    descriptionArea.setEditable(false);
    descriptionArea.setFocusable(false);
    descriptionArea.setBorder(BorderFactory.createEmptyBorder(4, 0, 0, 0));
    descriptionArea.setAlignmentX(Component.LEFT_ALIGNMENT);

    // Make the description scrollable
    JScrollPane scrollPane = new JScrollPane(descriptionArea);
    scrollPane.setBorder(null);
    scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
    scrollPane.setPreferredSize(new Dimension(0, 100)); // Adjust height as needed
    infoPanel.add(scrollPane);

        add(infoPanel, BorderLayout.CENTER);
    }
}
