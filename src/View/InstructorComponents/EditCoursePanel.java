package View.InstructorComponents;

import Model.Course.Course;
import View.StyledComponents.SOptionPane;
import View.StyledComponents.StyleColors;

import java.awt.Color; // Added for explicit foreground setting

import javax.swing.*;
import java.awt.event.ActionListener;
import java.io.File;

public class EditCoursePanel extends AddCoursePanel {
    private Course course;

    public EditCoursePanel(Course course) {
        super();
        this.course = course;

        // 1. Pre-fill Title
        setTitleText(course.getTitle());

        // 2. Pre-fill Description
        // Description is plain text, so set it directly.
        setDescriptionText(course.getDescription());

        // 3. Pre-fill Image Label - Show existing image filename if available
        String imgPath = course.getImagePath();
        if (imgPath != null && !imgPath.isEmpty()) {
            // Extract just the filename from the path (e.g., "Course_7001\\title.jpg" -> "title.jpg")
            String fileName = imgPath.substring(imgPath.lastIndexOf('\\') + 1);
            setImageFileName("Current: " + fileName);
        }

        // 4. Update Button Text & Emphasize Styling
        getAddButton().setText("Save Changes");
        getAddButton().setBackground(StyleColors.ACCENT); // Accent to highlight edit action
        getAddButton().setForeground(Color.WHITE); // Ensure high contrast
    }

    /**
     * Adds an action listener to the Save button that triggers a warning dialog first.
     * @param saveAction The controller action to execute if user confirms.
     */
    public void addSaveListener(ActionListener saveAction) {
        // Remove existing listeners from parent init (which handles 'Add')
        for (ActionListener al : getAddButton().getActionListeners()) {
            getAddButton().removeActionListener(al);
        }

        getAddButton().addActionListener(e -> {
            int result = SOptionPane.showConfirmDialog(
                    getRootPanel(),
                    "<html>Warning: Saving changes will update the course details permanently.<br>" +
                            "Are you sure you want to continue?</html>",
                    "Confirm Save",
                    JOptionPane.YES_NO_OPTION
            );

            if (result == JOptionPane.YES_OPTION) {
                saveAction.actionPerformed(e);
            }
        });
    }

    public Course getCourse() {
        return course;
    }
}