package View.StyledComponents;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
import java.awt.*;

public class SComboBox<E> extends JComboBox<E> {

    public SComboBox(E[] items) {
        super(items);
        applyStyling();
    }

    public SComboBox() {
        super();
        applyStyling();
    }

    private void applyStyling() {
        setFont(new Font("Segoe UI", Font.PLAIN, 14));
        setForeground(StyleColors.TEXT);
        setBackground(StyleColors.CARD);

        // Use a custom UI to control the arrow button and popup
        setUI(new ModernComboBoxUI());

        // Border similar to STField
        Border flatBorder = BorderFactory.createLineBorder(StyleColors.ACCENT_DARK, 1);
        Border padding = BorderFactory.createEmptyBorder(5, 8, 5, 8);
        setBorder(BorderFactory.createCompoundBorder(flatBorder, padding));

        // Ensure the renderer (the list items) matches the theme
        setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (isSelected) {
                    setBackground(StyleColors.ACCENT);
                    setForeground(StyleColors.TEXT);
                } else {
                    setBackground(StyleColors.CARD);
                    setForeground(StyleColors.TEXT);
                }
                setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
                return this;
            }
        });
    }

    private static class ModernComboBoxUI extends BasicComboBoxUI {
        @Override
        protected JButton createArrowButton() {
            JButton button = new JButton() {
                @Override
                public void paint(Graphics g) {
                    // Paint the background
                    g.setColor(StyleColors.CARD);
                    g.fillRect(0, 0, getWidth(), getHeight());

                    // Paint the arrow
                    int width = getWidth();
                    int height = getHeight();
                    int size = 8;
                    int x = (width - size) / 2;
                    int y = (height - size) / 2;

                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(StyleColors.ACCENT);

                    // Draw an inverted triangle
                    Polygon arrow = new Polygon();
                    arrow.addPoint(x, y);
                    arrow.addPoint(x + size, y);
                    arrow.addPoint(x + size / 2, y + size - 2);
                    g2.fillPolygon(arrow);
                }
            };
            button.setBorder(BorderFactory.createEmptyBorder());
            button.setContentAreaFilled(false);
            return button;
        }

        @Override
        protected ComboPopup createPopup() {
            BasicComboPopup popup = new BasicComboPopup(comboBox) {
                @Override
                protected JScrollPane createScroller() {
                    // Use our styled scroll pane for the dropdown list
                    SScrollPane scroller = new SScrollPane(list);
                    scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
                    scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
                    return scroller;
                }
            };
            popup.setBorder(BorderFactory.createLineBorder(StyleColors.ACCENT_DARK, 1));
            return popup;
        }
    }
}