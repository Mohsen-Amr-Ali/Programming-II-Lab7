package Controller;

import Model.Course.Course;
import Model.Course.COURSE_STATUS;
import Model.JsonDatabaseManager;
import java.util.ArrayList;

public class AdminController {
    private JsonDatabaseManager dbManager;

    public AdminController() {
        this.dbManager = JsonDatabaseManager.getInstance();
    }

    public ArrayList<Course> getPendingCourses() {
        return dbManager.getPendingCourses();
    }

    public void approveCourse(int courseId) {
        dbManager.changeCourseStatus(courseId, COURSE_STATUS.APPROVED);
    }

    public void declineCourse(int courseId) {
        dbManager.changeCourseStatus(courseId, COURSE_STATUS.REJECTED);
    }

    // Additional methods for searching/filtering pending courses can be added here
    // later.
}