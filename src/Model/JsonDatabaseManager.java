package Model;

//Instructor IDs start with 10----
//Student IDs start with 90----

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Random;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

public class JsonDatabaseManager {
    private static JsonDatabaseManager instance; //conventional name for a singleton instnace, it'll be just one instance for the entire program
    private ArrayList<Student> students = new ArrayList<>();
    private ArrayList<Instructor> instructors = new ArrayList<>();
    private ArrayList<Course> courses = new ArrayList<>();

    private String usersFile = "y:\\AlexU\\Term 5\\Programming 2\\Programming-II-Lab7\\src\\Database\\users.json";
    private String coursesFile = "y:\\AlexU\\Term 5\\Programming 2\\Programming-II-Lab7\\src\\Database\\courses.json";

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final Random random = new Random();


    //================= Constructor, setters and getters =================\\
    private JsonDatabaseManager(){
        loadUsers();
        loadCourses();
    }

    public void setPaths(String usersFile, String coursesFile){
        this.usersFile = usersFile;
        this.coursesFile = coursesFile;
    }

    public static JsonDatabaseManager getInstance(){
        if(instance == null) instance = new JsonDatabaseManager();
        return instance;
    }

    public ArrayList<Course> getCourses() {
        return courses;
    }

    //================= Load Functions =================\\
    private boolean isStudent(int id){
        while(id >= 100) id /=10;
        return id == 90;
    }

    private void loadUsers(){
        try(FileReader reader = new FileReader(usersFile)){
            JsonArray jsonArray = JsonParser.parseReader(reader).getAsJsonArray();
            
            if (jsonArray == null || jsonArray.size() == 0) {
                System.out.println("\nNo users found in file");
                return;
            }

            for (JsonElement userElement : jsonArray) {
                JsonObject userObject = userElement.getAsJsonObject();

                String role = userObject.get("role").getAsString();


                if (role.equals("student")) {
                    Student student = gson.fromJson(userObject, Student.class);
                    students.add(student);
                } else{
                    Instructor instructor = gson.fromJson(userObject, Instructor.class);
                    instructors.add(instructor);
                }
            }

            System.out.println("\nUsers loaded successfully");
        } catch (IOException e) {
            System.out.println("\nException in JsonDatabaseManager - loadUsers()");
        }

    }

    private void loadCourses(){
        try(FileReader reader = new FileReader(coursesFile)){
            Type coursesType  = new TypeToken<ArrayList<Course>>(){}.getType();
            ArrayList<Course> loadedCourses = gson.fromJson(reader, coursesType);
            if (loadedCourses != null) {
                courses = loadedCourses;
            }
            System.out.println("\nCourses loaded successfully");
        }catch (IOException e) {
            System.out.println("\nException in JsonDatabaseManager - loadCourses()");
        }
    }

    //================= Save Functions =================\\
    private void saveUsers(){
        ArrayList<User> allUsers = new ArrayList<>();
        allUsers.addAll(students);
        allUsers.addAll(instructors);

        try(FileWriter writer = new FileWriter(usersFile)){
            gson.toJson(allUsers, writer);
            System.out.println("\nUsers saved successfully.");
        } catch (IOException e) {
            System.out.println("\nException in JsonDatabaseManager - saveUsers()");
        }
    }

    private void saveCourses(){
        try(FileWriter writer = new FileWriter(coursesFile)){
            gson.toJson(courses, writer);
            System.out.println("\nCourses saved successfully.");
        } catch (IOException e) {
            System.out.println("\nException in JsonDatabaseManager - saveCourses()");
        }
    }

    //================= User Operations =================\\
    public User getUserByEmail(String email){
        User user = null;

        for(Instructor instructor : instructors){
            if(instructor.getEmail().equals(email)){
                user = instructor;
                break;
            }
        }

        if(user == null){
            for(Student student : students){
                if(student.getEmail().equals(email)){
                    user = student;
                    break;
                }
            }
        }

        return user;
    }

    public User getUserById(int id){
        User user = null;

        if(isStudent(id)){
            for(Student student : students){
                if(student.getId() == id){
                    user = student;
                    break;
                }
            }
        } else{
            for(Instructor instructor : instructors){
                if(instructor.getId() == id){
                    user = instructor;
                    break;
                }
            }
        }

        return user;
    }

    private boolean isUserIdUnique(int id){
        return getUserById(id) == null;
    }

    private int generateUserId(String role){
        int id;

        if(role.equals("student")){

            do {
                id = 900000 + random.nextInt(10000);
            } while(!isUserIdUnique(id));

        }else{

            do {
                id = 100000 + random.nextInt(10000);
            } while(!isUserIdUnique(id));

        }

        return id;
    }

    public void addUser(Instructor instructor){
        instructor.setId(generateUserId("instructor"));
        instructors.add(instructor);
        saveUsers();
    }

    public void addUser(Student student){
        student.setId(generateUserId("student"));
        students.add(student);
        saveUsers();
    }

    public void updateUser(User updatedUser) {
        if (updatedUser instanceof Student) {
            for (int i = 0; i < students.size(); i++) {
                if (students.get(i).getId() == updatedUser.getId()) {
                    students.set(i, (Student) updatedUser);
                    break;
                }
            }
        } else if (updatedUser instanceof Instructor) {
            for (int i = 0; i < instructors.size(); i++) {
                if (instructors.get(i).getId() == updatedUser.getId()) {
                    instructors.set(i, (Instructor) updatedUser);
                    break;
                }
            }
        }
        saveUsers();
    }

    //================= Course Operations =================\\
    public Course getCourseById(int courseId){
        Course retCourse = null;
        for(Course course : courses){
            if(course.getCourseId() == courseId){
                retCourse = course;
                break;
            }
        }

        return retCourse;
    }

    private boolean isCourseIdUnique(int id){
        return getCourseById(id) == null;
    }

    private int generateCourseId(){
        int id;
        do {
            id = 700000 + random.nextInt(10000);
        } while(!isCourseIdUnique(id));

        return id;
    }

    public void addCourse(Course course){
        course.setCourseId(generateCourseId());
        courses.add(course);
        saveCourses();
    }

    public void updateCourses(Course updatedCourse){
        for (int i = 0; i < courses.size(); i++) {
            if (courses.get(i).getCourseId() == updatedCourse.getCourseId()) {
                courses.set(i, updatedCourse);
            }
        }
        saveCourses();
    }

    public void removeCourse(Course removedCourse){
        courses.remove(removedCourse);

        for (Integer studentId : removedCourse.getStudents()) {
            User user = getUserById(studentId);
            if (user instanceof Student) {
                Student student = (Student) user;
                student.getEnrolledCoursesIDs().remove((Integer) removedCourse.getCourseId());
                student.getProgress().remove((Integer) removedCourse.getCourseId());
            }
        }

        User instructorUser = getUserById(removedCourse.getInstructorId());
        if (instructorUser instanceof Instructor) {
            Instructor instructor = (Instructor) instructorUser;
            instructor.getCreatedCourses().remove((Integer)removedCourse.getCourseId());
        }

        saveCourses();
        saveUsers();
    }

    public ArrayList<Course> getCourseByTitle(String title) {
        ArrayList<Course> results = new ArrayList<>();
        for (Course course : courses) {
            if (course.getTitle().toLowerCase().contains(title))
                results.add(course);
        }
        return results;
    }

    public ArrayList<Course> getCourseByTitle(ArrayList<Course> courses, String title) {
        ArrayList<Course> results = new ArrayList<>();
        for (Course course : courses) {
            if (course.getTitle().toLowerCase().contains(title))
                results.add(course);
        }
        return results;
    }

    public void removeLesson(Lesson removedLesson){
        Course course = getCourseById(removedLesson.getCourseId());
        if (course == null) return;
        
        course.removeLesson(removedLesson.getLessonId());
        
        for (Student student : students) {
            if (student.isEnrolled(course.getCourseId())) {
                ArrayList<Integer> completedLessons = student.getCompletedLessons(course.getCourseId());
                if (completedLessons != null) {
                    completedLessons.remove((Integer)removedLesson.getLessonId());
                }
            }
        }
        
        saveCourses();
        saveUsers();
    }



}