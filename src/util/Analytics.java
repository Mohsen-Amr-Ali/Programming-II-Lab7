package util;

import Model.Course.Course;
import Model.Course.COURSE_STATUS;
import Model.Course.Lesson;
import Model.Course.Quiz;
import Model.User.Student;
import Model.User.User;
import Model.JsonDatabaseManager;

import java.util.*;

public class Analytics {

    // instructor analytics //


    // student analytics //
    public String getQuizResult (QuizResult result) {
        return "You scored " + result.getScore() + "/" + result.getMaxScore();
    }

    public boolean hasCompletedCourse (Student student, Course course) {
        for (Lesson lesson : course.getLessons()) {
            Quiz quiz = lesson.getQuiz();
            if (quiz != null) {
                QuizResult result = student.getLatestQuizResult(quiz);
                if (result == null || !result.checkPassed(quiz))
                {
                    return false;
                }
            }
        }
        return true;
    }

    public double getLessonProgress (Student student, Course course) {
        ArrayList<Lesson> lessons = course.getLessons();
        if (lessons.isEmpty()) {
            return 0;
        }

        int passed = 0;
        int totalWithQuizzes = 0;

        for (Lesson lesson : lessons) {
            Quiz quiz = lesson.getQuiz();
            if (quiz != null) {
                totalWithQuizzes++;
                QuizResult result = student.getLatestQuizResult(quiz);
                if (result != null && result.checkPassed(quiz)) {
                    passed++;
                }
            }
        }

        return totalWithQuizzes == 0 ? 0 : (passed * 100.0) / totalWithQuizzes; // lessons without quizzes won't affect the percentage
    }

    // admin analytics //
    public int getPendingCourseCount (ArrayList<Course> courses) {
        int pending = 0;

        for (Course course : courses) {
            if (course.getStatus() == COURSE_STATUS.PENDING)
            {
                pending++;
            }
        }

        return pending;
    }

    public Map<String, Integer> systemHealth (ArrayList<User> users, ArrayList<Course> courses) {
        int students = 0, instructors = 0, approved = 0, pending = 0;

        for (User u : users) {
            if (u instanceof Student) {
                students++;
            }
            else
            {
                instructors++;
            }
        }

        for (Course course : courses) {
            if (course.getStatus() == COURSE_STATUS.APPROVED)
            {
                approved++;
            }
            if (course.getStatus() == COURSE_STATUS.PENDING)
            {
                pending++;
            }
        }

        Map<String, Integer> health = new LinkedHashMap<>();
        health.put("students", students);
        health.put("instructors", instructors);
        health.put("total_courses", courses.size());
        health.put("approved", approved);
        health.put("pending", pending);

        return health;
    }
}
