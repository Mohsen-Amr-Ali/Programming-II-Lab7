package Model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Model.Course.Course;
import Model.Course.Lesson;
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
        // Helper to get the latest attempt if needed
        // This requires mapping logic (QuizID vs LessonID).
        // Assuming LessonID key for simplicity as per addQuizAttempt.
        // This method signature might be tricky without LessonID context.
        return null;
    }

    /**
     * Helper to get result by Lesson ID directly.
     */
    public QuizResult getLatestQuizResultByLessonId(int lessonId) {
        if (quizResults == null || !quizResults.containsKey(lessonId)) return null;
        List<QuizResult> attempts = quizResults.get(lessonId);
        if (attempts.isEmpty()) return null;
        return attempts.get(attempts.size() - 1); // Return last attempt
    }

    public boolean hasPassedQuiz(int lessonId, double threshold) {
        if (quizResults == null || !quizResults.containsKey(lessonId)) {
            return false;
        }

        List<QuizResult> attempts = quizResults.get(lessonId);
        for (QuizResult result : attempts) {
            double percentage = (double) result.getScore() / result.getMaxScore() * 100.0;
            if (percentage >= threshold) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the student has completed all requirements for a course.
     * A course is complete if all lessons with quizzes have been passed.
     */
    public boolean isCourseComplete(Course course) {
        if (course == null) return false;

        for (Lesson lesson : course.getLessons()) {
            // If lesson has a quiz, it MUST be passed
            if (lesson.getQuiz() != null) {
                if (!hasPassedQuiz(lesson.getLessonId(), lesson.getQuiz().getPassThreshold())) {
                    return false;
                }
            } else {
                // If no quiz, just check if marked complete (optional, usually we just care about quizzes for certs)
                // But for 100% progress visually, we check manual completion too.
                if (!isLessonCompleted(course.getCourseId(), lesson.getLessonId())) {
                    return false;
                }
            }
        }
        return true;
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