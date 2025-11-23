package Controller;

import Model.Course.Course;
import Model.Course.Lesson;
import Model.Course.Question;
import Model.Course.Quiz;
import Model.Course.QuizResult;
import Model.JsonDatabaseManager;
import Model.User.Student;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StudentController {

    private JsonDatabaseManager dbManager;

    public StudentController() {
        this.dbManager = JsonDatabaseManager.getInstance();
    }

    public ArrayList<Course> getEnrolledCourses(int studentId) {
        Student student = (Student) dbManager.getUserById(studentId);
        ArrayList<Course> enrolledCourses = new ArrayList<>();
        if (student != null) {
            for (int courseId : student.getEnrolledCoursesIDs()) {
                enrolledCourses.add(dbManager.getCourseById(courseId));
            }
        }
        return enrolledCourses;
    }

    public ArrayList<Integer> getCompletedLessons(int studentId, int courseId) {
        Student student = (Student) dbManager.getUserById(studentId);
        if (student != null) {
            return student.getCompletedLessons(courseId);
        }
        return new ArrayList<>();
    }

    public void enrollInCourse(int studentId, int courseId) {
        Student student = (Student) dbManager.getUserById(studentId);
        Course course = dbManager.getCourseById(courseId);
        if (student != null && course != null) {
            student.enrollInCourse(courseId);
            course.enrollStudent(studentId);
            dbManager.updateUser(student);
            dbManager.updateCourses(course);
        }
    }

    public void dropCourse(int studentId, int courseId) {
        Student student = (Student) dbManager.getUserById(studentId);
        Course course = dbManager.getCourseById(courseId);
        if (student != null && course != null) {
            student.dropCourse(courseId);
            course.dropStudent(studentId);
            dbManager.updateUser(student);
            dbManager.updateCourses(course);
        }
    }

    public void markLessonAsCompleted(int studentId, int courseId, int lessonId) {
        Student student = (Student) dbManager.getUserById(studentId);
        if (student != null) {
            student.addCompletedLesson(courseId, lessonId);
            dbManager.updateUser(student);
        }
    }

    public void unmarkLessonAsCompleted(int studentId, int courseId, int lessonId) {
        Student student = (Student) dbManager.getUserById(studentId);
        if (student != null) {
            student.removeCompletedLesson(courseId, lessonId);
            dbManager.updateUser(student);
        }
    }

    public void submitQuiz(int studentId, int courseId, int lessonId, HashMap<Integer, Integer> answers) {
        Student student = (Student) dbManager.getUserById(studentId);
        Course course = dbManager.getCourseById(courseId);
        if (student == null || course == null) {
            return; // Or handle error
        }

        Lesson lesson = course.getLessonById(lessonId);
        if (lesson == null || lesson.getQuiz() == null) {
            // If no quiz exists, maybe mark complete immediately?
            // For now, do nothing if quiz logic was requested but missing.
            return;
        }

        Quiz quiz = lesson.getQuiz();

        // 1. Calculate Score
        int maxScore = quiz.getQuestions().size();
        int correctAnswers = calculateCorrectAnswers(quiz, answers);

        // 2. Determine Pass/Fail
        double percentage = (double) correctAnswers / maxScore * 100.0;
        boolean passed = percentage >= quiz.getPassThreshold(); // Assuming threshold is e.g. 60.0

        // 3. Create Result Object
        QuizResult result = new QuizResult(correctAnswers, maxScore, LocalDateTime.now(), passed);

        // 4. Save Attempt
        student.addQuizAttempt(lessonId, result);

        // 5. Mark Lesson Complete if Passed (Check history to be safe)
        if (student.hasPassedQuiz(lessonId, quiz.getPassThreshold())) {
            markLessonAsCompleted(studentId, courseId, lessonId);
        } else {
            // Optionally remove completion if they failed a re-take?
            // Usually we keep completion once earned.
        }

        dbManager.updateUser(student);
    }

    private int calculateCorrectAnswers(Quiz quiz, HashMap<Integer, Integer> answers) {
        int correctAnswers = 0;
        for (Question question : quiz.getQuestions()) {
            // Check if answer exists and matches correct index
            if (answers.containsKey(question.getQuestionId()) &&
                    answers.get(question.getQuestionId()).equals(question.getCorrectAnswerIndex())) {
                correctAnswers++;
            }
        }
        return correctAnswers;
    }
}