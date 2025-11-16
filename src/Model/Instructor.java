//YASSER'S TERRITORY
package Model;
import java.util.ArrayList;

public class Instructor extends User {
    private ArrayList<Integer> createdCourses;
//                                              CONSTRUCTOR

    public Instructor(ArrayList<Integer> createdCourses) {
        super();
        this.createdCourses = new ArrayList<>();
    }
//                                              GETTERS AND SETTERS

    public ArrayList<Integer> getCreatedCourses() {
        return createdCourses;
    }

    public void setCreatedCourses(ArrayList<Integer> createdCourses) {
        this.createdCourses = createdCourses;
    }
    //                                              METHODS

    public void addCreatedCourse(int courseId) {
        this.createdCourses.add(courseId);
    }

    public void removeCreatedCourse(int courseId) {
        this.createdCourses.remove(courseId);
    }

    public ArrayList<Course> getCourses(JsonDatabaseManager dbManager) {
        ArrayList<Course> courses = new ArrayList<>();
        for (int courseId : createdCourses) {
            Course course = dbManager.getCourseById(courseId);
            if (course != null) {
                courses.add(course);
            }
        }  return courses;
    }
}