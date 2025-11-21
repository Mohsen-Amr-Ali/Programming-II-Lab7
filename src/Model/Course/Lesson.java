//YASSER'S TERRITORY
package Model.Course;

public class Lesson {
    private int lessonId;
    private int courseId;
    private String title;
    private String content;
//                                              CONSTRUCTOR

    public Lesson(int lessonId, String title, String content) {
        this.lessonId = lessonId;
        this.title = title;
        this.content = content;
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
}