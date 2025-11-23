//YASSER'S TERRITORY
package Model.Course;

public class Lesson {
    private int lessonId;
    private int courseId;
    private String title;
    private String content;
    private Quiz quiz;
//                                              CONSTRUCTOR

    public Lesson(int lessonId, int courseId, String title, String content, Quiz quiz) {
        this.lessonId = lessonId;
        this.courseId = courseId;
        this.title = title;
        this.content = content;
        this.quiz = quiz;
    }
//                                              GETTERS AND SETTERS

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

    public int getCourseId(){
        return courseId;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }
}