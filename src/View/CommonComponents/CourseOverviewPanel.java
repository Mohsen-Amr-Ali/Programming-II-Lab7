package View.CommonComponents;

import Controller.CourseController;
import Model.Course.Course;
import Model.Course.COURSE_STATUS;
import View.StyledComponents.SLabel;
import View.StyledComponents.SScrollPane;
import View.StyledComponents.StyleColors;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class CourseOverviewPanel extends JPanel {
    // Set the parent path for all file/image operations
    private static final String parentPath = "Y:\\AlexU\\Term 5\\Programming 2\\Programming-II-Lab7\\src\\";

    Course course;
    String instructorName;
    private static final int IMG_WIDTH = 300;
    private static final int IMG_HEIGHT = 200;

    // Basic constructor
    public CourseOverviewPanel(Course course) {
        initPanel(course, null);
    }

    // Constructor with Status (For Admin/Instructor view)
    public CourseOverviewPanel(Course course, COURSE_STATUS status) {
        initPanel(course, status);
    }

    private void initPanel(Course course, COURSE_STATUS status) {
        this.course = course;
        this.instructorName = new CourseController().getInstructorName(course.getInstructorId());
        setLayout(new BorderLayout(0, 0));
        setBackground(StyleColors.BACKGROUND);

        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(StyleColors.ACCENT, 2, true),
                        BorderFactory.createEmptyBorder(24, 24, 24, 24)
                )
        ));

        // --- IMAGE SECTION ---
        SLabel imageLabel = new SLabel();
        imageLabel.setPreferredSize(new Dimension(IMG_WIDTH, IMG_HEIGHT));
        imageLabel.setMinimumSize(new Dimension(IMG_WIDTH, IMG_HEIGHT));
        imageLabel.setMaximumSize(new Dimension(IMG_WIDTH, IMG_HEIGHT));
        imageLabel.setBorder(BorderFactory.createLineBorder(StyleColors.ACCENT_DARK, 1));

        // Construct full image path: parentPath + Database\Assets\ + (course folder + filename from imagePath)
        String imagePath;
        System.out.println("CourseOverviewPanel: course.getImagePath() returned: '" + course.getImagePath() + "'");
        if (course.getImagePath() != null && !course.getImagePath().isEmpty()) {
            imagePath = parentPath + "Database\\Assets\\" + course.getImagePath();
            System.out.println("CourseOverviewPanel: Constructed path: '" + imagePath + "'");
        } else {
            imagePath = parentPath + "Database\\Assets\\Default_Img.png";
            System.out.println("CourseOverviewPanel: Using default image path");
        }

        ImageIcon icon = loadResizedIcon(imagePath, IMG_WIDTH, IMG_HEIGHT);
        if (icon == null) {
            icon = loadResizedIcon(parentPath + "Database\\Assets\\Default_Img.png", IMG_WIDTH, IMG_HEIGHT);
        }
        imageLabel.setIcon(icon);

        // --- HEADER SECTION ---
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(StyleColors.BACKGROUND);
        topPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        topPanel.add(imageLabel);
        topPanel.add(Box.createVerticalStrut(15)); // Space between image and title

        // Status Banner (if provided)
        if (status != null) {
            SLabel statusLabel = createStatusLabel(status);
            // Add status at the top right or just above title?
            // Above title is cleaner in vertical layout
            JPanel statusWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            statusWrapper.setBackground(StyleColors.BACKGROUND);
            statusWrapper.add(statusLabel);
            topPanel.add(statusWrapper);
            topPanel.add(Box.createVerticalStrut(8));
        }

        // Title
        JLabel titleLabel = new JLabel(course.getTitle());
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26)); // Slightly larger title
        titleLabel.setForeground(StyleColors.TEXT);
        titleLabel.setHorizontalAlignment(SwingConstants.LEFT);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        topPanel.add(titleLabel);

        // Instructor
        JLabel instructorLabel = new JLabel();
        instructorLabel.setText("<html><b><i>Instructor: </i></b><i>" + instructorName + "</i></html>");
        instructorLabel.setFont(new Font("Segoe UI", Font.ITALIC | Font.BOLD, 16));
        instructorLabel.setForeground(StyleColors.TEXT);
        instructorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        instructorLabel.setBorder(BorderFactory.createEmptyBorder(4, 0, 0, 0));
        topPanel.add(instructorLabel);

        add(topPanel, BorderLayout.NORTH);

        // --- DESCRIPTION SECTION ---
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(StyleColors.BACKGROUND);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        JLabel descLabel = new JLabel("Description:");
        descLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        descLabel.setForeground(StyleColors.ACCENT); // Accent color for section header
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(descLabel);
        infoPanel.add(Box.createVerticalStrut(5));

        JTextArea descriptionArea = new JTextArea(course.getDescription());
        descriptionArea.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        descriptionArea.setForeground(StyleColors.TEXT);
        descriptionArea.setBackground(StyleColors.BACKGROUND);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setEditable(false);
        descriptionArea.setFocusable(false);
        descriptionArea.setBorder(null);
        descriptionArea.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Make the description scrollable
        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(StyleColors.BACKGROUND);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        // Flexible height
        infoPanel.add(scrollPane);

        add(infoPanel, BorderLayout.CENTER);
    }

    private SLabel createStatusLabel(COURSE_STATUS status) {
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
                statusText = "PENDING APPROVAL";
                break;
        }

        SLabel label = new SLabel(statusText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(Color.WHITE); // White text on colored bg
        label.setOpaque(true);
        label.setBackground(statusColor);
        label.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        return label;
    }

    private ImageIcon loadResizedIcon(String path, int w, int h) {
        try {
            File file = new File(path);
            System.out.println("CourseOverviewPanel: Attempting to load image from: " + file.getAbsolutePath());

            if (!file.exists()) {
                System.out.println("CourseOverviewPanel: Image not found, trying default image");
                // Try fallback to default image
                file = new File(parentPath + "Database\\Assets\\Default_Img.png");
                if(!file.exists()) {
                    System.out.println("CourseOverviewPanel: Default image not found at: " + file.getAbsolutePath());
                    return null;
                }
            }

            BufferedImage img = ImageIO.read(file);
            if (img == null) {
                System.out.println("CourseOverviewPanel: ImageIO.read returned null for: " + file.getAbsolutePath());
                return null;
            }

            Image scaled = img.getScaledInstance(w, h, Image.SCALE_SMOOTH);
            System.out.println("CourseOverviewPanel: Successfully loaded and scaled image");
            return new ImageIcon(scaled);
        } catch (Exception e) {
            System.out.println("CourseOverviewPanel: Error loading image: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}