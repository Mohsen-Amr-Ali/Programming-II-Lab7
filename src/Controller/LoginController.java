package Controller;

import Model.User.Instructor;
import Model.JsonDatabaseManager;
import Model.User.*;
import View.LoginComponents.LoginPanel;
import View.LoginComponents.SignupPanel;
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
        loggedInUser = null;

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
            return loggedInUser;
        }
    }
    public User registerUser(String username, String email, String password, String repeatedPassword)
    {
        loggedInUser = null;
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
        if (db.getUserByEmail(email) != null)
        {
            signupPanel.displayMessage("This email is already used!");
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
        
        // Convert the role string from the UI to the enum value
        String roleString = signupPanel.getRole();
        USER_ROLE role = null;

        try {
            role = USER_ROLE.valueOf(roleString.toUpperCase());
        } catch (IllegalArgumentException e) {
            signupPanel.displayMessage("Invalid role selected.");
            return null;
        }

        // --- UPDATED USER CREATION LOGIC ---
        User newUser = null;
        String hashedPassword = PasswordHasher.hashPassword(password);
        
        if (role == USER_ROLE.STUDENT)
        {
            newUser = new Student(username, email, hashedPassword, USER_ROLE.STUDENT);
        }
        else if (role == USER_ROLE.INSTRUCTOR)
        {
            newUser = new Instructor(username, email, hashedPassword, USER_ROLE.INSTRUCTOR);
        }
        else if (role == USER_ROLE.ADMIN) 
        {
            // Note: Admins are not available in the public signup combobox, 
            // but this path supports manual creation or future changes.
            newUser = new Admin(username, email, hashedPassword, USER_ROLE.ADMIN);
        }

        if (newUser != null) {
            if (newUser instanceof Student) {
                db.addUser((Student) newUser);
            } else if (newUser instanceof Instructor) {
                db.addUser((Instructor) newUser);
            } else if (newUser instanceof Admin) {
                db.addUser((Admin) newUser);
            }
            loggedInUser = newUser;
            return newUser;
        }
        
        return null; // Should not happen if roles are correctly mapped
    }
}
