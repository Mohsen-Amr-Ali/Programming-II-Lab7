package View.InstructorComponents;

import Model.Course.Lesson;

public class EditLessonPanel extends AddLessonPanel {
    private Lesson lesson;

    public EditLessonPanel(Lesson lesson) {
        super();
        this.lesson = lesson;
        setTitleText(lesson.getTitle());
        setContentText(lesson.getContent());
        // Optionally set position and addAtEnd if you want to pre-fill them
    }

    public Lesson getLesson() {
        return lesson;
    }
}