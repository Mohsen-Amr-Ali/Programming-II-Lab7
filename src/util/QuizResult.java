package util;

import java.time.LocalDateTime;

public class QuizResult {

    private int score;
    private int maxScore;
    private LocalDateTime attemptDate;
    private boolean passed;

    // constructor + needed setters and getters

    public QuizResult(int score, int maxScore, LocalDateTime attemptDate, boolean passed) {
        this.score = score;
        this.maxScore = maxScore;
        this.attemptDate = attemptDate;
        this.passed = passed;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getMaxScore() {
        return maxScore;
    }

    public LocalDateTime getAttemptDate() {
        return attemptDate;
    }

    public void setAttemptDate(LocalDateTime attemptDate) {
        this.attemptDate = attemptDate;
    }

    public boolean isPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }
}
