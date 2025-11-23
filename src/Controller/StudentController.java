package Controller;

import Model.Course.Course;
import Model.Course.Lesson;
import Model.Course.Question;
import Model.Course.Quiz;
import Model.JsonDatabaseManager;
import Model.User.Student;

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
            return; // Or throw an exception
        }

        Lesson lesson = course.getLessonById(lessonId);
        if (lesson == null || lesson.getQuiz() == null) {
            return; // Or throw an exception
        }

        Quiz quiz = lesson.getQuiz();
        double score = calculateScore(quiz, answers);
        student.addQuizScore(lessonId, score);

        if (student.hasPassedQuiz(lessonId, quiz.getPassThreshold())) {
            markLessonAsCompleted(studentId, courseId, lessonId);
        }

        dbManager.updateUser(student);
    }

    private double calculateScore(Quiz quiz, HashMap<Integer, Integer> answers) {
        int correctAnswers = 0;
        for (Question question : quiz.getQuestions()) {
            if (answers.containsKey(question.getQuestionId()) && answers.get(question.getQuestionId()).equals(question.getCorrectAnswerIndex())) {
                correctAnswers++;
            }
        }
        return (double) correctAnswers / quiz.getQuestions().size() * 100;
    }
}
