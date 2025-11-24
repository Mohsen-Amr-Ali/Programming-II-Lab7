package View.LoginComponents;

import View.LoginAndSignupFrame;
import View.StyledComponents.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class SignupPanel extends JPanel {
    private STField nameField;
    private STField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private SComboBox<String> roleComboBox;
    private SBtn signupButton;
    private LoginAndSignupFrame parentFrame;

    public SignupPanel(LoginAndSignupFrame parentFrame) {
        this.parentFrame = parentFrame;
        //----------------------Instantiation----------------------//
        setLayout(new GridBagLayout());
        nameField = new STField(20);
        emailField = new STField(20);
        passwordField = new JPasswordField(20);
        confirmPasswordField = new JPasswordField(20);
        roleComboBox = new SComboBox<>(new String[]{"Instructor", "Student", "Admin"});
        signupButton = new SBtn("Sign Up");

        //---------------------- Styling ----------------------//
        setBackground(StyleColors.BACKGROUND);
        Border panelBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(StyleColors.ACCENT, 2),
                "Sign Up",
                TitledBorder.CENTER,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 25),
                StyleColors.ACCENT
        );
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(15, 15, 10, 15),
                BorderFactory.createCompoundBorder(panelBorder,
                        BorderFactory.createEmptyBorder(10, 10, 10, 10)
                ))
        );

        // Password Fields Styling
        Border flatBorder = BorderFactory.createLineBorder(StyleColors.ACCENT_DARK, 1);
        Border padding = BorderFactory.createEmptyBorder(5, 8, 5, 8);
        
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setForeground(StyleColors.TEXT);
        passwordField.setBackground(StyleColors.CARD);
        passwordField.setCaretColor(StyleColors.TEXT);
        passwordField.setBorder(BorderFactory.createCompoundBorder(flatBorder, padding));

        confirmPasswordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        confirmPasswordField.setForeground(StyleColors.TEXT);
        confirmPasswordField.setBackground(StyleColors.CARD);
        confirmPasswordField.setCaretColor(StyleColors.TEXT);
        confirmPasswordField.setBorder(BorderFactory.createCompoundBorder(flatBorder, padding));

        // Role ComboBox - SComboBox handles styling automatically

        //---------------------- Adding Components ----------------------//
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 8, 3, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;

        // Name Label
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        add(new SLabel("Name:"), gbc);

        // Name Field
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets.set(0, 8, 6, 8);
        add(nameField, gbc);

        // Email Label
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.insets.set(4, 8, 0, 8);
        add(new SLabel("Email:"), gbc);

        // Email Field
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.insets.set(3, 8, 6, 8);
        add(emailField, gbc);

        // Password Label
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.insets.set(4, 8, 0, 8);
        add(new SLabel("Password:"), gbc);

        // Password Field
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.insets.set(3, 8, 6, 8);
        add(passwordField, gbc);

        // Confirm Password Label
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.insets.set(4, 8, 0, 8);
        add(new SLabel("Confirm Password:"), gbc);

        // Confirm Password Field
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.insets.set(3, 8, 6, 8);
        add(confirmPasswordField, gbc);

        // Role Label
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.insets.set(4, 8, 0, 8);
        add(new SLabel("Role:"), gbc);

        // Role ComboBox
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.insets.set(3, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(roleComboBox, gbc);

        // Signup Button
        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.insets.set(12, 8, 5, 8);
        gbc.fill = GridBagConstraints.NONE;
        add(signupButton, gbc);

        // Signup Button Action Listener
        signupButton.addActionListener(e -> {
            String name = getName();
            String email = getEmail();
            String password = getPassword();
            String confirmPassword = getConfirmPassword();
            parentFrame.validateSignup(name, email, password, confirmPassword);
        });
    }

    public String getName() {
        return nameField.getText();
    }

    public String getEmail() {
        return emailField.getText();
    }

    public String getPassword() {
        return new String(passwordField.getPassword());
    }

    public String getConfirmPassword() {
        return new String(confirmPasswordField.getPassword());
    }

    public String getRole() {
        String selectedRole = (String) roleComboBox.getSelectedItem();
        return selectedRole != null ? selectedRole.toLowerCase() : "";
    }

    public void displayMessage(String message)
    {
        SOptionPane.showMessageDialog(this, message);
    }

    public void clearFields() {
        nameField.setText("");
        emailField.setText("");
        passwordField.setText("");
        confirmPasswordField.setText("");
        roleComboBox.setSelectedIndex(0);
    }

    public SBtn getSignupButton() {
        return signupButton;
    }
}

