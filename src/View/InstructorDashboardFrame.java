package View;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import Controller.CourseController;
import Controller.InstructorController;
import Model.Course.Course;
import Model.User.Instructor;
import View.CommonComponents.CourseCard;
import View.InstructorComponents.InstructorNavBar;
import View.StyledComponents.SLabel;
import View.StyledComponents.SOptionPane;
import View.StyledComponents.StyleColors;

public class InstructorDashboardFrame extends JFrame {
    private Instructor instructor;
    private InstructorController instructorController;
    private CourseController courseController;

    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JPanel courseCardsPanel;
    private InstructorNavBar navBar;

    private static final String MAIN_PANEL = "MainPanel";

    public InstructorDashboardFrame(Instructor instructor) {
        this.instructor = instructor;
        this.instructorController = new InstructorController();
        this.courseController = new CourseController();

        setTitle("Instructor Dashboard");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        JPanel contentPane = new JPanel(cardLayout);
        contentPane.setBackground(StyleColors.BACKGROUND);
        setContentPane(contentPane);

        createMainPanel();
        add(mainPanel, MAIN_PANEL);

        cardLayout.show(getContentPane(), MAIN_PANEL);
    }

    public void setLogoutListener(Runnable logoutAction) {
        navBar.getLogoutBtn().addActionListener(e -> {
            int result = SOptionPane.showConfirmDialog(
                InstructorDashboardFrame.this,
                "Are you sure you want to exit?",
                "Logout",
                JOptionPane.YES_NO_OPTION
            );
            if (result == JOptionPane.YES_OPTION) {
                logoutAction.run();
            }
        });
    }

    private void createMainPanel() {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(StyleColors.BACKGROUND);
        navBar = new InstructorNavBar(instructor);
        mainPanel.add(navBar, BorderLayout.NORTH);

        courseCardsPanel = new JPanel();
        courseCardsPanel.setLayout(new BoxLayout(courseCardsPanel, BoxLayout.Y_AXIS));
        courseCardsPanel.setBackground(StyleColors.BACKGROUND);
        courseCardsPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JScrollPane createdCoursesScrollPane = new JScrollPane(courseCardsPanel);
        createdCoursesScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        createdCoursesScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        createdCoursesScrollPane.setBorder(null);
        createdCoursesScrollPane.getViewport().setBackground(StyleColors.BACKGROUND);

        mainPanel.add(createdCoursesScrollPane, BorderLayout.CENTER);

        navBar.addSearchButtonListener(e -> {
            String searchText = navBar.getSearchText();
            if (searchText.equals("Search courses...") || searchText.isEmpty()) {
                loadCreatedCourses();
                return;
            }
            
            ArrayList<Course> createdCourses = instructorController.getCreatedCourses(instructor.getId());
            ArrayList<Course> searchResults = courseController.getCourseByTitle(createdCourses, searchText.toLowerCase());
            loadCoursesToPanel(courseCardsPanel, searchResults);
        });

        navBar.addRefreshButtonListener(e -> {
            navBar.clearSearchText();
            loadCreatedCourses();
        });

        loadCreatedCourses();
    }

    private void loadCoursesToPanel(JPanel panel, ArrayList<Course> courses) {
        panel.removeAll();
        
        if (courses.isEmpty()) {
            JPanel noResultsPanel = new JPanel(new BorderLayout());
            noResultsPanel.setBackground(StyleColors.BACKGROUND);
            noResultsPanel.setBorder(BorderFactory.createEmptyBorder(50, 20, 20, 20));
            
            SLabel noResultsLabel = new SLabel("No results found");
            noResultsLabel.setFont(noResultsLabel.getFont().deriveFont(18f).deriveFont(java.awt.Font.BOLD));
            noResultsLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            noResultsPanel.add(noResultsLabel, BorderLayout.CENTER);
            
            panel.add(noResultsPanel);
        } else {
            for (Course course : courses) {
                CourseCard card = new CourseCard(course);
                card.setMaximumSize(new Dimension(Integer.MAX_VALUE, card.getPreferredSize().height));
                panel.add(card);
                panel.add(javax.swing.Box.createRigidArea(new Dimension(0, 10)));
            }
        }
        panel.revalidate();
        panel.repaint();
    }

    private void loadCreatedCourses() {
        ArrayList<Course> createdCourses = instructorController.getCreatedCourses(instructor.getId());
        loadCoursesToPanel(courseCardsPanel, createdCourses);
    }
}
