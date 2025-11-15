//YASSER'S TERRITORY
package Controller;
import Model.Instructor;
import Model.JsonDatabaseManager;
import java.util.ArrayList;

public class InstructorController {
    private JsonDatabaseManager dbManager;
    private Instructor currentInstructor;
    //                                    CONSTRUCTOR

    public InstructorController(JsonDatabaseManager dbManager, Instructor currentInstructor) {
        this.dbManager = dbManager;
        this.currentInstructor = currentInstructor;
    }
    //                                    SETTERS AND GETTERS

    public Instructor getCurrentInstructor() {
        return currentInstructor;
    }
    public void setCurrentInstructor(Instructor currentInstructor) {
        this.currentInstructor = currentInstructor;
    }
    public JsonDatabaseManager getDbManager() {
        return dbManager;
    }
    public void setDbManager(JsonDatabaseManager dbManager) {
        this.dbManager = dbManager; }
    //                                         METHODS

    // Course Management
    public boolean createCourse(String title, String description) {

    }
    public boolean editCourse(String courseId, String newTitle, String newDescription) {

    }
    public boolean deleteCourse(String courseId) {

    }
    // Lesson Management
    public boolean addLesson(String courseId, String title, String content, ArrayList<String> resources){

    }
    public boolean updateLesson(String courseId, String lessonId, String title, String content, ArrayList<String> resources){

    }
    public boolean deleteLesson(String courseId, String lessonId){

    }
    // View Enrolled Students
    public ArrayList<String> getEnrolledStudents(String courseId) {

    }
}