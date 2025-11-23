package Model.Course;

import java.util.ArrayList;

public class Quiz {
    private int quizId;
    private ArrayList<Question> questions;
    private double passThreshold;

    public Quiz(int quizId, ArrayList<Question> questions, double passThreshold) {
        this.quizId = quizId;
        this.questions = questions;
        this.passThreshold = passThreshold;
    }

    public int getQuizId() {
        return quizId;
    }

    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
    }

    public double getPassThreshold() {
        return passThreshold;
    }

    public void setPassThreshold(double passThreshold) {
        this.passThreshold = passThreshold;
    }
}
