package Controller;

import Model.Course.Course;
import Model.JsonDatabaseManager;
import Model.User.Student;
import util.Certificate;

import java.util.ArrayList;

public class StudentController {

    private JsonDatabaseManager dbManager;

    public StudentController() {
        this.dbManager = JsonDatabaseManager.getInstance();
    }

    public ArrayList<Course> getEnrolledCourses(int studentId) {
        Student student = (Student) dbManager.getUserById(studentId);
        ArrayList<Course> enrolledCourses = new ArrayList<>();
        if (student != null) {
            for (Integer courseId : student.getEnrolledCoursesIDs()) {
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
    public boolean isCourseCompleted(int studentId, int courseId) {
        Student student = (Student) dbManager.getUserById(studentId);
        Course course = dbManager.getCourseById(courseId);
        if (student != null && course != null) {
            ArrayList<Integer> completedLessons = student.getCompletedLessons(courseId); // Get the completed lessons for the course
            return completedLessons.size() == course.getLessons().size(); // Compare with total lessons in the course, if equal, course is completed
        }
        return false;
    }

    public Certificate generateCertificate(int studentId, int courseId, int certificateId) {

        // 1) Check if student finished all lessons
        if (!isCourseCompleted(studentId, courseId)) {
            return null; // cannot issue certificate
        }

        // 2) Load student
        Student student = (Student) dbManager.getUserById(studentId);
        if (student == null) return null;

        // 3) Generate certificate fields
        String issueDate = java.time.LocalDate.now().toString();

        // 4) Create certificate object
        Certificate certificate = new Certificate(certificateId, studentId, courseId, issueDate);

        // 5) Attach certificate to student object
        student.getCertificates().add(certificate);

        // 6) Save student in JSON
        dbManager.updateUser(student);

        // 7) Generate PDF file
        String outputPath = "certificates/" + certificateId + ".pdf";
        util.CertificatePDFGenerator.generateCertificatePDF(certificate, outputPath);

        System.out.println("Certificate PDF Created at: " + outputPath);

        return certificate;
    }
}
