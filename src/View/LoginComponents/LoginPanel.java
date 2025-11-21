package View.LoginComponents;

import View.LoginAndSignupFrame;
import View.StyledComponents.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class LoginPanel extends JPanel {
    private STField idOrEmailField;
    private JPasswordField passwordField;
    private SBtn loginButton;
    private LoginAndSignupFrame parentFrame;

    public LoginPanel(LoginAndSignupFrame parentFrame) {
        this.parentFrame = parentFrame;

        //----------------------Instantiation----------------------//
        setLayout(new GridBagLayout());
        idOrEmailField = new STField(20);
        passwordField = new JPasswordField(20);
        loginButton = new SBtn("Login");

        //---------------------- Styling ----------------------//
        setBackground(StyleColors.BACKGROUND);
        Border panelBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(StyleColors.ACCENT, 2),
                "Login",
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

        // Password Field Styling
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setForeground(StyleColors.TEXT);
        passwordField.setBackground(StyleColors.CARD);
        passwordField.setCaretColor(StyleColors.TEXT);
        Border flatBorder = BorderFactory.createLineBorder(StyleColors.ACCENT_DARK, 1);
        Border padding = BorderFactory.createEmptyBorder(5, 8, 5, 8);
        passwordField.setBorder(BorderFactory.createCompoundBorder(flatBorder, padding));

        //---------------------- Adding Components ----------------------//
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 8, 3, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;

        // ID or Email Label
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        add(new SLabel("ID or Email:"), gbc);

        // ID or Email Field
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.insets.set(0, 8, 8, 8);
        add(idOrEmailField, gbc);

        // Password Label
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.insets.set(5, 8, 0, 8);
        add(new SLabel("Password:"), gbc);

        // Password Field
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.insets.set(3, 8, 8, 8);
        add(passwordField, gbc);

        // Login Button
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.insets.set(15, 8, 5, 8);
        gbc.fill = GridBagConstraints.NONE;
        add(loginButton, gbc);

        // Login Button Action Listener
        loginButton.addActionListener(e -> {
            String emailOrId = getIdOrEmail();
            String password = getPassword();
            parentFrame.validateLogin(emailOrId, password);
        });
    }

    public String getIdOrEmail() {
        return idOrEmailField.getText();
    }

    public String getPassword() {
        return new String(passwordField.getPassword());
    }

    public void displayMessage(String message)
    {
        SOptionPane.showMessageDialog(this, message);
    }

    public void clearFields() {
        idOrEmailField.setText("");
        passwordField.setText("");
    }

    public SBtn getLoginButton() {
        return loginButton;
    }
}

