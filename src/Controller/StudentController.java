package Controller;

import Model.Course;
import Model.JsonDatabaseManager;
import Model.Lesson;
import Model.Student;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

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

    // course management (all same methods in student class?)

    // lesson access and progress
    public ArrayList<Lesson> getLessonsForCourse(String courseID)
    {
        Course course = dbManager.getCourseById(courseID);

        if (course == null)
        {
            return new ArrayList<>();
        }

        return course.getLessons();
    }

    public boolean recordLessonCompletion(String courseID, String lessonID)
    {
        HashMap<String, ArrayList<String>> progress = currentStudent.getProgress();
        progress.putIfAbsent(courseID, new ArrayList<>());

        if(!progress.get(courseID).contains(lessonID))
        {
            progress.get(courseID).add(lessonID);
            // dbManager.saveUsers(); method is not public idk
            return true;
        }
        return false;
    }


}
