package View.InstructorComponents;

import Model.Course.Lesson;
import Model.Course.Quiz;
import Model.FileManager;
import View.StyledComponents.SBtn;
import View.StyledComponents.SOptionPane;
import View.StyledComponents.StyleColors;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;

public class EditLessonPanel extends AddLessonPanel {
    private Lesson lesson;
    private int currentLessonIndex;
    private SBtn manageQuizBtn;

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
        setLessonCount(totalLessons - 1); // 1..N
        positionComboBox.removeAllItems();
        for (int i = 1; i <= totalLessons; i++) {
            positionComboBox.addItem(i);
        }

        // Select current
        if (currentLessonIndex >= 0 && currentLessonIndex < totalLessons) {
            positionComboBox.setSelectedIndex(currentLessonIndex);
            setAddAtEndChecked(false);
        } else {
            setAddAtEndChecked(true);
        }

        // 4. Update Button Text
        getAddButton().setText("Save Changes");

        // 5. Add "Manage Quiz" Button
        addQuizButton();
    }

    private void addQuizButton() {
        // Create the button
        manageQuizBtn = new SBtn(lesson.getQuiz() == null ? "Create Quiz" : "Edit Quiz");
        manageQuizBtn.setBackground(new Color(255, 193, 7)); // Yellow/Orange for distinction
        manageQuizBtn.setForeground(Color.WHITE); // Text color might need adjustment against yellow, trying white
        // Or use StyleColors.ACCENT if preferred, but distinct is nice.
        // Let's stick to Accent for consistency if yellow is too bright.
        manageQuizBtn.setBackground(StyleColors.ACCENT_DARK);

        manageQuizBtn.addActionListener(e -> {
            Window parentWindow = SwingUtilities.getWindowAncestor(getRootPanel());
            AddQuizDialog dialog = new AddQuizDialog(parentWindow, lesson.getQuiz(), () -> {
                // On Save Callback
                // The dialog updates the 'quiz' object passed to it or creates a new one.
                // We need to retrieve it. The dialog logic updates the object reference if passed?
                // Actually, AddQuizDialog updates the 'quiz' field inside itself. We need to get it back.
                // However, since 'lesson.getQuiz()' might be null, passing null means the dialog creates a NEW object.
                // We need to capture that new object.
            });

            // Wait, the runnable in AddQuizDialog runs *before* dispose.
            // We need to access the quiz from the dialog instance.
            // Refactoring logic slightly:

            // Show the dialog
            dialog.setVisible(true);

            // After dialog closes (it is modal), check if we have a quiz
            Quiz updatedQuiz = dialog.getQuiz();
            if (updatedQuiz != null) {
                // Update the lesson object immediately in memory
                lesson.setQuiz(updatedQuiz);
                manageQuizBtn.setText("Edit Quiz");
                SOptionPane.showMessageDialog(getRootPanel(), "Quiz attached to lesson. Click 'Save Changes' to persist.", "Quiz Updated", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // Add to layout (Row 6, shifting Add button to Row 7)
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(7, 7, 7, 7);
        gbc.gridx = 0;
        gbc.gridy = 5; // Where Add button was? No, Add button was row 5 in parent.
        // Parent layout:
        // 0: Title
        // 1: Title Input
        // 2: Type
        // 3: Content
        // 4: Position
        // 5: Add Button

        // We want to insert Quiz button before Add Button.
        // Remove Add Button first?
        rootPanel.remove(getAddButton());

        // Add Quiz Button at Row 5
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        rootPanel.add(manageQuizBtn, gbc);

        // Re-add Add Button at Row 6
        gbc.gridy = 6;
        rootPanel.add(getAddButton(), gbc);

        rootPanel.revalidate();
        rootPanel.repaint();
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