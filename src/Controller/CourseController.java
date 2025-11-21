package Controller;

import java.util.ArrayList;

import Model.Course.Course;
import Model.Course.Lesson;
import Model.JsonDatabaseManager;
import Model.User.Student;

public class CourseController {
    private JsonDatabaseManager dbManager;

    public CourseController() {
        this.dbManager = JsonDatabaseManager.getInstance();
    }

    public ArrayList<Course> getAllCourses() {
        return dbManager.getCourses();
    }

    public String getInstructorName(int instructorId) {
        return dbManager.getUserById(instructorId).getUsername();
    }

    public ArrayList<Course> getCourseByTitle(ArrayList<Course> courses, String title) {
        return dbManager.getCourseByTitle(courses, title);
    }

    public boolean canAccessNextLesson(int studentId, int courseId, int currentLessonId) {
        Student student = (Student) dbManager.getUserById(studentId);
        Course course = dbManager.getCourseById(courseId);
        if (student == null || course == null) {
            return false;
        }

        Lesson currentLesson = course.getLessonById(currentLessonId);
        if (currentLesson == null) {
            return false; // Or handle as an error
        }

        // If the current lesson has no quiz, access to the next lesson is granted upon completion.
        if (currentLesson.getQuiz() == null) {
            return student.isLessonCompleted(courseId, currentLessonId);
        }

        // If there's a quiz, check if it's passed.
        return student.hasPassedQuiz(currentLessonId, currentLesson.getQuiz().getPassThreshold());
    }
}
