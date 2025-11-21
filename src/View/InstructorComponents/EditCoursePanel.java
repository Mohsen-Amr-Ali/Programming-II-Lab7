package View.InstructorComponents;

import Model.Course.Course;

public class EditCoursePanel extends AddCoursePanel {
    private Course course;

    public EditCoursePanel(Course course) {
        super();
        this.course = course;
        // Pre-fill the fields with course data
        setTitleText(course.getTitle());
        setDescriptionText(course.getDescription());
    }

    public Course getCourse() {
        return course;
    }
}