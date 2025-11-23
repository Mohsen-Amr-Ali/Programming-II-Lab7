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
        setBackground(StyleColors.BACKGROUND);
        setForeground(StyleColors.TEXT);
        setBorder(null);
        setFocusable(false);
        setOpaque(false); // Important for gaps to show background

        setUI(new ModernTabbedPaneUI());
    }

    private static class ModernTabbedPaneUI extends BasicTabbedPaneUI {

        @Override
        protected void installDefaults() {
            super.installDefaults();
            shadow = StyleColors.BACKGROUND;
            darkShadow = StyleColors.BACKGROUND;
            lightHighlight = StyleColors.BACKGROUND;
            focus = StyleColors.BACKGROUND;
            contentBorderInsets = new Insets(5, 0, 0, 0); // Gap between tabs and content
        }

        @Override
        protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
            // No border
        }

        @Override
        protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (isSelected) {
                g2.setColor(StyleColors.ACCENT);
            } else {
                g2.setColor(StyleColors.CARD);
            }

            // Draw rounded rect with gaps
            // x+2, y+2, w-4, h-2 creates a gap between tabs
            g2.fillRoundRect(x + 4, y + 2, w - 8, h - 2, 10, 10);

            g2.dispose();
        }

        @Override
        protected void paintContentBorder(Graphics g, int tabPlacement, int selectedIndex) {
            // No border around the content panel
        }

        @Override
        protected void paintText(Graphics g, int tabPlacement, Font font, FontMetrics metrics, int tabIndex, String title, Rectangle textRect, boolean isSelected) {
            g.setFont(font);
            if (isSelected) {
                g.setColor(StyleColors.TEXT);
            } else {
                g.setColor(StyleColors.TEXT.darker());
            }
            g.drawString(title, textRect.x, textRect.y + metrics.getAscent());
        }

        @Override
        protected int calculateTabHeight(int tabPlacement, int tabIndex, int fontHeight) {
            return super.calculateTabHeight(tabPlacement, tabIndex, fontHeight) + 12;
        }

        @Override
        protected int calculateTabWidth(int tabPlacement, int tabIndex, FontMetrics metrics) {
            return super.calculateTabWidth(tabPlacement, tabIndex, metrics) + 10; // Extra width for gaps
        }
    }
}