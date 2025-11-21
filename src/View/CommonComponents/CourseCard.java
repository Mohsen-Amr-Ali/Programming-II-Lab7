package View.CommonComponents;

import Controller.CourseController;
import Model.Course.Course;
import View.StyledComponents.SCoursePanel;
import View.StyledComponents.StyleColors;

import javax.swing.*;
import java.awt.*;

public class CourseCard extends SCoursePanel {
    private JLabel titleLabel;
    private JLabel instructorLabel;
    private JLabel progressLabel;
    private JLabel percentLabel;

    // Basic constructor: title and instructor
    public CourseCard(Course course) {
        String instructorName = new CourseController().getInstructorName(course.getInstructorId());
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(StyleColors.BACKGROUND);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(StyleColors.ACCENT, 2, true),
                BorderFactory.createEmptyBorder(18, 24, 18, 24)
        ));

        titleLabel = new JLabel(course.getTitle());
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(StyleColors.TEXT);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(titleLabel);

        instructorLabel = new JLabel("<html><b><i>Instructor: </i></b><i>" + instructorName + "</i></html>");
        instructorLabel.setFont(new Font("Segoe UI", Font.ITALIC | Font.BOLD, 14));
        instructorLabel.setForeground(StyleColors.TEXT);
        instructorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        instructorLabel.setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
        add(instructorLabel);
    }

    // Overloaded constructor: adds progress info
    public CourseCard(Course course, int numOfCompletedLessons, int totalLessons) {
        this(course);
        if (totalLessons > 0) {
            int percent = (int) Math.round((numOfCompletedLessons * 100.0) / totalLessons);
            progressLabel = new JLabel(numOfCompletedLessons + " out of " + totalLessons + " Completed");
            percentLabel = new JLabel("Progress: " + percent + "%");
        } else {
            progressLabel = new JLabel("No lessons");
            percentLabel = new JLabel("Progress: 0%");
        }
        progressLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        progressLabel.setForeground(StyleColors.TEXT);
        progressLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        progressLabel.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));
        add(progressLabel);

        percentLabel.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        percentLabel.setForeground(StyleColors.TEXT);
        percentLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        percentLabel.setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
        add(percentLabel);
    }
}
