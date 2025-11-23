package View.StyledComponents;

import javax.swing.*;
import java.awt.*;

public class StyleColors {
    // Primary Colors
    public static final Color CARD = new Color(40, 40, 60);
    public static final Color TEXT = new Color(220, 220, 235);
    public static final Color ACCENT = new Color(110, 90, 230);

    public static final Color ACCENT_DARK = ACCENT.darker();
    public static final Color HOVER = new Color(60, 60, 80);
    public static final Color BACKGROUND = new Color(30, 30, 45);

    // Initialize global UI defaults
    static {
        UIManager.put("TextField.caretForeground", Color.WHITE);
        UIManager.put("TextArea.caretForeground", Color.WHITE);
        UIManager.put("PasswordField.caretForeground", Color.WHITE);
        UIManager.put("FormattedTextField.caretForeground", Color.WHITE);
    }

    private StyleColors() {}

    // Utility to lighten a color
    public static Color lightenColor(Color color, float fraction) {
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        int red = (int) Math.min(r + 255 * fraction, 255);
        int green = (int) Math.min(g + 255 * fraction, 255);
        int blue = (int) Math.min(b + 255 * fraction, 255);
        return new Color(red, green, blue);
    }
}