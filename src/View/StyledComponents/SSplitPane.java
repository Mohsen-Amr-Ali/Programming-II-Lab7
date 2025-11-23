package View.StyledComponents;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import java.awt.*;

public class SSplitPane extends JSplitPane {

    public SSplitPane(int newOrientation, Component newLeftComponent, Component newRightComponent) {
        super(newOrientation, newLeftComponent, newRightComponent);
        applyStyling();
    }

    public SSplitPane(int newOrientation, boolean newContinuousLayout, Component newLeftComponent, Component newRightComponent) {
        super(newOrientation, newContinuousLayout, newLeftComponent, newRightComponent);
        applyStyling();
    }

    public SSplitPane() {
        super();
        applyStyling();
    }

    private void applyStyling() {
        // Remove default borders
        setBorder(null);

        // Set the split pane background (matches the gap color)
        setBackground(StyleColors.BACKGROUND);

        // Set the divider size (width of the gap)
        setDividerSize(10); // 10px gap

        // Apply smooth continuous layout by default
        setContinuousLayout(true);

        // Apply Custom UI to flatten the divider
        setUI(new ModernSplitPaneUI());
    }

    /**
     * Custom UI to render a flat, borderless divider.
     */
    private static class ModernSplitPaneUI extends BasicSplitPaneUI {

        @Override
        public BasicSplitPaneDivider createDefaultDivider() {
            return new BasicSplitPaneDivider(this) {
                @Override
                public void setBorder(Border b) {
                    // Prevent default border being set on the divider
                }

                @Override
                public void paint(Graphics g) {
                    // Paint the divider flat with the background color
                    g.setColor(StyleColors.BACKGROUND);
                    g.fillRect(0, 0, getWidth(), getHeight());
                    super.paint(g);
                }
            };
        }
    }
}