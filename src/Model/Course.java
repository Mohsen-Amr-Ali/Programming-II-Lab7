package Model; ////YASSER'S TERRITORY
import java.util.ArrayList;

public class Course {
    private int courseId;
    private String title;
    private String description;
    private int instructorId;
    private ArrayList<Lesson> lessons;
    private ArrayList<String> students;
//                                              CONSTRUCTOR

    public Course(int courseId, String title, String description, int instructorId) {
        this.courseId = courseId;
        this.title = title;
        this.description = description;
        this.instructorId = instructorId;
        this.lessons = new ArrayList<>();
        this.students = new ArrayList<>();
    }
//                                              GETTERS AND SETTERS

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(int instructorId) {
        this.instructorId = instructorId;
    }

    public ArrayList<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(ArrayList<Lesson> lessons) {
        this.lessons = lessons;
    }
    public ArrayList<String> getStudents() {
        return students;
    }
    public void setStudents(ArrayList<String> students) {
        this.students = students;
    }

    //                                              METHODS

    public void addLesson(Lesson lesson) {
        this.lessons.add(lesson);
    }
    public void removeLesson(String lessonId) {
        this.lessons.removeIf(lesson -> lesson.getLessonId().equals(lessonId));
    }
    public Lesson getLessonById(String lessonId) {
        for (Lesson lesson : lessons) {
            if (lesson.getLessonId().equals(lessonId)) {
                return lesson;
            }
        }
        return null;
    }
    public boolean updateLesson(Lesson updatedLesson) {
        for (int i = 0; i < lessons.size(); i++) {
            if (lessons.get(i).getLessonId().equals(updatedLesson.getLessonId())) {
                lessons.set(i, updatedLesson);
                return true;
            }
        }
        return false;
    }
    public void dropStudent(String studentId) {
        this.students.remove(studentId);
    }
    public void enrollStudent(String studentId) {
        this.students.add(studentId);
    }

}