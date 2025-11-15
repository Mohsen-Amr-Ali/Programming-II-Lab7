package Model;

//Instructor IDs start with 10----
//Student IDs start with 90----

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
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
    private void loadUsers(){
        try{
            FileReader reader = new FileReader(usersFile);
            User[] users  = gson.fromJson(reader, User[].class);

            for(User user : users){
                if(user.isInstructor()) instructors.add(user);
                else students.add(user);
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    private void loadCourses(){

    }

    //================= Save Functions =================//
    private void saveUsers(){
        try(FileWriter writer = new FileWriter(usersFile)){
            gson.toJson(students, writer);
            gson.toJson(instructors, writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveCourses(){
        try(FileWriter writer = new FileWriter(coursesFile)){
            gson.toJson(courses, writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}