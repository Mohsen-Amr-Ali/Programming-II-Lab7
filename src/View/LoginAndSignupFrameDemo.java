package View;

import javax.swing.*;

public class LoginAndSignupFrameDemo {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginAndSignupFrame frame = new LoginAndSignupFrame();

            // Add example action listeners to demonstrate functionality
            frame.getLoginPanel().getLoginButton().addActionListener(e -> {
                String idOrEmail = frame.getLoginPanel().getIdOrEmail();
                String password = frame.getLoginPanel().getPassword();

                JOptionPane.showMessageDialog(frame,
                    "Login clicked!\nID/Email: " + idOrEmail + "\nPassword: " + password,
                    "Login Info",
                    JOptionPane.INFORMATION_MESSAGE);
            });

            frame.getSignupPanel().getSignupButton().addActionListener(e -> {
                String name = frame.getSignupPanel().getName();
                String email = frame.getSignupPanel().getEmail();
                String password = frame.getSignupPanel().getPassword();
                String confirmPassword = frame.getSignupPanel().getConfirmPassword();

                if (!password.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(frame,
                        "Passwords do not match!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }

                JOptionPane.showMessageDialog(frame,
                    "Signup clicked!\nName: " + name + "\nEmail: " + email,
                    "Signup Info",
                    JOptionPane.INFORMATION_MESSAGE);
            });

            frame.setVisible(true);
        });
    }
}

