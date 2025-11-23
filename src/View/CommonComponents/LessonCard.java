package View.CommonComponents;

import Model.Course.Lesson;
import View.StyledComponents.*;

import javax.swing.*;
import java.awt.*;

public class LessonCard extends JPanel {
    private JLabel titleLabel;
    private JLabel numberLabel; // Make accessible for hover updates
    private SCheckBox completedCheckBox;

    // Colors
    private Color originalColor;
    private Color hoverColor;

    // First constructor: number and lesson
    public LessonCard(int number, Lesson lesson) {
        // Initialize default colors
        this.originalColor = StyleColors.CARD;
        this.hoverColor = StyleColors.lightenColor(originalColor, 0.12f);

        setLayout(new BorderLayout(12, 0));
        setBackground(originalColor);

        // Use consistent border styling
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(StyleColors.ACCENT_DARK, 1, true),
                BorderFactory.createEmptyBorder(12, 18, 12, 18)
        ));

        numberLabel = new JLabel(String.valueOf(number));
        numberLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        numberLabel.setForeground(StyleColors.ACCENT); // Default color
        numberLabel.setHorizontalAlignment(SwingConstants.CENTER);
        numberLabel.setPreferredSize(new Dimension(36, 36));
        numberLabel.setOpaque(false);

        titleLabel = new JLabel(lesson.getTitle());
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        titleLabel.setForeground(StyleColors.TEXT);
        titleLabel.setHorizontalAlignment(SwingConstants.LEFT);

        add(numberLabel, BorderLayout.WEST);
        add(titleLabel, BorderLayout.CENTER);

        setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        // Hover effect
        addHoverEffect();
    }

    // Overloaded constructor: adds a checkbox
    public LessonCard(int number, Lesson lesson, boolean completed) {
        this(number, lesson);
        completedCheckBox = new SCheckBox();
        completedCheckBox.setText("Completed");
        completedCheckBox.setSelected(completed);
        completedCheckBox.setBackground(originalColor); // Ensure match
        completedCheckBox.setForeground(StyleColors.TEXT);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setOpaque(false);
        rightPanel.add(completedCheckBox, BorderLayout.EAST);
        add(rightPanel, BorderLayout.EAST);
    }

    // Removed the Edit Button constructor as requested.
    // Instructor view will use the basic constructor and handle actions externally.

    private void addHoverEffect() {
        this.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                // Hover state
                setBackground(hoverColor);
                if (completedCheckBox != null) completedCheckBox.setBackground(hoverColor);

                // Change number color to white on hover/select
                numberLabel.setForeground(Color.WHITE);
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                // Revert state
                setBackground(originalColor);
                if (completedCheckBox != null) completedCheckBox.setBackground(originalColor);

                // Only revert number color if this isn't the "selected" card (handled via setBackground check usually)
                // For simplicity in hover: revert to accent.
                if (!getBackground().equals(StyleColors.ACCENT)) {
                    numberLabel.setForeground(StyleColors.ACCENT);
                } else {
                    numberLabel.setForeground(Color.WHITE); // Stay white if selected
                }
            }
        });
    }

    @Override
    public void setBackground(Color bg) {
        super.setBackground(bg);
        // If the background is set to Accent (Selected state), ensure text is white
        if (bg.equals(StyleColors.ACCENT)) {
            if (numberLabel != null) numberLabel.setForeground(Color.WHITE);
            if (completedCheckBox != null) completedCheckBox.setBackground(bg);
        } else if (bg.equals(StyleColors.CARD)) {
            // If reverting to default, revert text color
            if (numberLabel != null) numberLabel.setForeground(StyleColors.ACCENT);
            this.originalColor = bg;
            this.hoverColor = StyleColors.lightenColor(originalColor, 0.12f);
            if (completedCheckBox != null) completedCheckBox.setBackground(bg);
        }
    }

    public boolean isCompleted() {
        return completedCheckBox != null && completedCheckBox.isSelected();
    }

    public void setCompleted(boolean completed) {
        if (completedCheckBox != null) completedCheckBox.setSelected(completed);
    }

    public void addCheckBoxListener(java.awt.event.ActionListener listener) {
        if (completedCheckBox != null) completedCheckBox.addActionListener(listener);
    }
}