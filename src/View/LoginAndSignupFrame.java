package View;

import View.StyledComponents.*;
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

    public LoginAndSignupFrame() {
        //----------------------Instantiation----------------------//
        super("SkillForge");

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        loginPanel = new LoginPanel();
        signupPanel = new SignupPanel();

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
        setSize(450, 500);
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
}
