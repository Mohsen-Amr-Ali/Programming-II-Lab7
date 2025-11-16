package View.StyledComponents;

import javax.swing.*;
import java.awt.*;

public class SCoursePanel extends JPanel {

    private static final Color BORDER_COLOR = new Color(20, 20, 30);
    private static final Color DEFAULT_BACKGROUND = StyleColors.CARD;
    private static final Color HOVER_BACKGROUND = StyleColors.HOVER;

    public SCoursePanel() {
        super();
        applyStyling();
    }

    public SCoursePanel(LayoutManager layout) {
        super(layout);
        applyStyling();
    }

    private void applyStyling() {
        setBackground(DEFAULT_BACKGROUND);
        setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        setOpaque(true);

        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                setBackground(HOVER_BACKGROUND);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                setBackground(DEFAULT_BACKGROUND);
            }
        });
    }
}

