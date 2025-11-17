package Controller;

import java.util.ArrayList;

import Model.Course;
import Model.JsonDatabaseManager;

public class CourseController {
    private JsonDatabaseManager dbManager;

    public CourseController() {
        this.dbManager = JsonDatabaseManager.getInstance();
    }

    public ArrayList<Course> getAllCourses() {
        return dbManager.getCourses();
    }

    public String getInstructorName(int instructorId) {
        return dbManager.getUserById(instructorId).getUsername();
    }

    public ArrayList<Course> getCourseByTitle(ArrayList<Course> courses, String title) {
        return dbManager.getCourseByTitle(courses, title);
    }
}
