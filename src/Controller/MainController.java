package Controller;

import Model.JsonDatabaseManager;
import Model.Student;
import Model.User;
import View.LoginAndSignupFrame;
import View.LoginPanel;
import View.SignupPanel;
import View.StudentDashboardFrame;
import View.StyledComponents.SOptionPane;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class MainController {
    private LoginController loginController;
    private LoginAndSignupFrame loginFrame;
    private JsonDatabaseManager dbManager;

    public MainController() {
        this.dbManager = JsonDatabaseManager.getInstance();
    }

    public void start() {
        // Create the login/signup frame
        loginFrame = new LoginAndSignupFrame(this);
        
        // Create login and signup panels
        LoginPanel loginPanel = loginFrame.getLoginPanel();
        SignupPanel signupPanel = loginFrame.getSignupPanel();
        
        // Create login controller
        loginController = new LoginController(dbManager, loginPanel, signupPanel);
        
        // Show the login frame
        loginFrame.setVisible(true);
    }

    public void login(String emailOrId, String password) {
        User user = loginController.loginUser(emailOrId, password);
        if (user instanceof Student) {
            // Close login frame and open student dashboard
            loginFrame.dispose();
            Student student = (Student) user;
            StudentDashboardFrame dashboard = new StudentDashboardFrame(student);
            dashboard.setLogoutListener(() -> logout(dashboard));
            dashboard.setVisible(true);
        } else if (user != null) {
            // For now, show a message for instructors
            SOptionPane.showMessageDialog(loginFrame, 
                "Instructor dashboard is not yet implemented.", 
                "Coming Soon", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void signup(String username, String email, String password, String repeatedPassword) {
        User user = loginController.registerUser(username, email, password, repeatedPassword);
        if (user != null) {
            // After successful registration, automatically log in
            if (user instanceof Student) {
                loginFrame.dispose();
                Student student = (Student) user;
                StudentDashboardFrame dashboard = new StudentDashboardFrame(student);
                dashboard.setLogoutListener(() -> logout(dashboard));
                dashboard.setVisible(true);
            } else {
                // For now, show a message for instructors
                SOptionPane.showMessageDialog(loginFrame, 
                    "Instructor dashboard is not yet implemented.", 
                    "Coming Soon", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    public void logout(StudentDashboardFrame dashboard) {
        // Close the dashboard
        dashboard.dispose();
        
        // Create and show a new login frame
        start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainController controller = new MainController();
            controller.start();
        });
    }
}
