package View.StyledComponents;

import javax.swing.*;
import java.awt.*;

public class SBtn extends JButton {
    private Color originalBackgroundColor; // Store the original background color

    public SBtn(String text) {
        super(text);
        applyStyling();
    }

    public SBtn() {
        super();
        applyStyling();
    }

    private void applyStyling() {
        setFont(new Font("Segoe UI", Font.PLAIN, 14));
        setForeground(StyleColors.TEXT);
        setBackground(StyleColors.CARD);
        setFocusable(false);
        setFocusPainted(false);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(StyleColors.ACCENT, 1, true),
                BorderFactory.createEmptyBorder(6, 12, 6, 12)
        ));
        setOpaque(true);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Store the initial background color (default is CARD)
        originalBackgroundColor = getBackground();

        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                setBackground(StyleColors.ACCENT);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                // Restore the original background color instead of always using CARD
                setBackground(originalBackgroundColor);
            }
        });
    }

    @Override
    public void setBackground(Color bg) {
        super.setBackground(bg);
        // Update the stored original color whenever background is explicitly set
        // But only if we're not in a hover state (i.e., not ACCENT color)
        if (bg != null && !bg.equals(StyleColors.ACCENT)) {
            originalBackgroundColor = bg;
        }
    }
}
