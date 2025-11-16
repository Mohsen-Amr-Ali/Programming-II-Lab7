package Controller;

import Model.Instructor;
import Model.JsonDatabaseManager;
import Model.Student;
import Model.User;
import View.LoginPanel;
import View.SignupPanel;
import util.PasswordHasher;
import util.Validator;

public class LoginController {
    private JsonDatabaseManager db;
    private User loggedInUser;
    private final LoginPanel loginPanel;
    private final SignupPanel signupPanel;

    public LoginController(JsonDatabaseManager db, LoginPanel loginPanel, SignupPanel signupPanel) {
        this.db = db;
        this.loginPanel = loginPanel;
        this.signupPanel = signupPanel;
    }

    public JsonDatabaseManager getDb() {
        return db;
    }

    public void setDb(JsonDatabaseManager db) {
        this.db = db;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }
    private void login(String email, String password)
    {
        User user = db.getUserByEmail(email);
        checkUser(password, user);
    }

    private void login(int id, String password)
    {
        User user = db.getUserById(id);
        checkUser(password, user);
    }

    private void checkUser(String password, User user) {
        if (user == null)
        {
            loginPanel.displayMessage("Couldn't find user!");
            loginPanel.clearFields();
        }
        else
        {
            if (user.authenticate(password))
            {
                loggedInUser = user;
                loginPanel.displayMessage("Welcome " + loggedInUser.getUsername() + " !");
            }
            else
            {
                loginPanel.displayMessage("Incorrect username/password"); //In reality password is incorrect here but for security ig :(
            }
        }
    }

    public User loginUser(String emailOrId, String password)
    {
        if (Validator.isFieldEmpty(emailOrId) || Validator.isFieldEmpty(password)) {
            loginPanel.displayMessage("Email/ID and Password can't be empty!");
            loginPanel.clearFields();
            return null;
        }
        if (Validator.checkEmail(emailOrId))
        {
            login(emailOrId, password);
            return loggedInUser;
        } else if (Validator.checkId(emailOrId)) {
            login(Integer.parseInt(emailOrId), password);
            return loggedInUser;
        }
        else
        {
            loginPanel.displayMessage("Incorrect Format of email or ID entered");
            loginPanel.clearFields();
            return null;
        }
    }
    public User registerUser(String username, String email, String password, String repeatedPassword)
    {
        if (username.isEmpty()) {
            signupPanel.displayMessage("Username can't be empty!");
            return null;
        }
        if (!Validator.checkName(username))
        {
            signupPanel.displayMessage("Wrong username format!");
            return null;
        }
        if (!Validator.checkEmail(email))
        {
            signupPanel.displayMessage("Wrong email format!");
            return null;
        }
        if (!Validator.checkPassword(password))
        {
            signupPanel.displayMessage("Use Strong password! (6 chars min + symb + upperCase)");
            return null;
        }
        if (!Validator.confirmPassword(password, repeatedPassword))
        {
            signupPanel.displayMessage("Passwords do not match!");
            return null;
        }
        // If signup has buttons for student/instructor then get the value from there and add from db according to the button.
        if (signupPanel.getRole().equals("student"))
        {
            Student student = new Student(username, email, PasswordHasher.hashPassword(password), "student");
            db.addUser(student);
            loggedInUser = student;
            return student;
        }
        if (signupPanel.getRole().equals("instructor"))
        {
            Instructor instructor = new Instructor(username, email, PasswordHasher.hashPassword(password),  "instructor");
            db.addUser(instructor);
            loggedInUser = instructor;
            return instructor;
        }
        return null;
    }
}
