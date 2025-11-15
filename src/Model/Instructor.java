//YASSER'S TERRITORY
package Model;
import java.util.ArrayList;

public class Instructor extends User {
    private ArrayList<String> createdCourses;
//                                              CONSTRUCTOR

    public Instructor() {
        super();
        this.createdCourses = new ArrayList<>();
    }
//                                              GETTERS AND SETTERS

    public ArrayList<String> getCreatedCourses() {
        return createdCourses;
    }

    public void setCreatedCourses(ArrayList<String> createdCourses) {
        this.createdCourses = createdCourses;
    }
    //                                              METHODS

    public void addCreatedCourse(String courseId) {
        this.createdCourses.add(courseId);
    }

    public void removeCreatedCourse(String courseId) {
        this.createdCourses.remove(courseId);
    }
    //LESA MOHSEN MA3AMALSH EL GETCOURSEBYID
    public ArrayList<Course> getCourses(JsonDatabaseManager dbManager) {
        ArrayList<Course> courses = new ArrayList<>();
      /*  for (String courseId : createdCourses) {
            Course course = dbManager.getCourseById(courseId);
            if (course != null) {
                courses.add(course);
            }
        } */ return courses;
    }
}