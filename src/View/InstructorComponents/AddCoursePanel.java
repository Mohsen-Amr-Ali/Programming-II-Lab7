package View.InstructorComponents;

import View.StyledComponents.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class AddCoursePanel {
	private JPanel rootPanel;
	private SLabel titleLabel;
	private STField titleTextField;
	private SLabel descLabel;
	private JTextArea descTextArea;
	private SBtn addButton;

	public AddCoursePanel() {
		// Theme Colors
		Color bg = new Color(25, 25, 35);
		Color card = new Color(40, 40, 60);
		Color text = new Color(220, 220, 235);
		Color accent = new Color(110, 90, 230);

		// Instantiation
		titleLabel = new SLabel("Course Title: ");
		titleTextField = new STField(20);
		descLabel = new SLabel("Course Description: ");
		descTextArea = new JTextArea(6, 20);
		descTextArea.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		descTextArea.setLineWrap(true);
		descTextArea.setWrapStyleWord(true);
		descTextArea.setBackground(card);
		descTextArea.setForeground(text);
		descTextArea.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(accent, 1, true),
				BorderFactory.createEmptyBorder(8, 8, 8, 8)
		));

		JScrollPane descScroll = new JScrollPane(descTextArea);
		descScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		descScroll.setBorder(null);
		descScroll.getViewport().setBackground(card);

		addButton = new SBtn("Add Course");

		// Root Panel
	rootPanel = new JPanel(new GridBagLayout());
	rootPanel.setBackground(bg);
	Border panelBorder = BorderFactory.createTitledBorder(
		BorderFactory.createLineBorder(accent, 2),
		"Add New Course",
		TitledBorder.LEFT,
		TitledBorder.TOP,
		new Font("Segoe UI", Font.BOLD, 16),
		accent
	);
	// Reduce outer and inner padding
	rootPanel.setBorder(BorderFactory.createCompoundBorder(
		BorderFactory.createEmptyBorder(8, 10, 10, 10),
		BorderFactory.createCompoundBorder(panelBorder,
			BorderFactory.createEmptyBorder(6, 8, 8, 8)
		))
	);

	GridBagConstraints gbc = new GridBagConstraints();
	gbc.insets = new Insets(7, 7, 7, 7);

	// Row 0: Title label
	gbc.gridx = 0; gbc.gridy = 0;
	gbc.gridwidth = 1;
	gbc.anchor = GridBagConstraints.WEST;
	gbc.fill = GridBagConstraints.HORIZONTAL;
	rootPanel.add(titleLabel, gbc);

	// Row 1: Title input (full width)
	gbc.gridx = 0; gbc.gridy = 1;
	gbc.gridwidth = 2;
	gbc.weightx = 1.0;
	gbc.fill = GridBagConstraints.HORIZONTAL;
	rootPanel.add(titleTextField, gbc);

	// Row 2: Description label (below title)
	gbc.gridx = 0; gbc.gridy = 2;
	gbc.gridwidth = 2;
	gbc.weightx = 0;
	gbc.fill = GridBagConstraints.HORIZONTAL;
	rootPanel.add(descLabel, gbc);

	// Row 3: Description area (full width, max height)
	gbc.gridx = 0; gbc.gridy = 3;
	gbc.gridwidth = 2;
	gbc.weightx = 1.0;
	gbc.weighty = 1.0;
	gbc.fill = GridBagConstraints.BOTH;
	rootPanel.add(descScroll, gbc);

	// Row 4: Add button
	gbc.gridx = 0; gbc.gridy = 4;
	gbc.gridwidth = 2;
	gbc.weightx = 0;
	gbc.weighty = 0;
	gbc.fill = GridBagConstraints.NONE;
	gbc.anchor = GridBagConstraints.CENTER;
	rootPanel.add(addButton, gbc);
	}

	public JPanel getRootPanel() {
		return rootPanel;
	}

	public String getTitleText() {
		return titleTextField.getText();
	}

	public void setTitleText(String text) {
		titleTextField.setText(text);
	}

	public String getDescriptionText() {
		return descTextArea.getText();
	}

	public void setDescriptionText(String text) {
		descTextArea.setText(text);
	}
}
