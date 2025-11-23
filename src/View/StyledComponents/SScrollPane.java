package View.StyledComponents;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

public class SScrollPane extends JScrollPane {

    public SScrollPane(Component view) {
        super(view);
        applyStyling();
    }

    public SScrollPane() {
        super();
        applyStyling();
    }

    private void applyStyling() {
        // Remove the default border of the scrollpane
        setBorder(BorderFactory.createEmptyBorder());

        // Set backgrounds
        setBackground(StyleColors.BACKGROUND);
        getViewport().setBackground(StyleColors.BACKGROUND);

        // Apply custom UI to scrollbars
        getVerticalScrollBar().setUI(new ModernScrollBarUI());
        getHorizontalScrollBar().setUI(new ModernScrollBarUI());

        // Adjust scroll speed for better UX
        getVerticalScrollBar().setUnitIncrement(16);
        getHorizontalScrollBar().setUnitIncrement(16);
    }

    // Inner class for custom ScrollBar styling
    private static class ModernScrollBarUI extends BasicScrollBarUI {
        private static final int SCROLL_BAR_WIDTH = 10;

        @Override
        protected void configureScrollBarColors() {
            this.thumbColor = StyleColors.ACCENT;
            this.trackColor = StyleColors.BACKGROUND;
        }

        @Override
        public Dimension getPreferredSize(JComponent c) {
            return new Dimension(SCROLL_BAR_WIDTH, SCROLL_BAR_WIDTH);
        }

        @Override
        protected JButton createDecreaseButton(int orientation) {
            return createZeroButton();
        }

        @Override
        protected JButton createIncreaseButton(int orientation) {
            return createZeroButton();
        }

        private JButton createZeroButton() {
            JButton jbutton = new JButton();
            jbutton.setPreferredSize(new Dimension(0, 0));
            jbutton.setMinimumSize(new Dimension(0, 0));
            jbutton.setMaximumSize(new Dimension(0, 0));
            return jbutton;
        }

        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
            if (thumbBounds.isEmpty() || !scrollbar.isEnabled()) {
                return;
            }

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setPaint(StyleColors.ACCENT);
            // Draw with rounded corners
            g2.fillRoundRect(thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height, 8, 8);

            g2.dispose();
        }

        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(StyleColors.BACKGROUND);
            g2.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
            g2.dispose();
        }
    }
}