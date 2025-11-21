package Model.Course; ////YASSER'S TERRITORY
import java.util.ArrayList;
import java.util.Random;

public class Course {
    private int courseId;
    private String title;
    private String description;
    private int instructorId;
    private ArrayList<Lesson> lessons;
    private ArrayList<Integer> students;
    private String imagePath;
    private COURSE_STATUS status;

    private static final Random random = new Random();
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
        this.lessons = lessons != null ? lessons : new ArrayList<>();
    }
    public ArrayList<Integer> getStudents() {
        return students;
    }
    public void setStudents(ArrayList<Integer> students) {
        this.students = students != null ? students : new ArrayList<>();
    }

    public COURSE_STATUS getStatus(){
        return status;
    }

    public void setStatus(COURSE_STATUS status){
        this.status = status;
    }

    //                                              METHODS
    
    private int generateLessonId(){
        courseId = this.courseId % 10000;
        int id;

        do {
            id = 500000000 + (courseId * 1000) + random.nextInt(1000);
        } while (!isLessonIdUnique(id));

        return id;
    }

    private boolean isLessonIdUnique(int lessonId) {
        if (lessons == null) return true;
        for (Lesson lesson : lessons) {
            if (lesson.getLessonId() == lessonId) {
                return false;
            }
        }
        return true;
    }

    public void addLesson(Lesson lesson) {
        lesson.setLessonId(generateLessonId());
        this.lessons.add(lesson);
    }

    public void removeLesson(int lessonId) {
        this.lessons.removeIf(lesson -> lesson.getLessonId() == lessonId);
    }

    public Lesson getLessonById(int lessonId) {
        for (Lesson lesson : lessons) {
            if (lesson.getLessonId() == lessonId) {
                return lesson;
            }
        }
        return null;
    }

    public boolean updateLesson(Lesson updatedLesson) {
        for (int i = 0; i < lessons.size(); i++) {
            if (lessons.get(i).getLessonId() == updatedLesson.getLessonId()) {
                lessons.set(i, updatedLesson);
                return true;
            }
        }
        return false;
    }
    
    public void dropStudent(Integer studentId) {
        this.students.remove(studentId);
    }
    public void enrollStudent(Integer studentId) {
        this.students.add(studentId);
    }
}