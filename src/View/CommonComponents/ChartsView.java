package View.CommonComponents;

import Model.Course.Course;
import Model.Course.Lesson;
import Model.Course.Quiz;
import Model.Course.QuizResult;
import Model.JsonDatabaseManager;
import Model.User.Student;
import Model.User.User;
import util.Analytics;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Map;

import org.jfree.chart.*;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

public class ChartsView extends JPanel {
    private Analytics analytics;

    public ChartsView() {
        this.analytics = new Analytics();
        setLayout(new GridLayout(0, 1, 10, 10)); // Flexible grid
        setBackground(Color.WHITE); // Or theme color if preferred
    }

    // --- STUDENT ANALYTICS ---
    public void updateStudentStats(Student student) {
        removeAll();
        setLayout(new GridLayout(2, 1, 10, 10));

        // 1. Quiz Performance Bar Chart
        DefaultCategoryDataset scoreDataset = new DefaultCategoryDataset();
        JsonDatabaseManager db = JsonDatabaseManager.getInstance();

        // Iterate through enrolled courses to gather quiz data
        for (int courseId : student.getEnrolledCoursesIDs()) {
            Course course = db.getCourseById(courseId);
            if (course == null) continue;

            for (Lesson lesson : course.getLessons()) {
                if (lesson.getQuiz() != null) {
                    QuizResult result = student.getLatestQuizResult(lesson.getQuiz());
                    if (result != null) {
                        // Normalize score to %
                        double percent = (double) result.getScore() / result.getMaxScore() * 100;
                        scoreDataset.addValue(percent, "Score %", lesson.getTitle());
                    }
                }
            }
        }

        JFreeChart scoreChart = ChartFactory.createBarChart(
                "My Quiz Performance",
                "Lesson",
                "Score (%)",
                scoreDataset,
                PlotOrientation.VERTICAL,
                false, true, false
        );
        add(new ChartPanel(scoreChart));

        // 2. Course Progress Overview (Simple Bar for all enrolled courses)
        DefaultCategoryDataset progressDataset = new DefaultCategoryDataset();
        for (int courseId : student.getEnrolledCoursesIDs()) {
            Course course = db.getCourseById(courseId);
            if (course != null) {
                double progress = analytics.getLessonProgress(student, course);
                progressDataset.addValue(progress, "Completion %", course.getTitle());
            }
        }

        JFreeChart progressChart = ChartFactory.createBarChart(
                "My Course Progress",
                "Course",
                "Completion (%)",
                progressDataset,
                PlotOrientation.HORIZONTAL,
                false, true, false
        );
        add(new ChartPanel(progressChart));

        revalidate();
        repaint();
    }

    // --- INSTRUCTOR ANALYTICS ---
    public void updateInstructorStats(Course course) {
        removeAll();
        setLayout(new GridLayout(2, 1, 10, 10));

        // 1. Class Performance (Average Quiz Scores per Lesson)
        Map<String, Double> avgScores = analytics.getAverageQuizScorePerLesson(course);
        DefaultCategoryDataset avgDataset = new DefaultCategoryDataset();

        for (Map.Entry<String, Double> entry : avgScores.entrySet()) {
            avgDataset.addValue(entry.getValue(), "Class Average", entry.getKey());
        }

        JFreeChart avgChart = ChartFactory.createBarChart(
                "Class Average Quiz Scores",
                "Lesson",
                "Average Score (%)",
                avgDataset,
                PlotOrientation.VERTICAL,
                false, true, false
        );
        add(new ChartPanel(avgChart));

        // 2. Course Completion Rate (Pie Chart)
        double completionRate = analytics.getCourseCompletionPercentage(course);
        DefaultPieDataset pieDataset = new DefaultPieDataset();
        pieDataset.setValue("Completed", completionRate);
        pieDataset.setValue("In Progress", 100 - completionRate);

        JFreeChart pieChart = ChartFactory.createPieChart(
                "Student Completion Rate",
                pieDataset,
                true, true, false
        );
        add(new ChartPanel(pieChart));

        revalidate();
        repaint();
    }

    // --- ADMIN ANALYTICS ---
    public void updateAdminStats(JsonDatabaseManager db) {
        removeAll();
        setLayout(new GridLayout(2, 1, 10, 10));

        // 1. System User Distribution
        // Note: Assuming we can get full lists from DB manager.
        // Since getCourses() is public, we use that.
        // For users, we might need to add getters to JsonDatabaseManager or rely on what's available.
        // Assuming 'courses' are available. User counts might require new methods in DB Manager.
        // We will visualize Course Status distribution for now as it's accessible.

        int pending = 0;
        int approved = 0;
        int rejected = 0;

        for (Course c : db.getCourses()) {
            // Fix: Add null safety check for status
            if (c.getStatus() == null) {
                pending++; // Count null status as pending by default
                continue;
            }

            switch (c.getStatus()) {
                case PENDING: pending++; break;
                case APPROVED: approved++; break;
                case REJECTED: rejected++; break;
            }
        }

        DefaultPieDataset statusDataset = new DefaultPieDataset();
        statusDataset.setValue("Approved", approved);
        statusDataset.setValue("Pending", pending);
        statusDataset.setValue("Rejected", rejected);

        JFreeChart statusChart = ChartFactory.createPieChart(
                "Course Approval Status",
                statusDataset,
                true, true, false
        );
        add(new ChartPanel(statusChart));

        // 2. Pending vs Total Courses (Bar)
        DefaultCategoryDataset courseDataset = new DefaultCategoryDataset();
        courseDataset.addValue(db.getCourses().size(), "Count", "Total Courses");
        courseDataset.addValue(pending, "Count", "Pending Review");

        JFreeChart coursesChart = ChartFactory.createBarChart(
                "Platform Content Overview",
                "Category",
                "Count",
                courseDataset,
                PlotOrientation.VERTICAL,
                false, true, false
        );
        add(new ChartPanel(coursesChart));

        revalidate();
        repaint();
    }
}