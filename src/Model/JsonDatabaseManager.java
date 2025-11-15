package Model;

//Instructor IDs start with 10----
//Student IDs start with 90----

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import com.google.gson.Gson;

public class JsonDatabaseManager {
    private static JsonDatabaseManager instance; //conventional name for a singleton instnace, it'll be just one instance for the entire program
    private ArrayList<Student> students = new ArrayList<Student>();
    private ArrayList<Instructor> instructors = new ArrayList<Instructor>()
    private ArrayList<Course> courses = new ArrayList<Course>();

    private String usersFile = "Database/users.json";
    private String coursesFile = "Database/courses.json";

    private static final Gson gson = new Gson();

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

    private void loadUsers(){
        User user;
        try{
            FileReader reader = new FileReader(usersFile);
            user  = gson.fromJson(reader, User.class);

            if(user.isInstructor()) instructors.add(user);

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    private void loadCourses(){

    }

}