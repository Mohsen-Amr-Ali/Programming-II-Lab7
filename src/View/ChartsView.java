package View;
import javax.swing.*;
import java.awt.*;
import org.jfree.chart.*;
import org.jfree.chart.plot.*;
import org.jfree.data.category.DefaultCategoryDataset;

public class ChartsView extends JPanel {

    public ChartsView() {
        setLayout(new GridLayout(3, 1, 10, 10));

        add(createLessonAverageChart());
        add(createCompletionRateChart());
        add(createStudentScoresChart());
    }

    // -------------------------
    // Chart 1 — Lesson Quiz Averages
    // -------------------------
    private JPanel createLessonAverageChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Dummy data
        dataset.addValue(85, "Average Score", "Lesson 1"); //For actual data, replace values with real variables. eg: 85 would be lesson.getAverageQuizScore()
                                                                                 // and "Lesson 1"would be lesson.getTitle()
        dataset.addValue(78, "Average Score", "Lesson 2");
        dataset.addValue(92, "Average Score", "Lesson 3");

        JFreeChart chart = ChartFactory.createBarChart(
                "Quiz Averages per Lesson",
                "Lessons",
                "Average Score",
                dataset,
                PlotOrientation.VERTICAL,
                false, true, false
        );

        return new ChartPanel(chart);
    }

    // -------------------------
    // Chart 2 — Student Completion %
    // -------------------------
    private JPanel createCompletionRateChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Dummy data
        dataset.addValue(100, "Completion %", "Ahmed"); //For actual data, replace values with real variables. eg: "Ahmed" would be student.getName()
                                                                              // and 100 would be student.getCompletionPercentage()
        dataset.addValue(70, "Completion %", "Mona");
        dataset.addValue(40, "Completion %", "Sara");
        dataset.addValue(90, "Completion %", "Ali");

        JFreeChart chart = ChartFactory.createLineChart(
                "Student Completion Percentage",
                "Students",
                "Completion %",
                dataset,
                PlotOrientation.VERTICAL,
                false, true, false
        );

        return new ChartPanel(chart);
    }

    // -------------------------
    // Chart 3 — Student Quiz Scores
    // -------------------------
    private JPanel createStudentScoresChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Dummy data
        dataset.addValue(90, "Score", "Ahmed"); //For actual data, replace values with real variables. eg: "Ahmed" would be student.getName()
                                                                     // and 90 would be student.getQuizScore()
        dataset.addValue(75, "Score", "Mona");
        dataset.addValue(60, "Score", "Sara");
        dataset.addValue(88, "Score", "Ali");

        JFreeChart chart = ChartFactory.createBarChart(
                "Student Quiz Scores",
                "Students",
                "Score",
                dataset,
                PlotOrientation.VERTICAL,
                false, true, false
        );

        return new ChartPanel(chart);
    }
}
