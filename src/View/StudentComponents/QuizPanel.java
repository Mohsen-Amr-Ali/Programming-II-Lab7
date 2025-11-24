package View.StudentComponents;

import Model.Course.Question;
import Model.Course.Quiz;
import View.StyledComponents.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class QuizPanel extends JPanel {
    private Quiz quiz;
    private HashMap<Integer, Integer> userAnswers; // QuestionID -> Selected Option Index
    private Runnable onSubmitCallback;
    private boolean isSubmitted = false;

    // UI Components
    private JPanel questionsContainer;
    private SBtn submitButton;
    private SLabel scoreLabel;

    public QuizPanel(Quiz quiz, Runnable onSubmitCallback) {
        this.quiz = quiz;
        this.onSubmitCallback = onSubmitCallback;
        this.userAnswers = new HashMap<>();

        setLayout(new BorderLayout());
        setBackground(StyleColors.BACKGROUND);

        // Main container padding
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header (Title & Score)
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(StyleColors.BACKGROUND);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        SLabel titleLabel = new SLabel("Quiz Assessment");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(StyleColors.ACCENT);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        scoreLabel = new SLabel("");
        scoreLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        scoreLabel.setForeground(new Color(40, 167, 69)); // Green
        headerPanel.add(scoreLabel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Questions List (Scrollable)
        questionsContainer = new JPanel();
        questionsContainer.setLayout(new BoxLayout(questionsContainer, BoxLayout.Y_AXIS));
        questionsContainer.setBackground(StyleColors.BACKGROUND);

        renderQuestions();

        SScrollPane scrollPane = new SScrollPane(questionsContainer);
        add(scrollPane, BorderLayout.CENTER);

        // Footer (Submit Button)
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footerPanel.setBackground(StyleColors.BACKGROUND);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        submitButton = new SBtn("Submit Quiz");
        submitButton.setBackground(StyleColors.ACCENT);
        submitButton.setForeground(Color.WHITE);
        submitButton.addActionListener(e -> handleSubmit());

        footerPanel.add(submitButton);
        add(footerPanel, BorderLayout.SOUTH);
    }

    private void renderQuestions() {
        questionsContainer.removeAll();

        for (int i = 0; i < quiz.getQuestions().size(); i++) {
            Question q = quiz.getQuestions().get(i);
            JPanel qPanel = createQuestionPanel(q, i + 1, i); // Pass index as third parameter
            questionsContainer.add(qPanel);
            questionsContainer.add(Box.createVerticalStrut(20)); // Spacing between questions
        }

        questionsContainer.revalidate();
        questionsContainer.repaint();
    }

    private JPanel createQuestionPanel(Question q, int number, int questionIndex) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(StyleColors.CARD);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(StyleColors.ACCENT_DARK, 1, true),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Question Text
        JTextArea qText = new JTextArea(number + ". " + q.getQuestionText());
        qText.setFont(new Font("Segoe UI", Font.BOLD, 16));
        qText.setForeground(StyleColors.TEXT);
        qText.setBackground(StyleColors.CARD);
        qText.setLineWrap(true);
        qText.setWrapStyleWord(true);
        qText.setEditable(false);
        qText.setFocusable(false);
        qText.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(qText);
        panel.add(Box.createVerticalStrut(10));

        // Options
        ButtonGroup group = new ButtonGroup();
        final int qIndex = questionIndex; // Make final for lambda
        int selectedAns = userAnswers.getOrDefault(qIndex, -1); // Use questionIndex
        int correctAns = q.getCorrectAnswerIndex();

        for (int j = 0; j < q.getAnswers().size(); j++) {
            String optionText = q.getAnswers().get(j);
            JRadioButton rb = new JRadioButton(optionText);
            rb.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            rb.setForeground(StyleColors.TEXT);
            rb.setBackground(StyleColors.CARD);
            rb.setFocusPainted(false);
            rb.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            rb.setAlignmentX(Component.LEFT_ALIGNMENT);

            // Interaction Logic
            final int optionIndex = j;
            rb.addActionListener(e -> {
                if (!isSubmitted) {
                    userAnswers.put(qIndex, optionIndex); // Use questionIndex, not questionId
                }
            });

            if (j == selectedAns) {
                rb.setSelected(true);
            }

            // Result Visualization (Only after submission)
            if (isSubmitted) {
                rb.setEnabled(false); // Disable changing answers

                if (j == correctAns) {
                    // Highlight Correct Answer
                    rb.setForeground(new Color(40, 167, 69)); // Green text
                    if (j == selectedAns) {
                        rb.setText(optionText + " (Correct)");
                    } else {
                        rb.setText(optionText + " (Correct Answer)");
                    }
                } else if (j == selectedAns && j != correctAns) {
                    // Highlight Wrong Selection
                    rb.setForeground(new Color(220, 53, 69)); // Red text
                    rb.setText(optionText + " (Your Answer)");
                }
            }

            group.add(rb);
            panel.add(rb);
            panel.add(Box.createVerticalStrut(5));
        }

        return panel;
    }

    private void handleSubmit() {
        // Validation: Check if all questions answered? (Optional)
        // For now, allow partial submission but maybe warn.
        if (userAnswers.size() < quiz.getQuestions().size()) {
            int result = SOptionPane.showConfirmDialog(this,
                    "You haven't answered all questions. Submit anyway?",
                    "Incomplete Quiz",
                    JOptionPane.YES_NO_OPTION);
            if (result != JOptionPane.YES_OPTION) return;
        } else {
            int result = SOptionPane.showConfirmDialog(this,
                    "Are you sure you want to submit?",
                    "Submit Quiz",
                    JOptionPane.YES_NO_OPTION);
            if (result != JOptionPane.YES_OPTION) return;
        }

        isSubmitted = true;
        submitButton.setEnabled(false);
        submitButton.setText("Submitted");

        // Callback to controller to save results, which will in turn update lesson completion
        if (onSubmitCallback != null) {
            onSubmitCallback.run();
        }

        // Calculate local score for display
        int score = 0;
        for (int i = 0; i < quiz.getQuestions().size(); i++) {
            Question q = quiz.getQuestions().get(i);
            int selected = userAnswers.getOrDefault(i, -1); // Use index, not questionId
            if (selected == q.getCorrectAnswerIndex()) {
                score++;
            }
        }

        // Update Score Label
        double percentage = (double) score / quiz.getQuestions().size() * 100;
        String resultText = String.format("Score: %d / %d (%.1f%%)", score, quiz.getQuestions().size(), percentage);
        scoreLabel.setText(resultText);

        if (percentage >= quiz.getPassThreshold()) {
            scoreLabel.setForeground(new Color(40, 167, 69)); // Green
        } else {
            scoreLabel.setForeground(new Color(220, 53, 69)); // Red
        }

        // Re-render to show correct/incorrect highlights
        renderQuestions();
    }

    public HashMap<Integer, Integer> getUserAnswers() {
        return userAnswers;
    }
}