//YASSER'S TERRITORY
package Model;
import java.util.ArrayList;

public class Instructor extends User {
    private ArrayList<String> createdCourses;

    public Instructor() {
        super();
        this.createdCourses = new ArrayList<>();
    }

    public ArrayList<String> getCreatedCourses() {
        return createdCourses;
    }

    public void setCreatedCourses(ArrayList<String> createdCourses) {
        this.createdCourses = createdCourses;
    }

    public void addCreatedCourse(String courseId) {
        this.createdCourses.add(courseId);
    }

    public void removeCreatedCourse(String courseId) {
        this.createdCourses.remove(courseId);
    }
}