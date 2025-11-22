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

        public Certificate generateCertificate(int studentId, int courseId) {
            // Check completion
            if (!isCourseCompleted(studentId, courseId)) {
                return null; // cannot generate certificate
            }

            Student student = (Student) dbManager.getUserById(studentId);
            if (student == null) return null;

            // Generate certificateId (DB should handle ID creation)
            String certificateId = dbManager.generateCertificateId();

            // Get today's date
            String issueDate = java.time.LocalDate.now().toString();


            // Create certificate object
            Certificate certificate = new Certificate(certificateId, studentId, courseId, issueDate);

            // Add to student
            student.getCertificates().add(certificate);

            // Save updated student to JSON
            dbManager.updateUser(student);

            return certificate;
        }

    }
}
