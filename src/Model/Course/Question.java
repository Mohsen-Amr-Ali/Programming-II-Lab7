package Model.Course;

import java.util.ArrayList;

public class Question {
    private int questionId;
    private final String questionText;
    private final ArrayList<String> answers;
    private final int correctAnswerIndex;

    public Question(String questionText, ArrayList<String> answers, int correctAnswerIndex) {
        this.questionText = questionText;
        this.answers = answers;
        this.correctAnswerIndex = correctAnswerIndex;
    }

    public int getQuestionId() {
        return questionId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public ArrayList<String> getAnswers() {
        return answers;
    }

    public int getCorrectAnswerIndex() {
        return correctAnswerIndex;
    }
}
