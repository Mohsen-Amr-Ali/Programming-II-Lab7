package View.CommonComponents;

import Model.Lesson;
import View.StyledComponents.*;

import javax.swing.*;
import java.awt.*;

public class LessonCard extends JPanel {
	private JLabel titleLabel;
	private SCheckBox completedCheckBox;
	private Color baseColor = StyleColors.CARD;
	private Color hoverColor = StyleColors.lightenColor(baseColor, 0.12f);

	// First constructor: number and lesson
	public LessonCard(int number, Lesson lesson) {
		setLayout(new BorderLayout(12, 0));
		setBackground(baseColor);
		setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(hoverColor, 2, true),
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
		completedCheckBox.setBackground(baseColor);
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
				setBackground(hoverColor);
				setBorder(BorderFactory.createCompoundBorder(
						BorderFactory.createLineBorder(hoverColor, 2, true),
						BorderFactory.createEmptyBorder(12, 18, 12, 18)
				));
				if (completedCheckBox != null) completedCheckBox.setBackground(hoverColor);
			}
			@Override
			public void mouseExited(java.awt.event.MouseEvent e) {
				setBackground(baseColor);
				setBorder(BorderFactory.createCompoundBorder(
						BorderFactory.createLineBorder(hoverColor, 2, true),
						BorderFactory.createEmptyBorder(12, 18, 12, 18)
				));
				if (completedCheckBox != null) completedCheckBox.setBackground(baseColor);
			}
		});
	}

	public boolean isCompleted() {
		return completedCheckBox.isSelected();
	}

	public void setCompleted(boolean completed) {
		completedCheckBox.setSelected(completed);
	}

	public void addCheckBoxListener(java.awt.event.ActionListener listener) {
		completedCheckBox.addActionListener(listener);
	}
}
