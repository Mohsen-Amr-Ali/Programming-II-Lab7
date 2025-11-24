package View.CommonComponents;

import Model.Course.Course;
import Model.Course.Lesson;
import Model.Course.Quiz;
import Model.Course.QuizResult;
import Model.JsonDatabaseManager;
import Model.User.Student;
import Model.User.User;
import View.StyledComponents.StyleColors;
import util.Analytics;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Map;

import org.jfree.chart.*;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

public class ChartsView extends JPanel {
    private Analytics analytics;

    public ChartsView() {
        this.analytics = new Analytics();
        setLayout(new GridLayout(0, 1, 10, 10)); // Flexible grid
        setBackground(StyleColors.BACKGROUND); // Dark theme background
    }

    // Helper method to apply dark theme styling to charts
    private void styleChart(JFreeChart chart) {
        // Chart background
        chart.setBackgroundPaint(StyleColors.BACKGROUND);
        chart.getTitle().setPaint(StyleColors.TEXT);

        // Legend styling
        if (chart.getLegend() != null) {
            chart.getLegend().setBackgroundPaint(StyleColors.BACKGROUND);
            chart.getLegend().setItemPaint(StyleColors.TEXT);
        }

        // Plot styling
        if (chart.getPlot() instanceof CategoryPlot) {
            CategoryPlot plot = chart.getCategoryPlot();
            plot.setBackgroundPaint(StyleColors.CARD);
            plot.setDomainGridlinePaint(StyleColors.HOVER);
            plot.setRangeGridlinePaint(StyleColors.HOVER);
            plot.setOutlinePaint(StyleColors.ACCENT_DARK);

            // Axis styling
            plot.getDomainAxis().setLabelPaint(StyleColors.TEXT);
            plot.getDomainAxis().setTickLabelPaint(StyleColors.TEXT);
            plot.getRangeAxis().setLabelPaint(StyleColors.TEXT);
            plot.getRangeAxis().setTickLabelPaint(StyleColors.TEXT);

            // Bar renderer styling
            BarRenderer renderer = (BarRenderer) plot.getRenderer();
            renderer.setSeriesPaint(0, StyleColors.ACCENT);
        } else if (chart.getPlot() instanceof PiePlot) {
            PiePlot plot = (PiePlot) chart.getPlot();
            plot.setBackgroundPaint(StyleColors.CARD);
            plot.setOutlinePaint(StyleColors.ACCENT_DARK);
            plot.setLabelBackgroundPaint(StyleColors.BACKGROUND);
            plot.setLabelPaint(StyleColors.TEXT);
            plot.setLabelOutlinePaint(StyleColors.ACCENT_DARK);

            // Pie section colors
            plot.setSectionPaint("Completed", new Color(40, 167, 69)); // Green
            plot.setSectionPaint("In Progress", StyleColors.ACCENT);
            plot.setSectionPaint("Approved", new Color(40, 167, 69)); // Green
            plot.setSectionPaint("Pending", new Color(255, 193, 7)); // Yellow
            plot.setSectionPaint("Rejected", new Color(220, 53, 69)); // Red
        }
    }

    // --- STUDENT ANALYTICS ---
    public void updateStudentStats(Student student, JsonDatabaseManager dbManager, Map<String, Double> quizPerformance) {
        removeAll();
        setLayout(new GridLayout(2, 1, 10, 10));

        // 1. Quiz Performance Bar Chart
        DefaultCategoryDataset scoreDataset = new DefaultCategoryDataset();
        for (Map.Entry<String, Double> entry : quizPerformance.entrySet()) {
            scoreDataset.addValue(entry.getValue(), "Highest Score %", entry.getKey());
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

        // 2. Course Progress Overview
        DefaultCategoryDataset progressDataset = new DefaultCategoryDataset();
        Map<String, Double> progressMap = analytics.calculateCourseProgress(student, dbManager);
        for (Map.Entry<String, Double> entry : progressMap.entrySet()) {
            progressDataset.addValue(entry.getValue(), "Completion %", entry.getKey());
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
        styleChart(avgChart);
        ChartPanel avgChartPanel = new ChartPanel(avgChart);
        avgChartPanel.setBackground(StyleColors.BACKGROUND);
        add(avgChartPanel);

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
        styleChart(pieChart);
        ChartPanel pieChartPanel = new ChartPanel(pieChart);
        pieChartPanel.setBackground(StyleColors.BACKGROUND);
        add(pieChartPanel);

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
        styleChart(statusChart);
        ChartPanel statusChartPanel = new ChartPanel(statusChart);
        statusChartPanel.setBackground(StyleColors.BACKGROUND);
        add(statusChartPanel);

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
        styleChart(coursesChart);
        ChartPanel coursesChartPanel = new ChartPanel(coursesChart);
        coursesChartPanel.setBackground(StyleColors.BACKGROUND);
        add(coursesChartPanel);

        revalidate();
        repaint();
    }
}