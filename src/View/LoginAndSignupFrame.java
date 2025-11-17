package View;

import View.StyledComponents.*;
import Controller.MainController;
import javax.swing.*;
import java.awt.*;

public class LoginAndSignupFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private LoginPanel loginPanel;
    private SignupPanel signupPanel;
    private SBtn switchToSignupButton;
    private SBtn switchToLoginButton;
    private JPanel navigationPanel;
    private MainController mainController;

    public LoginAndSignupFrame(MainController mainController) {
        //----------------------Instantiation----------------------//
        super("SkillForge");

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        this.mainController = mainController;
        loginPanel = new LoginPanel(this);
        signupPanel = new SignupPanel(this);

        switchToSignupButton = new SBtn("Don't have an account? Sign Up");
        switchToLoginButton = new SBtn("Already have an account? Login");

        navigationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        //---------------------- Styling ----------------------//
        cardPanel.setBackground(StyleColors.BACKGROUND);
        navigationPanel.setBackground(StyleColors.BACKGROUND);
        navigationPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));


        //---------------------- Adding Components ----------------------//
        // Add panels to card layout
        cardPanel.add(loginPanel, "LOGIN");
        cardPanel.add(signupPanel, "SIGNUP");

        // Add navigation buttons to navigation panel
        navigationPanel.add(switchToSignupButton);
        navigationPanel.add(switchToLoginButton);

        // Initially show only the signup button (since login is shown first)
        switchToLoginButton.setVisible(false);

        // Set up the frame layout
        setLayout(new BorderLayout());
        add(cardPanel, BorderLayout.CENTER);
        add(navigationPanel, BorderLayout.SOUTH);

        //---------------------- Event Listeners ----------------------//
        switchToSignupButton.addActionListener(e -> {
            cardLayout.show(cardPanel, "SIGNUP");
            switchToSignupButton.setVisible(false);
            switchToLoginButton.setVisible(true);
        });

        switchToLoginButton.addActionListener(e -> {
            cardLayout.show(cardPanel, "LOGIN");
            switchToLoginButton.setVisible(false);
            switchToSignupButton.setVisible(true);
        });

        //---------------------- Frame Setup ----------------------//
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 600);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    public LoginPanel getLoginPanel() {
        return loginPanel;
    }

    public SignupPanel getSignupPanel() {
        return signupPanel;
    }

    public void showLoginPanel() {
        cardLayout.show(cardPanel, "LOGIN");
        switchToLoginButton.setVisible(false);
        switchToSignupButton.setVisible(true);
    }

    public void showSignupPanel() {
        cardLayout.show(cardPanel, "SIGNUP");
        switchToSignupButton.setVisible(false);
        switchToLoginButton.setVisible(true);
    }

    // Intermediary methods that panels will call
    public void validateLogin(String emailOrId, String password) {
        mainController.login(emailOrId, password);
    }

    public void validateSignup(String name, String email, String password, String confirmPassword) {
        mainController.signup(name, email, password, confirmPassword);
    }

    public String getLoginIdOrEmail() {
        return loginPanel.getIdOrEmail();
    }

    public String getLoginPassword() {
        return loginPanel.getPassword();
    }

    public String getSignupName() {
        return signupPanel.getName();
    }

    public String getSignupEmail() {
        return signupPanel.getEmail();
    }

    public String getSignupPassword() {
        return signupPanel.getPassword();
    }

    public String getSignupConfirmPassword() {
        return signupPanel.getConfirmPassword();
    }

    public String getSignupRole() {
        return signupPanel.getRole();
    }

    public void displayLoginMessage(String message) {
        loginPanel.displayMessage(message);
    }

    public void displaySignupMessage(String message) {
        signupPanel.displayMessage(message);
    }

    public void clearLoginFields() {
        loginPanel.clearFields();
    }

    public void clearSignupFields() {
        signupPanel.clearFields();
    }
}
