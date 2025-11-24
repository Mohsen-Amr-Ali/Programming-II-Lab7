package Controller;

import Model.Course.Certificate;
import Model.Course.Course;
import Model.Course.Lesson;
import Model.Course.Question;
import Model.Course.Quiz;
import Model.Course.QuizResult;
import Model.JsonDatabaseManager;
import Model.User.Student;
import util.CertificatePDFGenerator;

import java.time.LocalDate;
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
        if (student == null || course == null) return;

        Lesson lesson = course.getLessonById(lessonId);
        if (lesson == null || lesson.getQuiz() == null) return;

        Quiz quiz = lesson.getQuiz();

        // 1. Calculate Score
        int maxScore = quiz.getQuestions().size();
        int correctAnswers = calculateCorrectAnswers(quiz, answers);

        // 2. Determine Pass/Fail
        double percentage = (double) correctAnswers / maxScore * 100.0;
        boolean passed = percentage >= quiz.getPassThreshold();

        // 3. Create Result Object
        QuizResult result = new QuizResult(correctAnswers, maxScore, LocalDateTime.now(), passed);

        // 4. Save Attempt
        student.addQuizAttempt(lessonId, result);

        // 5. Mark Lesson Complete if Passed
        if (student.hasPassedQuiz(lessonId, quiz.getPassThreshold())) {
            markLessonAsCompleted(studentId, courseId, lessonId);
        }

        dbManager.updateUser(student);
    }

    private int calculateCorrectAnswers(Quiz quiz, HashMap<Integer, Integer> answers) {
        int correctAnswers = 0;
        for (Question question : quiz.getQuestions()) {
            if (answers.containsKey(question.getQuestionId()) &&
                    answers.get(question.getQuestionId()).equals(question.getCorrectAnswerIndex())) {
                correctAnswers++;
            }
        }
        return correctAnswers;
    }

    /**
     * Generates a certificate PDF for a completed course.
     * @param studentId The student ID.
     * @param courseId The course ID.
     * @param filePath The path where the PDF should be saved.
     * @return true if generation successful, false otherwise.
     */
    public boolean generateCertificate(int studentId, int courseId, String filePath) {
        Student student = (Student) dbManager.getUserById(studentId);
        Course course = dbManager.getCourseById(courseId);

        if (student == null || course == null) return false;

        // Verify Completion
        if (!student.isCourseComplete(course)) {
            System.out.println("Cannot generate certificate: Course not complete.");
            return false;
        }

        // Create Certificate Data
        // Check if certificate already exists for this course?
        // For now, we generate a new instance each download or retrieve existing if we tracked it.
        // We will generate a new ID for the record.
        int certId = student.generateCertificateId();
        String date = LocalDate.now().toString();

        Certificate cert = new Certificate(certId, course, student, date);

        // Save the record to student profile (optional, but good for history)
        student.getCertificates().add(cert);
        dbManager.updateUser(student);

        // Generate PDF
        return CertificatePDFGenerator.generateCertificatePDF(cert, filePath);
    }
}