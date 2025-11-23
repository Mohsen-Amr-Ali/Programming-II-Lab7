package util;

import Model.Course.Course;
import Model.Course.Lesson;
import Model.Course.Quiz;
import Model.User.Student;

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
                if (!quiz.hasPassedQuiz(quiz))
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

        for (Lesson lesson : lessons) {
            Quiz quiz = lesson.getQuiz();
            if (quiz != null && students.hasPassedQuiz(quiz)) {
                passed++;
            }
        }

        return (passed * 100.0) / lessons.size();
    }

}
