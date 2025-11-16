package Model;

import java.util.ArrayList;
import java.util.HashMap;

public class Student extends User{

    private ArrayList<String> enrolledCourses;
    private HashMap<String, ArrayList<String>> progress;

    public Student(int studentID, String username, String email, String passwordHash, String role) {
        super(studentID, username, email, passwordHash, role);
        enrolledCourses = new ArrayList<>();
        progress = new HashMap<>();
    }

    public Student(ArrayList<String> enrolledCourses, HashMap<String, ArrayList<String>> progress, int studentID, String username, String email, String passwordHash, String role) {
        super(studentID, username, email, passwordHash, role);
        this.enrolledCourses = enrolledCourses;
        this.progress = progress;
    }

    public ArrayList<String> getEnrolledCoursesIDs() {
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
        return enrolledCourses.contains(courseID);
    }

    public void markLessonCompleted(String courseID, String lessonID){
        progress.putIfAbsent(courseID, new ArrayList<>());
        if (!progress.get(courseID).contains(lessonID)) {
            progress.get(courseID).add(lessonID);
        }
    }

    public boolean isLessonCompleted(String courseID, String lessonID){
        return progress.containsKey(courseID) && progress.get(courseID).contains(lessonID);
    }

    @Override
    public void showDshboard(){
        System.out.println("Student Dashboard:");
        System.out.println("Enrolled Courses: " + enrolledCourses);
        System.out.println("Progress: " + progress);
    }
}