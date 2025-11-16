//YASSER'S TERRITORY
//the point of the controller is to act as an intermediary between the view and the model
package Controller;
import Model.Instructor;
import Model.JsonDatabaseManager;
import java.util.ArrayList;
import Model.Course;
import Model.Lesson;

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
        this.dbManager = dbManager;
    }
    //                                         METHODS

    //the methods below will interact with the dbManager to perform CRUD operations on courses and lessons
    // Course Management
    public void createCourse(String title, String description) {
        try {
            Course course = new Course(title, description, currentInstructor.getUserId());
            dbManager.addCourse(course); // DB generates courseId & saves
        } catch (Exception e) {

        }
    }

    public void editCourse(int courseId, String newTitle, String newDescription) {
         dbManager.editCourse(courseId, newTitle, newDescription);
    }

    public void deleteCourse(String courseId) {
         dbManager.deleteCourse(courseId);
    }

    // Lesson Management
    public void addLesson(int courseId, String title, String content) {
        Lesson lesson = new Lesson(courseId, title, content);
         dbManager.addLesson(lesson);
    }

    public void updateLesson(int lessonId, String title, String content) {
         dbManager.updateLesson(lessonId, title, content);
    }

    public void deleteLesson(String courseId, String lessonId) {
         dbManager.deleteLesson(courseId, lessonId);
    }

    // View Enrolled Students
    public ArrayList<String> getEnrolledStudents(String courseId) {
        return dbManager.getEnrolledStudents(courseId);
    }
}