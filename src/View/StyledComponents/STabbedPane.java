package View.StyledComponents;

import javax.swing.*;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import java.awt.*;

public class STabbedPane extends JTabbedPane {

    public STabbedPane() {
        super();
        applyStyling();
    }

    private void applyStyling() {
        setFont(new Font("Segoe UI", Font.BOLD, 14));
        setBackground(StyleColors.BACKGROUND); // Background of the content area
        setForeground(StyleColors.TEXT);       // Text color of unselected tabs
        setBorder(null);
        setFocusable(false);

        // Apply custom UI painter
        setUI(new ModernTabbedPaneUI());
    }

    private static class ModernTabbedPaneUI extends BasicTabbedPaneUI {

        @Override
        protected void installDefaults() {
            super.installDefaults();
            shadow = StyleColors.BACKGROUND;
            darkShadow = StyleColors.BACKGROUND;
            lightHighlight = StyleColors.BACKGROUND;
            focus = StyleColors.BACKGROUND; // Removes default focus color
        }

        @Override
        protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
            // No border for a cleaner look
        }

        @Override
        protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (isSelected) {
                g2.setColor(StyleColors.ACCENT); // Selected Color (Purple)
            } else {
                g2.setColor(StyleColors.CARD);   // Unselected Color (Card Dark)
            }

            // Draw a filled rectangle for the tab
            g2.fillRect(x, y, w, h);
        }

        @Override
        protected void paintContentBorder(Graphics g, int tabPlacement, int selectedIndex) {
            // No content border (removes the line around the panel)
        }

        @Override
        protected void paintText(Graphics g, int tabPlacement, Font font, FontMetrics metrics, int tabIndex, String title, Rectangle textRect, boolean isSelected) {
            g.setFont(font);
            if (isSelected) {
                g.setColor(StyleColors.TEXT); // Text color for selected
            } else {
                g.setColor(StyleColors.TEXT.darker()); // Dimmer text for unselected
            }
            g.drawString(title, textRect.x, textRect.y + metrics.getAscent());
        }

        @Override
        protected int calculateTabHeight(int tabPlacement, int tabIndex, int fontHeight) {
            return super.calculateTabHeight(tabPlacement, tabIndex, fontHeight) + 10; // Add padding height
        }
    }
}