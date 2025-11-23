package View.CommonComponents;

import Model.Course.Lesson;
import View.StyledComponents.*;

import javax.swing.*;
import java.awt.*;

public class LessonCard extends JPanel {
    private JLabel titleLabel;
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

        JLabel numberLabel = new JLabel(String.valueOf(number));
        numberLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        numberLabel.setForeground(StyleColors.ACCENT);
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

    // Third constructor: adds an 'Edit Lesson' stylized button instead of the checkbox
    public LessonCard(int number, Lesson lesson, String dummy) {
        this(number, lesson);
        SBtn editButton = new SBtn("Edit Lesson");
        editButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        editButton.setBackground(StyleColors.ACCENT);
        editButton.setForeground(Color.WHITE);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setOpaque(false);
        rightPanel.add(editButton, BorderLayout.EAST);
        add(rightPanel, BorderLayout.EAST);
    }

    private void addHoverEffect() {
        this.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                // Update hover color based on current background if it changed externally
                // But typically we stick to the theme.
                // Just set background to hover color.
                setBackground(hoverColor);
                // Checkbox needs manual update if it's opaque
                if (completedCheckBox != null) completedCheckBox.setBackground(hoverColor);
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                // Revert to the stored original color
                setBackground(originalColor);
                if (completedCheckBox != null) completedCheckBox.setBackground(originalColor);
            }
        });
    }

    @Override
    public void setBackground(Color bg) {
        super.setBackground(bg);
        // Update originalColor if set externally (except when hovering)
        // This ensures that if we change the card color programmatically,
        // the hover effect respects it.
        // Simple heuristic: if bg is not the hover color, update original.
        if (hoverColor != null && !bg.equals(hoverColor)) {
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