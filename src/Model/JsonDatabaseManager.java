package Model;

//Instructor IDs start with 10----
//Student IDs start with 90----
//Course IDs start with 70----
//Lesson IDs start with 50----***

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import Model.Course.Course;
import Model.Course.Lesson;
import Model.Course.COURSE_STATUS;
import Model.User.*;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

public class JsonDatabaseManager {
    private static JsonDatabaseManager instance; //conventional name for a singleton instnace, it'll be just one instance for the entire program
    private ArrayList<Student> students = new ArrayList<>();
    private ArrayList<Instructor> instructors = new ArrayList<>();
    private ArrayList<Admin> admins = new ArrayList<>();
    private ArrayList<Course> courses = new ArrayList<>();
    private ArrayList<Course> pendingCourses = new ArrayList<>();
    private ArrayList<Course> approvedCourses = new ArrayList<>();
    private ArrayList<Course> rejectedCourses = new ArrayList<>();

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

    public ArrayList<Course> getPendingCourses() {
        return pendingCourses;
    }

    public ArrayList<Course> getApprovedCourses() {
        return approvedCourses;
    }

    public ArrayList<Course> getRejectedCourses() {
        return rejectedCourses;
    }

    //================= Load Functions =================\\
    private boolean isStudent(int id){
        while(id >= 100) id /=10;
        return id == 90;
    }

    private boolean isInstructor(int id) {
        while (id >= 100)
            id /= 10;
        return id == 10;
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

                USER_ROLE role = USER_ROLE.valueOf(userObject.get("role").getAsString().toUpperCase());

                if (role == USER_ROLE.STUDENT) {
                    Student student = gson.fromJson(userObject, Student.class);
                    students.add(student);
                } else if (role == USER_ROLE.INSTRUCTOR) {
                    Instructor instructor = gson.fromJson(userObject, Instructor.class);
                    instructors.add(instructor);
                } else if (role == USER_ROLE.ADMIN){
                    Admin admin = gson.fromJson(userObject, Admin.class);
                    admins.add(admin);
                }
            }

            System.out.println("\nUsers loaded successfully");
        } catch (IOException e) {
            System.out.println("\nException in JsonDatabaseManager - loadUsers()");
        }

    }

    private void loadCourses(){
        pendingCourses.clear();
        approvedCourses.clear();
        rejectedCourses.clear();
        try(FileReader reader = new FileReader(coursesFile)){
            Type coursesType  = new TypeToken<ArrayList<Course>>(){}.getType();
            ArrayList<Course> loadedCourses = gson.fromJson(reader, coursesType);
            if (loadedCourses != null) {
                courses = loadedCourses;
                for (Course course : loadedCourses) {
                    if (course.getStatus() == COURSE_STATUS.PENDING) {
                        pendingCourses.add(course);
                    } else if (course.getStatus() == COURSE_STATUS.APPROVED) {
                        approvedCourses.add(course);
                    } else if (course.getStatus() == COURSE_STATUS.REJECTED) {
                        rejectedCourses.add(course);
                    }
                }
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
        allUsers.addAll(admins);

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

        if (user == null) {
            for (Admin admin: admins) {
                if (admin.getEmail().equals(email)) {
                    user = admin;
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
        }else if(isInstructor(id)){
            for(Instructor instructor : instructors){
                if(instructor.getId() == id){
                    user = instructor;
                    break;
                }
            }
        }else{
            for(Admin admin : admins){
                if(admin.getId() == id){
                    user = admin;
                    break;
                }
            }
        }

        return user;
    }

    private boolean isUserIdUnique(int id){
        return getUserById(id) == null;
    }

    private int generateUserId(USER_ROLE role){
        int id;

        if (role == USER_ROLE.STUDENT) {
            do {
                id = 900000 + random.nextInt(10000);
            } while (!isUserIdUnique(id));
        } else if (role == USER_ROLE.INSTRUCTOR) {
            do {
                id = 100000 + random.nextInt(10000);
            } while (!isUserIdUnique(id));
        } else {
            do {
                id = 200000 + random.nextInt(10000);
            } while (!isUserIdUnique(id));
        }
        return id;
    }

    public void addUser(Instructor instructor){
        instructor.setId(generateUserId(USER_ROLE.INSTRUCTOR));
        instructors.add(instructor);
        saveUsers();
    }

    public void addUser(Student student){
        student.setId(generateUserId(USER_ROLE.STUDENT));
        students.add(student);
        saveUsers();
    }

    public void addUser(Admin admin) {
        admin.setId(generateUserId(USER_ROLE.ADMIN));
        admins.add(admin);
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
        } else if (updatedUser instanceof Admin) {
            for (int i = 0; i < admins.size(); i++) {
                if (admins.get(i).getId() == updatedUser.getId()) {
                    admins.set(i, (Admin) updatedUser);
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
        // Add to the correct status list
        if (course.getStatus() == COURSE_STATUS.PENDING) {
            pendingCourses.add(course);
        } else if (course.getStatus() == COURSE_STATUS.APPROVED) {
            approvedCourses.add(course);
        } else if (course.getStatus() == COURSE_STATUS.REJECTED) {
            rejectedCourses.add(course);
        }
        saveCourses();
    }

    public void updateCourses(Course updatedCourse) {
        for (int i = 0; i < courses.size(); i++) {
            if (courses.get(i).getCourseId() == updatedCourse.getCourseId()) {
                // Remove from old status list
                COURSE_STATUS oldStatus = courses.get(i).getStatus();
                if (oldStatus == COURSE_STATUS.PENDING) {
                    pendingCourses.remove(courses.get(i));
                } else if (oldStatus == COURSE_STATUS.APPROVED) {
                    approvedCourses.remove(courses.get(i));
                } else if (oldStatus == COURSE_STATUS.REJECTED) {
                    rejectedCourses.remove(courses.get(i));
                }
                // Update course
                courses.set(i, updatedCourse);
                // Add to new status list
                if (updatedCourse.getStatus() == COURSE_STATUS.PENDING) {
                    pendingCourses.add(updatedCourse);
                } else if (updatedCourse.getStatus() == COURSE_STATUS.APPROVED) {
                    approvedCourses.add(updatedCourse);
                } else if (updatedCourse.getStatus() == COURSE_STATUS.REJECTED) {
                    rejectedCourses.add(updatedCourse);
                }
                break;
            }
        }
        saveCourses();
    }

    public void removeCourse(Course removedCourse) {
        courses.remove(removedCourse);

        // Remove from status list
        if (removedCourse.getStatus() == COURSE_STATUS.PENDING) {
            pendingCourses.remove(removedCourse);
        } else if (removedCourse.getStatus() == COURSE_STATUS.APPROVED) {
            approvedCourses.remove(removedCourse);
        } else if (removedCourse.getStatus() == COURSE_STATUS.REJECTED) {
            rejectedCourses.remove(removedCourse);
        }

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
            instructor.getCreatedCourses().remove((Integer) removedCourse.getCourseId());
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

    public ArrayList<Course> getCourseByTitle(COURSE_STATUS status, String title){
        if(status == COURSE_STATUS.APPROVED){
            return getCourseByTitle(approvedCourses,title);
        } else if(status == COURSE_STATUS.PENDING){
            return getCourseByTitle(pendingCourses, title);
        } else if(status == COURSE_STATUS.REJECTED){
            return getCourseByTitle(rejectedCourses, title);
        }

        return new ArrayList<>();
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

    public void changeCourseStatus(int courseId, COURSE_STATUS newStatus) {
        Course course = getCourseById(courseId);
        if (course == null) return;

        COURSE_STATUS oldStatus = course.getStatus();
        if (oldStatus == newStatus) return;

        // Remove from old status list
        if (oldStatus == COURSE_STATUS.PENDING) {
            pendingCourses.remove(course);
        } else if (oldStatus == COURSE_STATUS.APPROVED) {
            approvedCourses.remove(course);
        } else if (oldStatus == COURSE_STATUS.REJECTED) {
            rejectedCourses.remove(course);
        }

        // Set new status and add to new list
        course.setStatus(newStatus);
        if (newStatus == COURSE_STATUS.PENDING) {
            pendingCourses.add(course);
        } else if (newStatus == COURSE_STATUS.APPROVED) {
            approvedCourses.add(course);
        } else if (newStatus == COURSE_STATUS.REJECTED) {
            rejectedCourses.add(course);
        }

        saveCourses();
    }

    public ArrayList<Student> getEnrolledStudents (Course course, ArrayList<Student> students) {
        ArrayList<Student> enrolledStudents = new ArrayList<>();

        if (course == null || students == null)
        {
            return enrolledStudents;
        }

        for (Student student : students) {
            if (student != null && student.isEnrolled(course.getCourseId())) {
                enrolledStudents.add(student);
            }
        }
        return enrolledStudents;
    }
}