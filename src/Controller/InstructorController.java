//YASSER'S TERRITORY
package Controller;
import Model.Instructor;
import Model.JsonDatabaseManager;

public class InstructorController {
    // Controller logic for Instructor
    private JsonDatabaseManager dbManager;
    private Instructor currentInstructor;
    public InstructorController(JsonDatabaseManager dbManager, Instructor currentInstructor) {
        this.dbManager = dbManager;
        this.currentInstructor = currentInstructor;
    }
}