package util;

import Model.Course.*;
import Model.User.Student;
import Model.User.User;
import Model.User.Instructor; // Added import
import Model.User.Admin; // Added import
import Model.JsonDatabaseManager;

import java.util.*;

public class Analytics {

    // instructor analytics //
    public double getCourseCompletionPercentage (Course course) {
        JsonDatabaseManager dbManager = JsonDatabaseManager.getInstance();
        ArrayList<Integer> enrolledStudentIds = course.getStudents();
        if (enrolledStudentIds == null || enrolledStudentIds.isEmpty()) {
            return 0;
        }

        int completed = 0;

        for (Integer studentId : enrolledStudentIds)
        {
            User user = dbManager.getUserById(studentId);
            if (user instanceof Student) {
                Student student = (Student) user;
                if (hasCompletedCourse(student, course)) {
                    completed++;
                }
            }
        }
        return (completed * 100.0) / enrolledStudentIds.size();
    }

    public Map<String, Double> getAverageQuizScorePerLesson (Course course) {
        Map<String, Double> avgScores = new LinkedHashMap<>(); // Use LinkedHashMap to keep lesson order
        JsonDatabaseManager dbManager = JsonDatabaseManager.getInstance();
        ArrayList<Integer> enrolledStudentIds = course.getStudents();

        for (Lesson lesson : course.getLessons()) {
            Quiz quiz = lesson.getQuiz();
            if (quiz == null) {
                continue;
            }

            double totalScorePercent = 0;
            int count = 0;

            // Iterate through all students enrolled in this course
            for (Integer studentId : enrolledStudentIds) {
                User user = dbManager.getUserById(studentId);
                if (user instanceof Student) {
                    Student student = (Student) user;

                    // Get the student's latest result for this specific lesson
                    // Note: Student.java stores results by Lesson ID
                    QuizResult result = student.getLatestQuizResultByLessonId(lesson.getLessonId());

                    if (result != null) {
                        // Calculate percentage for this attempt
                        double percent = (double) result.getScore() / result.getMaxScore() * 100.0;
                        totalScorePercent += percent;
                        count++;
                    }
                }
            }

            if (count > 0) {
                avgScores.put(lesson.getTitle(), totalScorePercent / count);
            } else {
                avgScores.put(lesson.getTitle(), 0.0);
            }
        }
        return avgScores;
    }

    public Map<String, Double> getClassPerformanceTimeline (Course course) {
        return getAverageQuizScorePerLesson(course);
    }

    // student analytics //
    public String getQuizResult (QuizResult result) {
        return "You scored " + result.getScore() + "/" + result.getMaxScore();
    }

    public boolean hasCompletedCourse (Student student, Course course) {
        return student.isCourseComplete(course); // Reusing the logic we added to Student.java
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
            // If lesson has a quiz, check if passed
            if (quiz != null) {
                totalWithQuizzes++;
                if (student.hasPassedQuiz(lesson.getLessonId(), quiz.getPassThreshold())) {
                    passed++;
                }
            } else {
                // If no quiz, check manual completion (optional, depends on logic)
                // For now, analytics usually focuses on assessed content
            }
        }

        return totalWithQuizzes == 0 ? 0 : (double) passed / totalWithQuizzes * 100.0;
    }

    // admin analytics //
    public int getPendingCourseCount (ArrayList<Course> courses) {
        int pending = 0;
        for (Course course : courses) {
            if (course.getStatus() == COURSE_STATUS.PENDING) {
                pending++;
            }
        }
        return pending;
    }

    public Map<String, Integer> systemHealth (JsonDatabaseManager db) {
        // We need access to all users.
        // Assuming the next update to JsonDatabaseManager provides 'getAllUsers()'
        ArrayList<User> users = db.getAllUsers();

        int students = 0, instructors = 0;

        for (User u : users) {
            if (u instanceof Student) {
                students++;
            }
            else if (u instanceof Instructor)
            {
                instructors++;
            }
        }

        ArrayList<Course> courses = db.getCourses();
        int approved = 0, pending = 0, rejected = 0;

        for (Course course : courses) {
            if (course.getStatus() == COURSE_STATUS.APPROVED) approved++;
            else if (course.getStatus() == COURSE_STATUS.PENDING) pending++;
            else if (course.getStatus() == COURSE_STATUS.REJECTED) rejected++;
        }

        Map<String, Integer> health = new LinkedHashMap<>();
        health.put("students", students);
        health.put("instructors", instructors);
        health.put("total_courses", courses.size());
        health.put("approved_courses", approved);
        health.put("pending_courses", pending);
        health.put("rejected_courses", rejected);

        return health;
    }
}