package View.CommonComponents;

import Controller.CourseController;
import Model.Course.Course;
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

    // Fixed size for the course thumbnail
    private static final int IMG_WIDTH = 120;
    private static final int IMG_HEIGHT = 90;

    // Basic constructor: title and instructor
    public CourseCard(Course course) {
        String instructorName = new CourseController().getInstructorName(course.getInstructorId());
        setLayout(new BorderLayout(15, 0)); // Gap between image and text
        setBackground(StyleColors.BACKGROUND);
        // Outer border + padding
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(StyleColors.ACCENT, 2, true),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));

        // --- 1. Image Section (Left) ---
        imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(IMG_WIDTH, IMG_HEIGHT));
        imageLabel.setMinimumSize(new Dimension(IMG_WIDTH, IMG_HEIGHT));
        imageLabel.setMaximumSize(new Dimension(IMG_WIDTH, IMG_HEIGHT));
        imageLabel.setBorder(BorderFactory.createLineBorder(StyleColors.ACCENT_DARK, 1));

        // Load Image
        String imagePath = "Database/Assets/Default_Img.png"; // Default
        // Assuming Course has getImagePath(), if not, use default logic or add getter
        // if (course.getImagePath() != null && !course.getImagePath().isEmpty()) {
        //     imagePath = course.getImagePath();
        // }
        // Note: Since getImagePath() isn't in the Course Model provided yet, I'll comment it out
        // and rely on the default for now, or reflection if you added it in memory.
        // Ideally: imagePath = (course.getImagePath() != null) ? course.getImagePath() : "Database/Assets/Default_Img.png";

        ImageIcon icon = loadResizedIcon(imagePath, IMG_WIDTH, IMG_HEIGHT);
        imageLabel.setIcon(icon);
        add(imageLabel, BorderLayout.WEST);

        // --- 2. Info Section (Center) ---
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(StyleColors.BACKGROUND);
        infoPanel.setOpaque(false); // Allow parent hover color to show

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

    // Overloaded constructor: adds progress info
    public CourseCard(Course course, int numOfCompletedLessons, int totalLessons) {
        this(course);

        // Get the info panel (Center component) to add more details
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
        percentLabel.setForeground(StyleColors.TEXT); // Slightly darker/different if desired
        percentLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        percentLabel.setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
        infoPanel.add(percentLabel);
    }

    /**
     * Helper to load and resize an image safely.
     */
    private ImageIcon loadResizedIcon(String path, int w, int h) {
        try {
            File file = new File(path);
            if (!file.exists()) {
                // Fallback if specific path doesn't exist, try absolute or project root
                file = new File("src/" + path);
                if(!file.exists()) return null; // Or return a generated placeholder
            }
            BufferedImage img = ImageIO.read(file);
            Image scaled = img.getScaledInstance(w, h, Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);
        } catch (Exception e) {
            // e.printStackTrace(); // Fail silently or log
            return null; // Return null so label is empty or we can draw a placeholder rect
        }
    }
}