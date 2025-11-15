package View.StyledComponents;

import javax.swing.*;
import java.awt.*;

public class SBtn extends JButton {

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

        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                setBackground(StyleColors.ACCENT);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                setBackground(StyleColors.CARD);
            }
        });
    }
}
