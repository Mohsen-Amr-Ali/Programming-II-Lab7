package util;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.chart.plot.PlotOrientation;

import javax.swing.JFrame;
import java.awt.Dimension;
import java.util.Map;

public class Insights {

    // chart generators //
    public void generateCompletionPieChart(double completionPercentage) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("Completed", completionPercentage);
        dataset.setValue("Not Completed", 100 - completionPercentage);

        JFreeChart pieChart = ChartFactory.createPieChart(
                "Course Completion Percentage",
                dataset,
                true, true, false
        );

        showChartInFrame(pieChart, "Insights: Completion Percentage");
    }

    public void generateQuizAverageBarChart(Map<String, Double> avgScores) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Map.Entry<String, Double> entry : avgScores.entrySet()) {
            dataset.addValue(entry.getValue(), "Average Score", entry.getKey());
        }

        JFreeChart barChart = ChartFactory.createBarChart(
                "Quiz Averages per Lesson",
                "Lesson",
                "Average Score",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false
        );

        showChartInFrame(barChart, "Insights: Quiz Averages");
    }

    public void generatePerformanceLineChart(Map<String, Double> timelineScores) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Map.Entry<String, Double> entry : timelineScores.entrySet()) {
            dataset.addValue(entry.getValue(), "Class Performance", entry.getKey());
        }

        JFreeChart lineChart = ChartFactory.createLineChart(
                "Class Performance Timeline",
                "Lesson",
                "Average Score",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false
        );

        showChartInFrame(lineChart, "Insights: Class Performance");
    }

    // tying statistical data with graphs //
    public void showCourseCompletionPercentage (double completionPercentage) {
        generateCompletionPieChart(completionPercentage);
    }

    public void showQuizAverage (Map<String, Double> avgScores) {
        generateQuizAverageBarChart(avgScores);
    }

    public void showTimeline (Map<String, Double> timelineScores) {
        generatePerformanceLineChart(timelineScores);
    }

    // utility //
    private void showChartInFrame(JFreeChart chart, String title) {
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(800, 600));

        JFrame frame = new JFrame(title);
        frame.setContentPane(chartPanel);
        frame.pack();
        frame.setVisible(true);
    }
}
