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

        setUI(new ModernComboBoxUI());

        // Border similar to STField
        Border flatBorder = BorderFactory.createLineBorder(StyleColors.ACCENT_DARK, 1);
        Border padding = BorderFactory.createEmptyBorder(5, 8, 5, 8);
        setBorder(BorderFactory.createCompoundBorder(flatBorder, padding));

        // Custom Renderer for BOTH the list items and the selected item view
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

                // If index is -1, it means we are rendering the selected item in the combo box itself
                if (index == -1) {
                    setBackground(StyleColors.CARD);
                    setForeground(StyleColors.TEXT);
                }

                setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
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
                    // Paint background of the arrow button area
                    g.setColor(StyleColors.CARD);
                    g.fillRect(0, 0, getWidth(), getHeight());

                    // Paint arrow
                    int width = getWidth();
                    int height = getHeight();
                    int size = 8;
                    int x = (width - size) / 2;
                    int y = (height - size) / 2;

                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(StyleColors.ACCENT);

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
        public void paintCurrentValueBackground(Graphics g, Rectangle bounds, boolean hasFocus) {
            // Override this to force the background of the "selected item" area
            g.setColor(StyleColors.CARD);
            g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
        }

        @Override
        protected ComboPopup createPopup() {
            BasicComboPopup popup = new BasicComboPopup(comboBox) {
                @Override
                protected JScrollPane createScroller() {
                    SScrollPane scroller = new SScrollPane(list);
                    scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
                    scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
                    // Force viewport background
                    scroller.getViewport().setBackground(StyleColors.CARD);
                    scroller.setBackground(StyleColors.CARD);
                    return scroller;
                }

                @Override
                protected void configurePopup() {
                    super.configurePopup();
                    list.setBackground(StyleColors.CARD);
                    list.setForeground(StyleColors.TEXT);
                    list.setSelectionBackground(StyleColors.ACCENT);
                    list.setSelectionForeground(StyleColors.TEXT);
                    setBorder(BorderFactory.createLineBorder(StyleColors.ACCENT_DARK, 1));
                }
            };
            return popup;
        }
    }
}