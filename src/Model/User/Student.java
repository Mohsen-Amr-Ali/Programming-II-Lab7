package Model.User;

import java.util.ArrayList;
import java.util.HashMap;
import util.Certificate;
import util.QuizResult;

import java.util.Random;


public class Student extends User {

    private static final Random random = new Random();
    private ArrayList<Integer> enrolledCourses;
    private HashMap<Integer, ArrayList<Integer>> progress; //first integer is courseID, second integer is list of completed lessonIDs
    private ArrayList<Certificate> certificates = new ArrayList<>();
    private HashMap<Integer, ArrayList<QuizResult>> quizResults;

    public Student(String username, String email, String passwordHash, USER_ROLE role) {
        super(username, email, passwordHash, role);
        enrolledCourses = new ArrayList<>();
        progress = new HashMap<>();
    }

    public Student(ArrayList<Integer> enrolledCourses, HashMap<Integer, ArrayList<Integer>> progress, int studentID, String username, String email, String passwordHash, USER_ROLE role) {
        super(studentID, username, email, passwordHash, role);
        this.enrolledCourses = enrolledCourses != null ? enrolledCourses : new ArrayList<>();
        this.progress = progress != null ? progress : new HashMap<>();
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

    public void enrollInCourse(Integer courseID) {
        if (!enrolledCourses.contains(courseID)) {
            enrolledCourses.add(courseID);
        }
    }

    public void dropCourse(Integer courseID) {
        enrolledCourses.remove(courseID);
        progress.remove(courseID);
    }

    public boolean isEnrolled(Integer courseID) {
        return enrolledCourses.contains(courseID);
    }

    public void addCompletedLesson(Integer courseID, Integer lessonID) {
        progress.putIfAbsent(courseID, new ArrayList<>());
        if (!progress.get(courseID).contains(lessonID)) {
            progress.get(courseID).add(lessonID);
        }
    }

    public void removeCompletedLesson(Integer courseID, Integer lessonID) {
        if (progress.containsKey(courseID)) {
            progress.get(courseID).remove(lessonID);
        }
    }

    public ArrayList<Integer> getCompletedLessons(int courseID) {
        // Always return the actual reference from the HashMap, or an empty list if not found
        return progress.getOrDefault(courseID, new ArrayList<>());
    }


    public boolean isLessonCompleted(Integer courseID, Integer lessonID) {
        return progress.containsKey(courseID) && progress.get(courseID).contains(lessonID);
    }

    public ArrayList<Certificate> getCertificates() {
        return certificates;
    }

    public void setCertificates(ArrayList<Certificate> certificates) {
        this.certificates = certificates;
    }

    private boolean isCertificateIdUnique(int certificateId) {
        for (Certificate cert : certificates) {
            if (cert.getCertificateId() == certificateId) {
                return false;
            }
        }
        return true;
    }
    public int generateCertificateId(){
        id = this.id % 10000;
        int id1;

        do {
            id1 = 400000000 + (id * 1000) + random.nextInt(1000);

        } while (!isCertificateIdUnique(id));

        return id1; }

    public void addQuizAttempt (int lessonId,  QuizResult quizResult) {
        quizResults.putIfAbsent(lessonId, new ArrayList<>());
        quizResults.get(lessonId).add(quizResult);
    }

    public ArrayList<QuizResult> getQuizResults() {
        ArrayList<QuizResult> results = new ArrayList<>();
        for (ArrayList<QuizResult> result: quizResults.values()) {
            results.addAll(result);
        }
        return results;
    }
}