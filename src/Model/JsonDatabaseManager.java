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

    private String usersFile = "Database/users.json";
    private String coursesFile = "Database/courses.json";

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final Random random = new Random();


    //================= Constructor, setters and getters =================//
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

    //================= Load Functions =================//
    private boolean isStudent(int id){
        while(id >= 100) id /=10;
        return id == 90;
    }

    private void loadUsers(){
        try(FileReader reader = new FileReader(usersFile)){
            JsonArray jsonArray = JsonParser.parseReader(reader).getAsJsonArray();

            for (JsonElement userElement : jsonArray) {
                JsonObject userObject = userElement.getAsJsonObject();

                int id = userObject.get("id").getAsInt();


                if (isStudent(id)) {
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
            courses = gson.fromJson(reader, coursesType);
        }catch (IOException e) {
            System.out.println("\nException in JsonDatabaseManager - loadCourses()");
        }
    }

    //================= Save Functions =================//
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

    //================= User Operations =================//
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
        if(isStudent(id)){
            for(Student student : students){
                if(student.getId() == id){
                    return false;
                }
            }
        }else{
            for(Instructor instructor : instructors){
                if(instructor.getId() == id){
                    return false;
                }
            }
        }

        return true;
    }

    private int generateId(String role){
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
        instructors.add(instructor);
        saveUsers();
    }

    public void addUser(Student student){
        students.add(student);
        saveUsers();
    }

}