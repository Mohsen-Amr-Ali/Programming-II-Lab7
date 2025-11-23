package View.CommonComponents;

import Controller.CourseController;
import Model.Course.Course;
import Model.Course.COURSE_STATUS;
import View.StyledComponents.SCoursePanel;
import View.StyledComponents.StyleColors;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class CourseCard extends SCoursePanel {
    private JLabel imageLabel;
    private JLabel titleLabel;
    private JLabel instructorLabel;
    private JLabel progressLabel;
    private JLabel percentLabel;
    private JLabel statusLabel;

    private static final int IMG_WIDTH = 150;
    private static final int IMG_HEIGHT = 100;

    // Basic constructor: title and instructor
    public CourseCard(Course course) {
        initCard(course);
    }

    // Constructor with Status (For Admin/Instructor)
    public CourseCard(Course course, COURSE_STATUS status) {
        initCard(course);
        addStatusLabel(status);
    }

    // Overloaded constructor: adds progress info
    public CourseCard(Course course, int numOfCompletedLessons, int totalLessons) {
        this(course);

        JPanel infoPanel = (JPanel) ((BorderLayout)getLayout()).getLayoutComponent(BorderLayout.CENTER);

        if (totalLessons > 0) {
            int percent = (int) Math.round((numOfCompletedLessons * 100.0) / totalLessons);
            progressLabel = new JLabel(numOfCompletedLessons + " out of " + totalLessons + " Completed");
            percentLabel = new JLabel("Progress: " + percent + "%");
        } else {
            progressLabel = new JLabel("No lessons");
            percentLabel = new JLabel("Progress: 0%");
        }

        progressLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        progressLabel.setForeground(StyleColors.TEXT);
        progressLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        progressLabel.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));
        infoPanel.add(progressLabel);

        percentLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        percentLabel.setForeground(StyleColors.TEXT);
        percentLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        percentLabel.setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
        infoPanel.add(percentLabel);
    }

    // Third constructor for Instructor view (Edit mode dummy - legacy)
    public CourseCard(Course course, String actionText) {
        this(course);
    }

    private void initCard(Course course) {
        String instructorName = new CourseController().getInstructorName(course.getInstructorId());
        setLayout(new BorderLayout(15, 0));
        setBackground(StyleColors.BACKGROUND);

        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(StyleColors.ACCENT, 2, true),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));

        // --- Image Section ---
        imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(IMG_WIDTH, IMG_HEIGHT));
        imageLabel.setMinimumSize(new Dimension(IMG_WIDTH, IMG_HEIGHT));
        imageLabel.setMaximumSize(new Dimension(IMG_WIDTH, IMG_HEIGHT));
        imageLabel.setBorder(BorderFactory.createLineBorder(StyleColors.ACCENT_DARK, 1));

        String imagePath = "Database/Assets/Default_Img.png";
        if (course.getImagePath() != null && !course.getImagePath().isEmpty()) {
            imagePath = course.getImagePath();
        }

        ImageIcon icon = loadResizedIcon(imagePath, IMG_WIDTH, IMG_HEIGHT);
        if (icon == null) {
            icon = loadResizedIcon("Database/Assets/Default_Img.png", IMG_WIDTH, IMG_HEIGHT);
        }

        if (icon != null) {
            imageLabel.setIcon(icon);
        } else {
            imageLabel.setText("No Image");
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            imageLabel.setForeground(StyleColors.TEXT);
        }

        add(imageLabel, BorderLayout.WEST);

        // --- Info Section ---
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(StyleColors.BACKGROUND);
        infoPanel.setOpaque(false);

        titleLabel = new JLabel(course.getTitle());
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(StyleColors.TEXT);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(titleLabel);

        instructorLabel = new JLabel("<html><b><i>Instructor: </i></b><i>" + instructorName + "</i></html>");
        instructorLabel.setFont(new Font("Segoe UI", Font.ITALIC | Font.PLAIN, 14));
        instructorLabel.setForeground(StyleColors.TEXT);
        instructorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        instructorLabel.setBorder(BorderFactory.createEmptyBorder(4, 0, 0, 0));
        infoPanel.add(instructorLabel);

        add(infoPanel, BorderLayout.CENTER);
    }

    private void addStatusLabel(COURSE_STATUS status) {
        Color statusColor;
        String statusText;

        switch (status) {
            case APPROVED:
                statusColor = new Color(40, 167, 69); // Green
                statusText = "APPROVED";
                break;
            case REJECTED:
                statusColor = new Color(220, 53, 69); // Red
                statusText = "REJECTED";
                break;
            default: // PENDING
                statusColor = new Color(255, 193, 7); // Amber/Yellow
                statusText = "PENDING";
                break;
        }

        statusLabel = new JLabel(statusText);
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setOpaque(true);
        statusLabel.setBackground(statusColor);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));

        // Create a panel to hold the label at the top-right or similar
        // Since we are using BorderLayout, EAST is a good spot
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        statusPanel.setOpaque(false);
        statusPanel.add(statusLabel);

        add(statusPanel, BorderLayout.EAST);
    }

    private ImageIcon loadResizedIcon(String path, int w, int h) {
        try {
            File file = new File(path);
            if (!file.exists()) {
                file = new File("src/" + path);
                if(!file.exists()) return null;
            }
            BufferedImage img = ImageIO.read(file);
            Image scaled = img.getScaledInstance(w, h, Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);
        } catch (Exception e) {
            return null;
        }
    }
}