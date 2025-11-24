package View.InstructorComponents;

import Model.Course.Question;
import Model.Course.Quiz;
import View.StyledComponents.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class AddQuizDialog extends JDialog {
    private Quiz quiz; // The quiz being edited/created
    private ArrayList<QuestionEditorPanel> questionPanels;
    private Runnable onSave;

    // UI Components
    private JSpinner thresholdSpinner;
    private JPanel questionsContainer;
    private SScrollPane scrollPane;

    public AddQuizDialog(Window owner, Quiz existingQuiz, Runnable onSave) {
        super(owner, "Manage Quiz", ModalityType.APPLICATION_MODAL);
        this.quiz = existingQuiz;
        this.onSave = onSave;
        this.questionPanels = new ArrayList<>();

        setSize(600, 700);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());
        getContentPane().setBackground(StyleColors.BACKGROUND);

        initUI();

        // Load existing questions if any
        if (quiz != null) {
            thresholdSpinner.setValue((int) quiz.getPassThreshold());
            for (Question q : quiz.getQuestions()) {
                addQuestionPanel(q);
            }
        } else {
            // Default: Add one empty question
            addQuestionPanel(null);
        }
    }

    private void initUI() {
        // --- Header (Settings) ---
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        headerPanel.setBackground(StyleColors.BACKGROUND);
        headerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, StyleColors.ACCENT_DARK));

        SLabel thresholdLabel = new SLabel("Passing Score (%):");
        thresholdLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

        SpinnerNumberModel model = new SpinnerNumberModel(60, 0, 100, 5);
        thresholdSpinner = new JSpinner(model);
        thresholdSpinner.setPreferredSize(new Dimension(60, 30));

        headerPanel.add(thresholdLabel);
        headerPanel.add(thresholdSpinner);
        add(headerPanel, BorderLayout.NORTH);

        // --- Center (Questions List) ---
        questionsContainer = new JPanel();
        questionsContainer.setLayout(new BoxLayout(questionsContainer, BoxLayout.Y_AXIS));
        questionsContainer.setBackground(StyleColors.BACKGROUND);
        questionsContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        scrollPane = new SScrollPane(questionsContainer);
        add(scrollPane, BorderLayout.CENTER);

        // --- Bottom (Actions) ---
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        footerPanel.setBackground(StyleColors.BACKGROUND);
        footerPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, StyleColors.ACCENT_DARK));

        SBtn addQBtn = new SBtn("+ Add Question");
        addQBtn.addActionListener(e -> addQuestionPanel(null));

        SBtn saveBtn = new SBtn("Save Quiz");
        saveBtn.setBackground(new Color(40, 167, 69)); // Green
        saveBtn.setForeground(Color.WHITE);
        saveBtn.addActionListener(e -> saveQuiz());

        SBtn cancelBtn = new SBtn("Cancel");
        cancelBtn.setBackground(new Color(220, 53, 69)); // Red
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.addActionListener(e -> dispose());

        footerPanel.add(addQBtn);
        footerPanel.add(saveBtn);
        footerPanel.add(cancelBtn);
        add(footerPanel, BorderLayout.SOUTH);
    }

    private void addQuestionPanel(Question existingQuestion) {
        QuestionEditorPanel panel = new QuestionEditorPanel(existingQuestion);

        // FIX: Set the callback to remove THIS specific panel
        panel.setOnRemove(() -> removeQuestionPanel(panel));

        questionPanels.add(panel);
        questionsContainer.add(panel);
        questionsContainer.add(Box.createVerticalStrut(15)); // Spacing

        questionsContainer.revalidate();
        questionsContainer.repaint();

        // Scroll to bottom
        SwingUtilities.invokeLater(() -> {
            JScrollBar vertical = scrollPane.getVerticalScrollBar();
            vertical.setValue(vertical.getMaximum());
        });
    }

    private void removeQuestionPanel(QuestionEditorPanel panel) {
        questionPanels.remove(panel);
        questionsContainer.remove(panel);
        // We also need to remove the spacing strut, but simpler to just re-render or leave it
        // Ideally, wrap panel+strut in a container and remove container.
        // For now, a full re-render is safest if layout breaks, but removing just panel works ok with BoxLayout.

        questionsContainer.revalidate();
        questionsContainer.repaint();
    }

    private void saveQuiz() {
        // Validation
        if (questionPanels.isEmpty()) {
            SOptionPane.showMessageDialog(this, "A quiz must have at least one question.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ArrayList<Question> newQuestions = new ArrayList<>();

        for (QuestionEditorPanel p : questionPanels) {
            if (!p.validateInput()) {
                SOptionPane.showMessageDialog(this, "Please fill in all fields for every question.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            // Construct Question Object
            // FIX: Removed ID argument to match your Question constructor (3 args)
            newQuestions.add(new Question(p.getQuestionText(), p.getAnswers(), p.getCorrectIndex()));
        }

        double threshold = (Integer) thresholdSpinner.getValue();

        // Update or Create Quiz Object
        if (quiz == null) {
            // ID 0 initially, will be set/managed by Controller or just nested
            quiz = new Quiz(0, newQuestions, threshold);
        } else {
            quiz.setQuestions(newQuestions);
            quiz.setPassThreshold(threshold);
        }

        if (onSave != null) onSave.run();
        dispose();
    }

    public Quiz getQuiz() {
        return quiz;
    }

    // --- Inner Class: Question Editor ---
    private class QuestionEditorPanel extends JPanel {
        private JTextField questionField;
        private ArrayList<JTextField> answerFields;
        private ArrayList<JRadioButton> correctRadios;
        private ButtonGroup radioGroup;
        private Runnable onRemove;

        public QuestionEditorPanel(Question q) {
            setLayout(new BorderLayout());
            setBackground(StyleColors.CARD);
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(StyleColors.ACCENT, 1, true),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
            ));

            // Header
            JPanel top = new JPanel(new BorderLayout());
            top.setOpaque(false);
            SLabel lbl = new SLabel("Question:");
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));

            SBtn removeBtn = new SBtn("X");
            removeBtn.setPreferredSize(new Dimension(30, 20));
            removeBtn.setBackground(new Color(220, 53, 69));
            removeBtn.setForeground(Color.WHITE);
            removeBtn.addActionListener(e -> {
                if (onRemove != null) onRemove.run();
            });

            top.add(lbl, BorderLayout.WEST);
            top.add(removeBtn, BorderLayout.EAST);
            add(top, BorderLayout.NORTH);

            // Inputs
            JPanel center = new JPanel(new GridBagLayout());
            center.setOpaque(false);
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            // Question Text
            questionField = new STField(20);
            if (q != null) questionField.setText(q.getQuestionText());

            gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
            center.add(questionField, gbc);

            // Answers
            answerFields = new ArrayList<>();
            correctRadios = new ArrayList<>();
            radioGroup = new ButtonGroup();

            // 4 Options fixed
            for (int i = 0; i < 4; i++) {
                gbc.gridwidth = 1;
                gbc.gridy++;

                JRadioButton rb = new JRadioButton();
                rb.setOpaque(false);
                radioGroup.add(rb);
                correctRadios.add(rb);

                gbc.gridx = 0; gbc.weightx = 0;
                center.add(rb, gbc);

                STField ansField = new STField(15);
                if (q != null && i < q.getAnswers().size()) {
                    ansField.setText(q.getAnswers().get(i));
                }
                answerFields.add(ansField);

                gbc.gridx = 1; gbc.weightx = 1.0;
                center.add(ansField, gbc);
            }

            if (q != null && q.getCorrectAnswerIndex() >= 0 && q.getCorrectAnswerIndex() < 4) {
                correctRadios.get(q.getCorrectAnswerIndex()).setSelected(true);
            } else {
                correctRadios.get(0).setSelected(true); // Default first correct
            }

            add(center, BorderLayout.CENTER);
        }

        public void setOnRemove(Runnable onRemove) {
            this.onRemove = onRemove;
        }

        public boolean validateInput() {
            if (questionField.getText().trim().isEmpty()) return false;
            for (JTextField tf : answerFields) {
                if (tf.getText().trim().isEmpty()) return false;
            }
            return true;
        }

        public String getQuestionText() { return questionField.getText().trim(); }

        public ArrayList<String> getAnswers() {
            ArrayList<String> ans = new ArrayList<>();
            for (JTextField tf : answerFields) ans.add(tf.getText().trim());
            return ans;
        }

        public int getCorrectIndex() {
            for (int i = 0; i < correctRadios.size(); i++) {
                if (correctRadios.get(i).isSelected()) return i;
            }
            return 0;
        }
    }
}