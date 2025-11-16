package Model;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

public class Student extends User{

    private ArrayList<String> enrolledCourses;
    private HashMap<String, ArrayList<String>> progress;

    private int studentID;
    private String role;
    private String username;
    private String email;
    private String passwordHash;

    Gson gson = new Gson();

    public Student(int studentID, String role, String username, String email, String passwordHash) {
            super(studentID, role, username, username, email, passwordHash);
            this.enrolledCourses = new ArrayList<>();
            this.progress = new HashMap<>();
    }

    public ArrayList<String> getEnrolledCourses() {
        return enrolledCourses;
    }

    public void setEnrolledCourses(ArrayList<String> enrolledCourses) {
        this.enrolledCourses = enrolledCourses;
    }

    public HashMap<String, ArrayList<String>> getProgress() {
        return progress;
    }

    public void setProgress(HashMap<String, ArrayList<String>> progress) {
        this.progress = progress;
    }

    public void enroll(String courseID){
        if (!enrolledCourses.contains(courseID)) {
            enrolledCourses.add(courseID);
        }
    }

    public void unenroll(String courseID){
        if(enrolledCourses.contains(courseID))
        {
            enrolledCourses.remove(courseID);
        }
    }

    public boolean isEnrolled(String courseID){
        if (enrolledCourses.contains(courseID))
        {
            return true;
        }
        else
            return false;
    }
}