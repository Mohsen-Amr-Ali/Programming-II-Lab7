package Controller;

import Model.Course;
import Model.JsonDatabaseManager;
import Model.Student;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;

public class StudentController {

    private JsonDatabaseManager dbManager;
    private Student currentStudent;

    private String usersFile = "Database/users.json";
    private String coursesFile = "Database/courses.json";

    Gson gson = new Gson();

    public StudentController(Student currentStudent) {
        this.dbManager = JsonDatabaseManager.getInstance();
        this.currentStudent = currentStudent;
    }

    public JsonDatabaseManager getDbManager() {
        return dbManager;
    }

    public Student getCurrentStudent() {
        return currentStudent;
    }

    public void setCurrentStudent(Student currentStudent) {
        this.currentStudent = currentStudent;
    }

    // course management

    public ArrayList<Course> getAvailableCourses(){

        ArrayList<Course> allCourses = dbManager.getCourses();
        ArrayList<Course> enrolledCourses = currentStudent.getEnrolledCourses();
        ArrayList<Course> availableCourses = new ArrayList<>();
        HashSet<String> enrolledCourseIds = new HashSet<>();
        for (Course c : enrolledCourses) {
            enrolledCourseIds.add(c.getCourseId());
        }
        for (Course c : allCourses) {
            if (!enrolledCourseIds.contains(c.getCourseId())) {
                availableCourses.add(c);
            }
        }
        return availableCourses;
    }
}
