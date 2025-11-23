package Controller;

import Model.Course.Course;
import Model.Course.COURSE_STATUS; // Import the Course Status Enum
import Model.JsonDatabaseManager;
import java.util.ArrayList;

/**
 * Controller class dedicated to handling business logic for Admin users,
 * primarily focusing on course approval workflows.
 */
public class AdminController {
    private JsonDatabaseManager dbManager;

    public AdminController() {
        // Ensure the controller uses the singleton instance of the database manager
        this.dbManager = JsonDatabaseManager.getInstance();
    }

    /**
     * Retrieves all courses currently requiring review.
     * @return A list of courses with status PENDING.
     */
    public ArrayList<Course> getPendingCourses() {
        return dbManager.getPendingCourses();
    }

    /**
     * Retrieves all courses that have been approved.
     * @return A list of courses with status APPROVED.
     */
    public ArrayList<Course> getApprovedCourses() {
        return dbManager.getApprovedCourses();
    }

    /**
     * Retrieves all courses that have been rejected.
     * @return A list of courses with status REJECTED.
     */
    public ArrayList<Course> getRejectedCourses() {
        return dbManager.getRejectedCourses();
    }

    /**
     * Changes a course's status to APPROVED.
     * @param courseId The ID of the course to approve.
     */
    public void approveCourse(int courseId) {
        // Calls the necessary method in the database manager
        dbManager.changeCourseStatus(courseId, COURSE_STATUS.APPROVED);
    }

    /**
     * Changes a course's status to REJECTED.
     * @param courseId The ID of the course to reject.
     */
    public void declineCourse(int courseId) {
        // Calls the necessary method in the database manager
        dbManager.changeCourseStatus(courseId, COURSE_STATUS.REJECTED);
    }
}