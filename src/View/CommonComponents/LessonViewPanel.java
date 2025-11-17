package View.CommonComponents;

import Model.Lesson;
import View.StyledComponents.StyleColors;

import javax.swing.*;
import java.awt.*;

public class LessonViewPanel extends JPanel {
	public LessonViewPanel(Lesson lesson, int number) {
		setLayout(new BorderLayout(0, 0));
		setBackground(StyleColors.BACKGROUND);
		setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createEmptyBorder(10, 10, 10, 10),
				BorderFactory.createCompoundBorder(
						BorderFactory.createLineBorder(StyleColors.ACCENT, 2, true),
						BorderFactory.createEmptyBorder(24, 24, 24, 24)
				)
		));

		// Title and number
		JLabel titleLabel = new JLabel("Lesson " + number + ": " + lesson.getTitle());
		titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
		titleLabel.setForeground(StyleColors.TEXT);
		titleLabel.setHorizontalAlignment(SwingConstants.LEFT);
		titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
		topPanel.setBackground(StyleColors.BACKGROUND);
		topPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		topPanel.add(titleLabel);

		add(topPanel, BorderLayout.NORTH);

		// Content area (scrollable)
		JTextArea contentArea = new JTextArea(lesson.getContent());
		contentArea.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		contentArea.setForeground(StyleColors.TEXT);
		contentArea.setBackground(StyleColors.BACKGROUND);
		contentArea.setLineWrap(true);
		contentArea.setWrapStyleWord(true);
		contentArea.setEditable(false);
		contentArea.setFocusable(false);
		contentArea.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));
		contentArea.setAlignmentX(Component.LEFT_ALIGNMENT);

		JScrollPane scrollPane = new JScrollPane(contentArea);
		scrollPane.setBorder(null);
		scrollPane.setBackground(StyleColors.BACKGROUND);
		scrollPane.getViewport().setBackground(StyleColors.BACKGROUND);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

		add(scrollPane, BorderLayout.CENTER);
	}
}
