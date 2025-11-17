package View.StyledComponents;

import javax.swing.*;
import java.awt.*;

/**
 * SOptionPane is a styled version of JOptionPane that uses the custom style defined in StyleColors and other StyledComponents.
 */
public class SOptionPane extends JOptionPane {
    public SOptionPane() {
        super();
        applyCustomStyle();
    }

    public SOptionPane(Object message) {
        super(message);
        applyCustomStyle();
    }

    public SOptionPane(Object message, int messageType) {
        super(message, messageType);
        applyCustomStyle();
    }

    public SOptionPane(Object message, int messageType, int optionType) {
        super(message, messageType, optionType);
        applyCustomStyle();
    }

    private void applyCustomStyle() {
        UIManager.put("OptionPane.background", StyleColors.BACKGROUND);
        UIManager.put("Panel.background", StyleColors.BACKGROUND);
        UIManager.put("OptionPane.messageForeground", StyleColors.TEXT);
        UIManager.put("Button.background", StyleColors.ACCENT);
        UIManager.put("Button.foreground", StyleColors.TEXT);
        UIManager.put("Button.font", new Font("Segoe UI", Font.BOLD, 14));
        UIManager.put("OptionPane.messageFont", new Font("Segoe UI", Font.PLAIN, 15));
    }

    // Static helper methods for showing dialogs with custom style
    public static void showMessageDialog(Component parentComponent, Object message, String title, int messageType) {
        new SOptionPane().applyCustomStyle();
        JOptionPane.showMessageDialog(parentComponent, message, title, messageType);
    }

    public static int showConfirmDialog(Component parentComponent, Object message, String title, int optionType) {
        new SOptionPane().applyCustomStyle();
        return JOptionPane.showConfirmDialog(parentComponent, message, title, optionType);
    }

    public static String showInputDialog(Component parentComponent, Object message, String title, int messageType) {
        new SOptionPane().applyCustomStyle();
        return JOptionPane.showInputDialog(parentComponent, message, title, messageType);
    }
}
