package Controller;

import Model.Instructor;
import Model.Student;
import Model.User;
import View.LoginPanel;

public class MainController {
    private LoginController loginController;
    private StudentController studentController;
    private InstructorController instructorController;
    private final LoginPanel loginPanel;

    public MainController(LoginPanel loginPanel, LoginController loginController, StudentController studentController, InstructorController instructorController) {
        this.loginPanel = loginPanel;
        this.loginController = loginController;
        this.instructorController = instructorController;
        this.studentController = studentController;
    }
    public void login(String emailOrId, String password)
    {
        User user = loginController.loginUser(emailOrId, password);
        if (user instanceof Student)
        {
            studentController.showDashboard();
        }
        if (user instanceof Instructor)
        {
            instructorController.showDashboard();
        }
    }
    public void signup(String username, String email, String password, String repeatedPassword)
    {
        User user = loginController.registerUser(username, email, password, repeatedPassword);
        if (user != null) {
            // After successful registration, automatically log in
            if (user instanceof Student) {
                studentController.showDashboard();
            } else if (user instanceof Instructor) {
                instructorController.showDashboard();
            }
        }
    }

}
