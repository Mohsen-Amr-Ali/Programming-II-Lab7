package View.CommonComponents;

import Controller.CourseController;
import Model.Course.Course;
import Model.Course.COURSE_STATUS;
import View.StyledComponents.SCoursePanel;
import View.StyledComponents.StyleColors;
import View.StyledComponents.SLabel; // Added import

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class CourseCard extends SCoursePanel {
    // Set the parent path for all file/image operations
    private static final String parentPath = "C:\\Users\\malak\\Desktop\\Programming II\\Programming-II-Lab7\\src\\";

    // Use SLabel for all textual/image labels for consistent styling
    private SLabel imageLabel;
    private SLabel titleLabel;
    private SLabel instructorLabel;
    private SLabel progressLabel;
    private SLabel percentLabel;
    private SLabel statusLabel;

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
            progressLabel = new SLabel(numOfCompletedLessons + " out of " + totalLessons + " Completed");
            percentLabel = new SLabel("Progress: " + percent + "%");
        } else {
            progressLabel = new SLabel("No lessons");
            percentLabel = new SLabel("Progress: 0%");
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
        // Use CARD color for the card surface while the parent list uses BACKGROUND
        setBackground(StyleColors.CARD);

        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(StyleColors.ACCENT, 2, true),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));

        // --- Image Section ---
        imageLabel = new SLabel();
        imageLabel.setPreferredSize(new Dimension(IMG_WIDTH, IMG_HEIGHT));
        imageLabel.setMinimumSize(new Dimension(IMG_WIDTH, IMG_HEIGHT));
        imageLabel.setMaximumSize(new Dimension(IMG_WIDTH, IMG_HEIGHT));
        imageLabel.setBorder(BorderFactory.createLineBorder(StyleColors.ACCENT_DARK, 1));

        // Construct full image path: parentPath + Database\Assets\ + (course folder + filename from imagePath)
        String imagePath;
        System.out.println("CourseCard: course.getImagePath() returned: '" + course.getImagePath() + "'");
        if (course.getImagePath() != null && !course.getImagePath().isEmpty()) {
            imagePath = parentPath + "Database\\Assets\\" + course.getImagePath();
            System.out.println("CourseCard: Constructed path: '" + imagePath + "'");
        } else {
            imagePath = parentPath + "Database\\Assets\\Default_Img.png";
            System.out.println("CourseCard: Using default image path");
        }

        ImageIcon icon = loadResizedIcon(imagePath, IMG_WIDTH, IMG_HEIGHT);
        if (icon == null) {
            // Fallback to default image
            icon = loadResizedIcon(parentPath + "Database\\Assets\\Default_Img.png", IMG_WIDTH, IMG_HEIGHT);
        }

        if (icon != null) {
            imageLabel.setIcon(icon);
        } else {
            imageLabel.setText("No Image");
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        }

        add(imageLabel, BorderLayout.WEST);

        // --- Info Section ---
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(StyleColors.CARD);
        infoPanel.setOpaque(false);

        titleLabel = new SLabel(course.getTitle());
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(titleLabel);

        instructorLabel = new SLabel("<html><b><i>Instructor: </i></b><i>" + instructorName + "</i></html>");
        instructorLabel.setFont(new Font("Segoe UI", Font.ITALIC | Font.PLAIN, 14));
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

        statusLabel = new SLabel(statusText);
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        statusLabel.setForeground(Color.WHITE); // Keep high contrast inside colored badge
        statusLabel.setOpaque(true);
        statusLabel.setBackground(statusColor);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));

        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        statusPanel.setOpaque(false);
        statusPanel.add(statusLabel);

        add(statusPanel, BorderLayout.EAST);
    }

    private ImageIcon loadResizedIcon(String path, int w, int h) {
        try {
            File file = new File(path);
            System.out.println("CourseCard: Attempting to load image from: " + file.getAbsolutePath());

            if (!file.exists()) {
                System.out.println("CourseCard: Image not found, trying default image");
                file = new File(parentPath + "Database\\Assets\\Default_Img.png");
                if (!file.exists()) {
                    System.out.println("CourseCard: Default image not found at: " + file.getAbsolutePath());
                    return null;
                }
            }

            BufferedImage img = ImageIO.read(file);
            if (img == null) {
                System.out.println("CourseCard: ImageIO.read returned null for: " + file.getAbsolutePath());
                return null;
            }

            Image scaled = img.getScaledInstance(w, h, Image.SCALE_SMOOTH);
            System.out.println("CourseCard: Successfully loaded and scaled image");
            return new ImageIcon(scaled);
        } catch (Exception e) {
            System.out.println("CourseCard: Error loading image: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}