package Model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Model.Course.Quiz;
import Model.Course.Certificate;
import Model.Course.QuizResult;

import java.util.Random;

public class Student extends User {

    private static final Random random = new Random();
    private ArrayList<Integer> enrolledCourses;
    private HashMap<Integer, ArrayList<Integer>> progress; // CourseID -> List of completed LessonIDs
    private ArrayList<Certificate> certificates;
    private HashMap<Integer, ArrayList<QuizResult>> quizResults; // LessonID -> List of attempts

    public Student(String username, String email, String passwordHash, USER_ROLE role) {
        super(username, email, passwordHash, role);
        this.enrolledCourses = new ArrayList<>();
        this.progress = new HashMap<>();
        this.certificates = new ArrayList<>();
        this.quizResults = new HashMap<>();
    }

    public Student(ArrayList<Integer> enrolledCourses, HashMap<Integer, ArrayList<Integer>> progress, int studentID, String username, String email, String passwordHash, USER_ROLE role) {
        super(studentID, username, email, passwordHash, role);
        this.enrolledCourses = enrolledCourses != null ? enrolledCourses : new ArrayList<>();
        this.progress = progress != null ? progress : new HashMap<>();
        this.certificates = new ArrayList<>();
        this.quizResults = new HashMap<>();
    }

    // --- Getters & Setters ---

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

    public ArrayList<Certificate> getCertificates() {
        return certificates;
    }

    public void setCertificates(ArrayList<Certificate> certificates) {
        this.certificates = certificates != null ? certificates : new ArrayList<>();
    }

    public HashMap<Integer, ArrayList<QuizResult>> getQuizResultsMap() {
        return quizResults;
    }

    public void setQuizResultsMap(HashMap<Integer, ArrayList<QuizResult>> quizResults) {
        this.quizResults = quizResults != null ? quizResults : new HashMap<>();
    }

    // --- Core Methods ---

    public void enrollInCourse(int courseID){
        if (!enrolledCourses.contains(courseID)) {
            enrolledCourses.add(courseID);
        }
    }

    public void dropCourse(int courseID){
        enrolledCourses.remove((Integer) courseID);
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
            progress.get(courseID).remove((Integer) lessonID);
        }
    }

    public ArrayList<Integer> getCompletedLessons(int courseID){
        return progress.getOrDefault(courseID, new ArrayList<>());
    }

    public boolean isLessonCompleted(int courseID, int lessonID){
        return progress.containsKey(courseID) && progress.get(courseID).contains(lessonID);
    }

    // --- Quiz Methods ---

    /**
     * Records a quiz attempt for a specific lesson.
     * Use this method instead of addQuizScore to track full attempt history.
     * @param lessonId The ID of the lesson taken.
     * @param quizResult The result object containing score, date, and pass status.
     */
    public void addQuizAttempt(int lessonId, QuizResult quizResult) {
        if (quizResults == null) quizResults = new HashMap<>();
        quizResults.putIfAbsent(lessonId, new ArrayList<>());
        quizResults.get(lessonId).add(quizResult);
    }

    public ArrayList<QuizResult> getQuizResults() {
        ArrayList<QuizResult> results = new ArrayList<>();
        if (quizResults != null) {
            for (ArrayList<QuizResult> result : quizResults.values()) {
                results.addAll(result);
            }
        }
        return results;
    }

    public QuizResult getLatestQuizResult(Quiz quiz) {
        // Helper to get the latest attempt if needed, but logic should likely reside in controller
        return null;
    }

    /**
     * Checks if the student has passed the quiz for a given lesson.
     * @param lessonId The ID of the lesson associated with the quiz.
     * @param threshold The passing score percentage (e.g., 60.0).
     * @return true if any attempt for this lesson has a passing score.
     */
    public boolean hasPassedQuiz(int lessonId, double threshold) {
        if (quizResults == null || !quizResults.containsKey(lessonId)) {
            return false;
        }

        List<QuizResult> attempts = quizResults.get(lessonId);
        for (QuizResult result : attempts) {
            // Result should store pass status, but we can double check against threshold
            // or rely on result.isPassed() if we trust it was set correctly.
            // Here we calculate percentage to be safe.
            double percentage = (double) result.getScore() / result.getMaxScore() * 100.0;
            if (percentage >= threshold) {
                return true;
            }
        }
        return false;
    }

    // --- Certificate Methods ---

    private boolean isCertificateIdUnique(int certificateId) {
        if (certificates == null) return true;
        for (Certificate cert : certificates) {
            if (cert.getCertificateId() == certificateId) {
                return false;
            }
        }
        return true;
    }

    public int generateCertificateId(){
        int baseId = this.id % 10000;
        int newId;
        do {
            newId = 400000000 + (baseId * 1000) + random.nextInt(1000);
        } while (!isCertificateIdUnique(newId));
        return newId;
    }
}