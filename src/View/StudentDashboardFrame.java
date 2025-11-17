package View;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import Controller.CourseController;
import Controller.StudentController;
import Model.Course;
import Model.Lesson;
import Model.Student;
import View.CommonComponents.CourseCard;
import View.CommonComponents.CourseOverviewPanel;
import View.CommonComponents.LessonCard;
import View.CommonComponents.LessonViewPanel;
import View.StyledComponents.SBtn;
import View.StyledComponents.SLabel;
import View.StyledComponents.SOptionPane;
import View.StyledComponents.StyleColors;
import View.StudentDashboardComponents.StudentNavBar;

public class StudentDashboardFrame extends JFrame {
    private Student student;
    private StudentController studentController;
    private CourseController courseController;

    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JPanel courseCardsPanel;
    private JPanel availableCoursesPanel;
    private StudentNavBar navBar;

    private static final String MAIN_PANEL = "MainPanel";
    private static final String COURSE_VIEW_PANEL = "CourseViewPanel";
    private static final String LESSON_VIEW_PANEL = "LessonViewPanel";

    public StudentDashboardFrame(Student student) {
        this.student = student;
        this.studentController = new StudentController();
        this.courseController = new CourseController();

        setTitle("Student Dashboard");
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

    private void createMainPanel() {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(StyleColors.BACKGROUND);
        navBar = new StudentNavBar(student);
        mainPanel.add(navBar, BorderLayout.NORTH);

        courseCardsPanel = new JPanel();
        courseCardsPanel.setLayout(new BoxLayout(courseCardsPanel, BoxLayout.Y_AXIS));
        courseCardsPanel.setBackground(StyleColors.BACKGROUND);
        courseCardsPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JScrollPane enrolledScrollPane = new JScrollPane(courseCardsPanel);
        enrolledScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        enrolledScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        enrolledScrollPane.setBorder(null);
        enrolledScrollPane.getViewport().setBackground(StyleColors.BACKGROUND);

        availableCoursesPanel = new JPanel();
        availableCoursesPanel.setLayout(new BoxLayout(availableCoursesPanel, BoxLayout.Y_AXIS));
        availableCoursesPanel.setBackground(StyleColors.BACKGROUND);
        availableCoursesPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JScrollPane availableScrollPane = new JScrollPane(availableCoursesPanel);
        availableScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        availableScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        availableScrollPane.setBorder(null);
        availableScrollPane.getViewport().setBackground(StyleColors.BACKGROUND);

        CardLayout coursesCardLayout = new CardLayout();
        JPanel coursesContainer = new JPanel(coursesCardLayout);
        coursesContainer.setBackground(StyleColors.BACKGROUND);
        coursesContainer.add(enrolledScrollPane, "Enrolled");
        coursesContainer.add(availableScrollPane, "Available");

        mainPanel.add(coursesContainer, BorderLayout.CENTER);

        navBar.addEnrolledCoursesButtonListener(e -> {
            coursesCardLayout.show(coursesContainer, "Enrolled");
            navBar.clearSearchText();
            loadEnrolledCourses();
        });
        navBar.addAvailableCoursesButtonListener(e -> {
            coursesCardLayout.show(coursesContainer, "Available");
            navBar.clearSearchText();
            loadAvailableCourses();
        });

        navBar.addSearchButtonListener(e -> {
            String searchText = navBar.getSearchText();
            if (searchText.equals("Search courses...") || searchText.isEmpty()) {
                loadEnrolledCourses();
                loadAvailableCourses();
                return;
            }
            
            // Determine which panel is visible
            if (courseCardsPanel.isShowing()) {
                ArrayList<Course> enrolledCourses = studentController.getEnrolledCourses(student.getId());
                ArrayList<Course> searchResults = courseController.getCourseByTitle(enrolledCourses, searchText.toLowerCase());
                loadCoursesToPanel(courseCardsPanel, searchResults, true);
            } else if (availableCoursesPanel.isShowing()) {
                ArrayList<Course> allCourses = courseController.getAllCourses();
                ArrayList<Integer> enrolledCourseIds = student.getEnrolledCoursesIDs();
                ArrayList<Course> availableCourses = allCourses.stream()
                        .filter(course -> !enrolledCourseIds.contains(course.getCourseId()))
                        .collect(Collectors.toCollection(ArrayList::new));
                ArrayList<Course> searchResults = courseController.getCourseByTitle(availableCourses, searchText.toLowerCase());
                loadCoursesToPanel(availableCoursesPanel, searchResults, false);
            }
        });

        loadEnrolledCourses();
        loadAvailableCourses();
    }

    private void loadCoursesToPanel(JPanel panel, ArrayList<Course> courses, boolean isEnrolled) {
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
                CourseCard card;
                if (isEnrolled) {
                    int completedLessons = studentController.getCompletedLessons(student.getId(), course.getCourseId()).size();
                    card = new CourseCard(course, completedLessons, course.getLessons().size());
                    card.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                            showCourseView(course);
                        }
                    });
                } else {
                    card = new CourseCard(course);
                    card.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                            showAvailableCourseView(course);
                        }
                    });
                }
                card.setMaximumSize(new Dimension(Integer.MAX_VALUE, card.getPreferredSize().height));
                panel.add(card);
                panel.add(javax.swing.Box.createRigidArea(new Dimension(0, 10)));
            }
        }
        panel.revalidate();
        panel.repaint();
    }

    private void loadEnrolledCourses() {
        ArrayList<Course> enrolledCourses = studentController.getEnrolledCourses(student.getId());
        loadCoursesToPanel(courseCardsPanel, enrolledCourses, true);
    }

    private void loadAvailableCourses() {
        ArrayList<Course> allCourses = courseController.getAllCourses();
        ArrayList<Integer> enrolledCourseIds = student.getEnrolledCoursesIDs();
        ArrayList<Course> availableCoursesList = allCourses.stream()
                .filter(course -> !enrolledCourseIds.contains(course.getCourseId()))
                .collect(Collectors.toCollection(ArrayList::new));
        loadCoursesToPanel(availableCoursesPanel, availableCoursesList, false);
    }

    private void showCourseView(Course course) {
        JPanel courseViewPanel = new JPanel(new BorderLayout());
        courseViewPanel.setBackground(StyleColors.BACKGROUND);

        CourseOverviewPanel overviewPanel = new CourseOverviewPanel(course);
        courseViewPanel.add(overviewPanel, BorderLayout.CENTER);

        JPanel sidePanel = new JPanel(new BorderLayout());
        sidePanel.setBackground(StyleColors.BACKGROUND);
        sidePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Progress section
        JPanel progressPanel = new JPanel(new BorderLayout(0, 8));
        progressPanel.setBackground(StyleColors.BACKGROUND);
        progressPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(StyleColors.ACCENT, 2, true),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        SLabel progressLabel = new SLabel("Course Progress");
        progressLabel.setFont(progressLabel.getFont().deriveFont(16f).deriveFont(java.awt.Font.BOLD));
        progressPanel.add(progressLabel, BorderLayout.NORTH);

        JProgressBar progressBar = new JProgressBar(0, course.getLessons().size());
        int completedLessons = studentController.getCompletedLessons(student.getId(), course.getCourseId()).size();
        progressBar.setValue(completedLessons);
        progressBar.setStringPainted(true);
        progressBar.setString(completedLessons + "/" + course.getLessons().size() + " Completed");
        progressBar.setPreferredSize(new Dimension(0, 30));
        progressBar.setForeground(StyleColors.ACCENT);
        progressBar.setBackground(StyleColors.CARD);
        progressBar.setBorder(BorderFactory.createLineBorder(StyleColors.ACCENT, 1, true));
        progressPanel.add(progressBar, BorderLayout.CENTER);

        sidePanel.add(progressPanel, BorderLayout.NORTH);

        // Lessons section
        JPanel lessonsContainer = new JPanel(new BorderLayout());
        lessonsContainer.setBackground(StyleColors.BACKGROUND);
        lessonsContainer.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        SLabel lessonsLabel = new SLabel("Lessons");
        lessonsLabel.setFont(lessonsLabel.getFont().deriveFont(16f).deriveFont(java.awt.Font.BOLD));
        lessonsLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        lessonsContainer.add(lessonsLabel, BorderLayout.NORTH);

        JPanel lessonsPanel = new JPanel();
        lessonsPanel.setLayout(new BoxLayout(lessonsPanel, BoxLayout.Y_AXIS));
        lessonsPanel.setBackground(StyleColors.BACKGROUND);
        lessonsPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        
        java.util.List<Model.Lesson> lessons = course.getLessons();
        for (int i = 0; i < lessons.size(); i++) {
            Model.Lesson lesson = lessons.get(i);
            int lessonIndex = i;
            LessonCard lessonCard = new LessonCard(i + 1, lesson, studentController.getCompletedLessons(student.getId(), course.getCourseId()).contains(lesson.getLessonId()));
            lessonCard.addCheckBoxListener(e -> {
                if (lessonCard.isCompleted()) {
                    studentController.markLessonAsCompleted(student.getId(), course.getCourseId(), lesson.getLessonId());
                } else {
                    studentController.unmarkLessonAsCompleted(student.getId(), course.getCourseId(), lesson.getLessonId());
                }
                int newCompletedLessons = studentController.getCompletedLessons(student.getId(), course.getCourseId()).size();
                progressBar.setValue(newCompletedLessons);
                progressBar.setString(newCompletedLessons + "/" + course.getLessons().size() + " Completed");
            });
            lessonCard.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    showLessonView(course, lessonIndex);
                }
            });
            lessonCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, lessonCard.getPreferredSize().height));
            lessonsPanel.add(lessonCard);
            if (i < lessons.size() - 1) {
                lessonsPanel.add(javax.swing.Box.createRigidArea(new Dimension(0, 8)));
            }
        }
        
        JScrollPane lessonsScrollPane = new JScrollPane(lessonsPanel);
        lessonsScrollPane.setBorder(null);
        lessonsScrollPane.getViewport().setBackground(StyleColors.BACKGROUND);
        lessonsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        lessonsScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        lessonsContainer.add(lessonsScrollPane, BorderLayout.CENTER);

        sidePanel.add(lessonsContainer, BorderLayout.CENTER);

        // Buttons section
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonsPanel.setBackground(StyleColors.BACKGROUND);
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        SBtn backButton = new SBtn("Back");
        backButton.addActionListener(e -> {
            loadEnrolledCourses();
            loadAvailableCourses();
            getContentPane().remove(1);
            cardLayout.show(StudentDashboardFrame.this.getContentPane(), MAIN_PANEL);
        });

        SBtn dropButton = new SBtn("Drop Course");
        dropButton.setBackground(new Color(220, 53, 69));
        dropButton.addActionListener(e -> {
            int result = SOptionPane.showConfirmDialog(
                StudentDashboardFrame.this,
                "Are you sure you want to drop this course?\nAll current progress will be lost.",
                "Drop Course",
                JOptionPane.YES_NO_OPTION
            );
            if (result == JOptionPane.YES_OPTION) {
                studentController.dropCourse(student.getId(), course.getCourseId());
                loadEnrolledCourses();
                loadAvailableCourses();
                getContentPane().remove(1);
                cardLayout.show(StudentDashboardFrame.this.getContentPane(), MAIN_PANEL);
            }
        });

        buttonsPanel.add(backButton);
        buttonsPanel.add(dropButton);
        sidePanel.add(buttonsPanel, BorderLayout.SOUTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, courseViewPanel, sidePanel);
        splitPane.setDividerLocation(700);
        splitPane.setBackground(StyleColors.BACKGROUND);
        splitPane.setBorder(null);

        add(splitPane, COURSE_VIEW_PANEL);
        cardLayout.show(getContentPane(), COURSE_VIEW_PANEL);
    }

    private void showAvailableCourseView(Course course) {
        JPanel courseViewPanel = new JPanel(new BorderLayout());
        courseViewPanel.setBackground(StyleColors.BACKGROUND);

        CourseOverviewPanel overviewPanel = new CourseOverviewPanel(course);
        courseViewPanel.add(overviewPanel, BorderLayout.CENTER);

        JPanel sidePanel = new JPanel(new BorderLayout());
        sidePanel.setBackground(StyleColors.BACKGROUND);
        sidePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Lessons section (no progress bar)
        JPanel lessonsContainer = new JPanel(new BorderLayout());
        lessonsContainer.setBackground(StyleColors.BACKGROUND);
        lessonsContainer.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        SLabel lessonsLabel = new SLabel("Lessons");
        lessonsLabel.setFont(lessonsLabel.getFont().deriveFont(16f).deriveFont(java.awt.Font.BOLD));
        lessonsLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        lessonsContainer.add(lessonsLabel, BorderLayout.NORTH);

        JPanel lessonsPanel = new JPanel();
        lessonsPanel.setLayout(new BoxLayout(lessonsPanel, BoxLayout.Y_AXIS));
        lessonsPanel.setBackground(StyleColors.BACKGROUND);
        lessonsPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        
        java.util.List<Model.Lesson> lessons = course.getLessons();
        for (int i = 0; i < lessons.size(); i++) {
            Model.Lesson lesson = lessons.get(i);
            LessonCard lessonCard = new LessonCard(i + 1, lesson);
            lessonCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, lessonCard.getPreferredSize().height));
            lessonsPanel.add(lessonCard);
            if (i < lessons.size() - 1) {
                lessonsPanel.add(javax.swing.Box.createRigidArea(new Dimension(0, 8)));
            }
        }
        
        JScrollPane lessonsScrollPane = new JScrollPane(lessonsPanel);
        lessonsScrollPane.setBorder(null);
        lessonsScrollPane.getViewport().setBackground(StyleColors.BACKGROUND);
        lessonsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        lessonsScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        lessonsContainer.add(lessonsScrollPane, BorderLayout.CENTER);

        sidePanel.add(lessonsContainer, BorderLayout.CENTER);

        // Buttons section
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonsPanel.setBackground(StyleColors.BACKGROUND);
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        SBtn backButton = new SBtn("Back");
        backButton.addActionListener(e -> {
            loadEnrolledCourses();
            loadAvailableCourses();
            getContentPane().remove(1);
            cardLayout.show(StudentDashboardFrame.this.getContentPane(), MAIN_PANEL);
        });

        SBtn enrollButton = new SBtn("Enroll in Course");
        enrollButton.setBackground(new Color(40, 167, 69));
        enrollButton.addActionListener(e -> {
            studentController.enrollInCourse(student.getId(), course.getCourseId());
            loadEnrolledCourses();
            loadAvailableCourses();
            getContentPane().remove(1);
            showCourseView(course);
        });

        buttonsPanel.add(backButton);
        buttonsPanel.add(enrollButton);
        sidePanel.add(buttonsPanel, BorderLayout.SOUTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, courseViewPanel, sidePanel);
        splitPane.setDividerLocation(700);
        splitPane.setBackground(StyleColors.BACKGROUND);
        splitPane.setBorder(null);

        add(splitPane, COURSE_VIEW_PANEL);
        cardLayout.show(getContentPane(), COURSE_VIEW_PANEL);
    }

    private void showLessonView(Course course, int lessonIndex) {
        if (getContentPane().getComponentCount() > 1) {
            getContentPane().remove(1);
        }

        // Right Panel: Course Side Panel
        JPanel rightPanel = createCourseSidePanel(course);

        // Left Panel: Lesson Content and Navigation
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(StyleColors.BACKGROUND);
        leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        Lesson currentLesson = course.getLessons().get(lessonIndex);
        LessonViewPanel lessonViewPanel = new LessonViewPanel(currentLesson, lessonIndex + 1);
        leftPanel.add(lessonViewPanel, BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
        buttonsPanel.setBackground(StyleColors.BACKGROUND);
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        SBtn backButton = new SBtn("Back to Course");
        backButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        backButton.addActionListener(e -> {
            getContentPane().remove(1);
            showCourseView(course);
        });

        SBtn homeButton = new SBtn("Home");
        homeButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        homeButton.addActionListener(e -> {
            loadEnrolledCourses();
            loadAvailableCourses();
            getContentPane().remove(1);
            cardLayout.show(StudentDashboardFrame.this.getContentPane(), MAIN_PANEL);
        });

        SBtn nextButton = new SBtn("Next Lesson");
        nextButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        nextButton.setBackground(new Color(40, 167, 69));
        nextButton.addActionListener(e -> {
            studentController.markLessonAsCompleted(student.getId(), course.getCourseId(), currentLesson.getLessonId());
            if (lessonIndex + 1 < course.getLessons().size()) {
                showLessonView(course, lessonIndex + 1);
            } else {
                SOptionPane.showMessageDialog(
                    StudentDashboardFrame.this,
                    "Congratulations! You've completed all lessons in this course.",
                    "Course Complete",
                    JOptionPane.INFORMATION_MESSAGE
                );
                getContentPane().remove(1);
                showCourseView(course);
            }
        });

        buttonsPanel.add(backButton);
        buttonsPanel.add(javax.swing.Box.createRigidArea(new Dimension(0, 10)));
        buttonsPanel.add(homeButton);
        buttonsPanel.add(javax.swing.Box.createRigidArea(new Dimension(0, 10)));
        buttonsPanel.add(nextButton);
        
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(StyleColors.BACKGROUND);
        bottomPanel.add(buttonsPanel, BorderLayout.NORTH);
        leftPanel.add(bottomPanel, BorderLayout.SOUTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(800);
        splitPane.setBackground(StyleColors.BACKGROUND);
        splitPane.setBorder(null);

        add(splitPane, LESSON_VIEW_PANEL);
        cardLayout.show(getContentPane(), LESSON_VIEW_PANEL);
    }

    private JPanel createCourseSidePanel(Course course) {
        JPanel sidePanel = new JPanel(new BorderLayout());
        sidePanel.setBackground(StyleColors.BACKGROUND);
        sidePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Unclickable CourseCard at the top
        CourseCard courseCard = new CourseCard(course);
        courseCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, courseCard.getPreferredSize().height));
        sidePanel.add(courseCard, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(StyleColors.BACKGROUND);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        // Progress section
        JPanel progressPanel = new JPanel(new BorderLayout(0, 8));
        progressPanel.setBackground(StyleColors.BACKGROUND);
        progressPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(StyleColors.ACCENT, 2, true),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        SLabel progressLabel = new SLabel("Course Progress");
        progressLabel.setFont(progressLabel.getFont().deriveFont(16f).deriveFont(java.awt.Font.BOLD));
        progressPanel.add(progressLabel, BorderLayout.NORTH);

        JProgressBar progressBar = new JProgressBar(0, course.getLessons().size());
        int completedLessons = studentController.getCompletedLessons(student.getId(), course.getCourseId()).size();
        progressBar.setValue(completedLessons);
        progressBar.setString(completedLessons + "/" + course.getLessons().size() + " Completed");
        progressBar.setStringPainted(true);
        progressBar.setForeground(StyleColors.ACCENT);
        progressBar.setBackground(StyleColors.CARD);
        progressBar.setBorder(BorderFactory.createLineBorder(StyleColors.ACCENT, 1, true));
        progressPanel.add(progressBar, BorderLayout.CENTER);
        centerPanel.add(progressPanel, BorderLayout.NORTH);

        // Lessons section
        JPanel lessonsContainer = new JPanel(new BorderLayout());
        lessonsContainer.setBackground(StyleColors.BACKGROUND);
        lessonsContainer.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        SLabel lessonsLabel = new SLabel("Lessons");
        lessonsLabel.setFont(lessonsLabel.getFont().deriveFont(16f).deriveFont(java.awt.Font.BOLD));
        lessonsLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        lessonsContainer.add(lessonsLabel, BorderLayout.NORTH);

        JPanel lessonsPanel = new JPanel();
        lessonsPanel.setLayout(new BoxLayout(lessonsPanel, BoxLayout.Y_AXIS));
        lessonsPanel.setBackground(StyleColors.BACKGROUND);
        lessonsPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        
        java.util.List<Model.Lesson> lessons = course.getLessons();
        for (int i = 0; i < lessons.size(); i++) {
            Model.Lesson lesson = lessons.get(i);
            int lessonIndex = i;
            LessonCard lessonCard = new LessonCard(i + 1, lesson, studentController.getCompletedLessons(student.getId(), course.getCourseId()).contains(lesson.getLessonId()));
            lessonCard.addCheckBoxListener(e -> {
                if (lessonCard.isCompleted()) {
                    studentController.markLessonAsCompleted(student.getId(), course.getCourseId(), lesson.getLessonId());
                } else {
                    studentController.unmarkLessonAsCompleted(student.getId(), course.getCourseId(), lesson.getLessonId());
                }
                int newCompletedLessons = studentController.getCompletedLessons(student.getId(), course.getCourseId()).size();
                progressBar.setValue(newCompletedLessons);
                progressBar.setString(newCompletedLessons + "/" + course.getLessons().size() + " Completed");
            });
            lessonCard.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    showLessonView(course, lessonIndex);
                }
            });
            lessonCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, lessonCard.getPreferredSize().height));
            lessonsPanel.add(lessonCard);
            if (i < lessons.size() - 1) {
                lessonsPanel.add(javax.swing.Box.createRigidArea(new Dimension(0, 8)));
            }
        }
        
        JScrollPane lessonsScrollPane = new JScrollPane(lessonsPanel);
        lessonsScrollPane.setBorder(null);
        lessonsScrollPane.getViewport().setBackground(StyleColors.BACKGROUND);
        lessonsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        lessonsScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        lessonsContainer.add(lessonsScrollPane, BorderLayout.CENTER);

        centerPanel.add(lessonsContainer, BorderLayout.CENTER);
        sidePanel.add(centerPanel, BorderLayout.CENTER);

        return sidePanel;
    }
}
