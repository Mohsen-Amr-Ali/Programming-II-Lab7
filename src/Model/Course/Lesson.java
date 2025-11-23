package Model.Course;

public class Lesson {
    private int lessonId;
    private int courseId; // Can be kept for reverse lookup or consistency
    private String title;
    private String content; // Path to binary file
    private Quiz quiz; // Optional: Null if no quiz exists

    // Constructor WITHOUT Quiz (For initial creation)
    public Lesson(int lessonId, int courseId, String title, String content) {
        this.lessonId = lessonId;
        this.courseId = courseId;
        this.title = title;
        this.content = content;
        this.quiz = null; // Default to no quiz
    }

    // Full Constructor (if loading from DB with a quiz)
    public Lesson(int lessonId, int courseId, String title, String content, Quiz quiz) {
        this.lessonId = lessonId;
        this.courseId = courseId;
        this.title = title;
        this.content = content;
        this.quiz = quiz;
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getLessonId() {
        return lessonId;
    }

    public void setLessonId(int lessonId) {
        this.lessonId = lessonId;
    }

    public int getCourseId() {
        return courseId;
    }

    // Quiz Getter/Setter
    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    /**
     * Generates the deterministic Quiz ID for this lesson.
     * Logic: Replaces the leading '50' of the Lesson ID with '40'.
     * Lesson ID format: 50xxxxxxx
     * Quiz ID format:   40xxxxxxx
     * @return The unique Quiz ID associated with this lesson.
     */
    public int getAssociatedQuizId() {
        // Assuming Lesson ID is 9 digits starting with 50
        // We want to take the last 7 digits (modulo 10,000,000 is safer if random part is large,
        // but based on your structure: 50 + 4 digits + 3 digits = 9 digits total.
        // 500,000,000 range.

        int idWithoutPrefix = this.lessonId % 10000000; // Gets the last 7 digits
        return 40000000 + idWithoutPrefix;
    }
}