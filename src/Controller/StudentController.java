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

    public ArrayList<Course> getCompletedCourses(int studentId) {
        Student student = (Student) dbManager.getUserById(studentId);
        ArrayList<Course> completedCourses = new ArrayList<>();
        if (student != null) {
            for (int courseId : student.getEnrolledCoursesIDs()) {
                Course course = dbManager.getCourseById(courseId);
                if (course != null && student.isCourseComplete(course)) {
                    completedCourses.add(course);
                }
            }
        }
        return completedCourses;
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
        // Loop by index since QuizPanel stores answers by question index, not questionId
        for (int i = 0; i < quiz.getQuestions().size(); i++) {
            Question question = quiz.getQuestions().get(i);
            if (answers.containsKey(i) && answers.get(i).equals(question.getCorrectAnswerIndex())) {
                correctAnswers++;
            }
        }
        return correctAnswers;
    }

    /**
     * Generates a certificate PDF for a completed course.
     *
     * @param studentId The student ID.
     * @param courseId  The course ID.
     * @param filePath  The path where the PDF should be saved.
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

        // Check if certificate already exists for this course (in runtime cache)
        Certificate cert = student.getCertificateByCourseId(courseId);

        if (cert == null) {
            // First time - create new certificate
            int certId = student.generateCertificateId();
            String date = LocalDate.now().toString();
            cert = new Certificate(certId, course, student, date);

            // Store in cache and save ID to database
            student.addCertificateToCache(courseId, cert);
            dbManager.updateUser(student); // Saves certificateIds to JSON, not full objects

            System.out.println("New certificate created: ID " + certId + " for course " + courseId);
        } else {
            System.out.println("Reusing existing certificate: ID " + cert.getCertificateId() + " for course " + courseId);
        }

        // Generate PDF file
        boolean success = CertificatePDFGenerator.generateCertificatePDF(cert, filePath);

        if (success) {
            System.out.println("Certificate PDF saved successfully at: " + filePath);
        } else {
            System.out.println("Failed to generate certificate PDF");
        }

        return success;
    }

    public JsonDatabaseManager getDbManager() {
        return dbManager;
    }

    public Map<String, Double> getQuizPerformanceData(int studentId) {
        Map<String, Double> performanceData = new HashMap<>();
        Student student = (Student) dbManager.getUserById(studentId);
        if (student == null) {
            return performanceData;
        }

        // Get all quiz results for the student
        HashMap<Integer, ArrayList<QuizResult>> allResults = student.getQuizResultsMap();
        if (allResults == null || allResults.isEmpty()) {
            return performanceData;
        }

        // Iterate through each lesson that has a quiz result
        for (Map.Entry<Integer, ArrayList<QuizResult>> entry : allResults.entrySet()) {
            int lessonId = entry.getKey();
            ArrayList<QuizResult> results = entry.getValue();

            if (results != null && !results.isEmpty()) {
                // Find the highest score for this lesson
                double highestPercentage = 0.0;
                for (QuizResult result : results) {
                    double percentage = ((double) result.getScore() / result.getMaxScore()) * 100.0;
                    if (percentage > highestPercentage) {
                        highestPercentage = percentage;
                    }
                }

                // Find the lesson title from the lessonId
                Lesson lesson = dbManager.getLessonById(lessonId);
                if (lesson != null) {
                    performanceData.put(lesson.getTitle(), highestPercentage);
                }
            }
        }
        return performanceData;
    }
}