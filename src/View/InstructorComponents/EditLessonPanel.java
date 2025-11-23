package View.InstructorComponents;

import Model.Course.Lesson;
import Model.FileManager;
import View.StyledComponents.SOptionPane;
import View.StyledComponents.StyleColors;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.io.File;

public class EditLessonPanel extends AddLessonPanel {
    private Lesson lesson;
    private int currentLessonIndex;

    /**
     * Constructor for EditLessonPanel
     * @param lesson The lesson object to edit
     * @param currentLessonIndex The 0-based index of this lesson in the course list (for position dropdown)
     * @param totalLessons The total number of lessons in the course (to populate dropdown)
     */
    public EditLessonPanel(Lesson lesson, int currentLessonIndex, int totalLessons) {
        super();
        this.lesson = lesson;
        this.currentLessonIndex = currentLessonIndex;

        // 1. Pre-fill Title
        setTitleText(lesson.getTitle());

        // 2. Pre-fill Content
        // The 'content' field in Lesson now stores a file path to the binary data.
        // We must read this data back into text to display it in the editor.
        String contentPath = lesson.getContent();
        String displayContent = "";

        if (contentPath != null && !contentPath.isEmpty()) {
            // Try to read as file first (relative path from src root usually)
            File f = new File("src/" + contentPath);
            if (f.exists()) {
                displayContent = FileManager.readBinaryToText(contentPath);
            } else {
                // Fallback for legacy data or absolute paths
                File abs = new File(contentPath);
                if (abs.exists()) {
                    displayContent = FileManager.readBinaryToText(contentPath);
                } else {
                    // Assume it was raw text (legacy support)
                    displayContent = contentPath;
                }
            }
        }
        setContentText(displayContent);

        // Default to showing the Text tab with the content loaded
        typeTextRadio.setSelected(true);
        contentCardLayout.show(contentInputContainer, "TEXT");

        // 3. Pre-fill Position
        // Populate the dropdown with 1..N (we don't need N+1 for editing, just N is fine,
        // but standardizing on the AddLessonPanel logic is okay).
        // We need to populate it first.
        setLessonCount(totalLessons - 1); // -1 because we are one of them?
        // Actually, if we move it, we can move to any slot 1..N.
        // If we use setLessonCount(totalLessons), we get 1..N+1.
        // For editing, we usually want to just swap positions.
        // Let's populate 1 to Total.
        positionComboBox.removeAllItems();
        for (int i = 1; i <= totalLessons; i++) {
            positionComboBox.addItem(i);
        }

        // Select current (currentLessonIndex is 0-based, combo is 1-based)
        if (currentLessonIndex >= 0 && currentLessonIndex < totalLessons) {
            positionComboBox.setSelectedIndex(currentLessonIndex);
            setAddAtEndChecked(false); // Explicitly enable the dropdown
        } else {
            setAddAtEndChecked(true);
        }

        // 4. Update Button Text
        getAddButton().setText("Save Changes");
    }

    /**
     * Adds an action listener to the Save button that triggers a warning dialog first.
     * @param saveAction The controller action to execute if user confirms.
     */
    public void addSaveListener(ActionListener saveAction) {
        // Remove existing listeners from parent init
        for (ActionListener al : getAddButton().getActionListeners()) {
            getAddButton().removeActionListener(al);
        }

        getAddButton().addActionListener(e -> {
            int result = SOptionPane.showConfirmDialog(
                    getRootPanel(),
                    "<html>Warning: Saving changes will <b>overwrite</b> the existing lesson content.<br>" +
                            "The previous content file will be replaced permanently.<br>" +
                            "This action cannot be undone. Continue?</html>",
                    "Confirm Save",
                    JOptionPane.YES_NO_OPTION
            );

            if (result == JOptionPane.YES_OPTION) {
                saveAction.actionPerformed(e);
            }
        });
    }

    public Lesson getLesson() {
        return lesson;
    }
}