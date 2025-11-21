//YASSER'S TERRITORY
//the point of the controller is to act as an intermediary between the view and the model
package Controller;
import Model.User.Instructor;
import Model.JsonDatabaseManager;
import java.util.ArrayList;
import Model.Course.Course;

public class InstructorController {
    private JsonDatabaseManager dbManager;

    public InstructorController() {
        this.dbManager = JsonDatabaseManager.getInstance();
    }

    public ArrayList<Course> getCreatedCourses(int instructorId) {
        Instructor instructor = (Instructor) dbManager.getUserById(instructorId);
        ArrayList<Course> createdCourses = new ArrayList<>();
        if (instructor != null) {
            for (Integer courseId : instructor.getCreatedCourses()) {
                createdCourses.add(dbManager.getCourseById(courseId));
            }
        }
        return createdCourses;
    }
}