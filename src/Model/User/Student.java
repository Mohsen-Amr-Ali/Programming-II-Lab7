package Model.User;

import java.util.ArrayList;
import java.util.HashMap;

public class Student extends User{

    private HashMap<Integer, ArrayList<Double>> quizScores; // Integer is quizID, Double is List of scores from attempts
    private ArrayList<Integer> enrolledCourses;
    private HashMap<Integer, ArrayList<Integer>> progress; //first integer is courseID, second integer is list of completed lessonIDs

    public Student(String username, String email, String passwordHash, USER_ROLE role) {
        super(username, email, passwordHash, role);
        enrolledCourses = new ArrayList<>();
        progress = new HashMap<>();
        quizScores = new HashMap<>();
    }

    public Student(ArrayList<Integer> enrolledCourses, HashMap<Integer, ArrayList<Integer>> progress, int studentID, String username, String email, String passwordHash, HashMap<Integer, ArrayList<Double>> quizScores, USER_ROLE role) {
        super(studentID, username, email, passwordHash, role);
        this.enrolledCourses = enrolledCourses != null ? enrolledCourses : new ArrayList<>();
        this.progress = progress != null ? progress : new HashMap<>();
        this.quizScores = quizScores != null ? quizScores : new HashMap<>();
    }

    public ArrayList<Integer> getEnrolledCoursesIDs() {
        return enrolledCourses;
    }

    public void setEnrolledCourses(ArrayList<Integer> enrolledCourses) {
        this.enrolledCourses = enrolledCourses != null ? enrolledCourses : new ArrayList<>();
    }

    public HashMap<Integer, ArrayList<Integer>> getProgress() {
        return progress;
    }

    public void setProgress(HashMap<Integer, ArrayList<Integer>> progress) {
        this.progress = progress != null ? progress : new HashMap<>();
    }

    public void enrollInCourse(int courseID){
        if (!enrolledCourses.contains(courseID)) {
            enrolledCourses.add(courseID);
        }
    }

    public void dropCourse(int courseID){
        enrolledCourses.remove(courseID);
        progress.remove(courseID);
    }

    public boolean isEnrolled(int courseID){
        return enrolledCourses.contains(courseID);
    }

    public void addCompletedLesson(int courseID, int lessonID){
        progress.putIfAbsent(courseID, new ArrayList<>());
        if (!progress.get(courseID).contains(lessonID)) {
            progress.get(courseID).add(lessonID);
        }
    }

    public void removeCompletedLesson(int courseID, int lessonID){
        if (progress.containsKey(courseID)) {
            progress.get(courseID).remove(lessonID);
        }
    }

    public ArrayList<Integer> getCompletedLessons(int courseID){
        // Always return the actual reference from the HashMap, or an empty list if not found
        return progress.getOrDefault(courseID, new ArrayList<>());
    }


    public boolean isLessonCompleted(int courseID, int lessonID){
        return progress.containsKey(courseID) && progress.get(courseID).contains(lessonID);
    }

    public void addQuizScore(int lessonID, double score)
    {
        quizScores.putIfAbsent(lessonID, new ArrayList<>());
        quizScores.get(lessonID).add(score);
    }

    public ArrayList<Double> getQuizAttempts(int lessonID) {
        return quizScores.getOrDefault(lessonID, new ArrayList<>());
    }

    public double getLatestQuizScore(int lessonID) {
        ArrayList<Double> attempts = getQuizAttempts(lessonID);
        if (attempts.isEmpty()) {
            return -1.0; // No attempts made
        }
        return attempts.getLast();
    }

    public boolean hasPassedQuiz(int lessonID, double passMark) {
        double latestScore = getLatestQuizScore(lessonID);
        return latestScore >= passMark;
    }

}