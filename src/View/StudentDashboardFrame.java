package View;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JDialog;
import javax.swing.JFileChooser; // For file saving
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import Controller.CourseController;
import Controller.StudentController;
import Model.Course.Course;
import Model.Course.Lesson;
import Model.Course.COURSE_STATUS;
import Model.User.Student;
import View.CommonComponents.ChartsView;
import View.CommonComponents.CourseCard;
import View.CommonComponents.CourseOverviewPanel;
import View.CommonComponents.LessonCard;
import View.CommonComponents.LessonViewPanel;
import View.StudentComponents.QuizPanel;
import View.StyledComponents.*;
import View.StudentComponents.StudentNavBar;

public class StudentDashboardFrame extends JFrame {
    // Set the parent path for all file operations
    private static final String parentPath = "Y:\\AlexU\\Term 5\\Programming 2\\Programming-II-Lab7\\";
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

        setTitle("Student Dashboard - SkillForge");
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
                    StudentDashboardFrame.this,
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

        navBar = new StudentNavBar(student);
        mainPanel.add(navBar, BorderLayout.NORTH);

        // --- Course Lists ---
        courseCardsPanel = new JPanel();
        courseCardsPanel.setLayout(new BoxLayout(courseCardsPanel, BoxLayout.Y_AXIS));
        courseCardsPanel.setBackground(StyleColors.BACKGROUND);
        courseCardsPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        SScrollPane enrolledScrollPane = new SScrollPane(courseCardsPanel);

        availableCoursesPanel = new JPanel();
        availableCoursesPanel.setLayout(new BoxLayout(availableCoursesPanel, BoxLayout.Y_AXIS));
        availableCoursesPanel.setBackground(StyleColors.BACKGROUND);
        availableCoursesPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        SScrollPane availableScrollPane = new SScrollPane(availableCoursesPanel);

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

        // Stats Button Listener
        navBar.addMyStatsButtonListener(e -> showStatsDialog());

        navBar.addSearchButtonListener(e -> {
            String searchText = navBar.getSearchText();
            if (searchText.equals("Search courses...") || searchText.isEmpty()) {
                loadEnrolledCourses();
                loadAvailableCourses();
                return;
            }

            if (courseCardsPanel.isShowing()) {
                ArrayList<Course> enrolledCourses = studentController.getEnrolledCourses(student.getId());
                ArrayList<Course> searchResults = courseController.getCourseByTitle(enrolledCourses, searchText.toLowerCase());
                loadCoursesToPanel(courseCardsPanel, searchResults, true);
            } else if (availableCoursesPanel.isShowing()) {
                ArrayList<Course> allCourses = courseController.getAllCourses();
                ArrayList<Integer> enrolledCourseIds = student.getEnrolledCoursesIDs();
                ArrayList<Course> availableCourses = allCourses.stream()
                        .filter(course -> !enrolledCourseIds.contains(course.getCourseId()))
                        .filter(course -> course.getStatus() == COURSE_STATUS.APPROVED)
                        .collect(Collectors.toCollection(ArrayList::new));
                ArrayList<Course> searchResults = courseController.getCourseByTitle(availableCourses, searchText.toLowerCase());
                loadCoursesToPanel(availableCoursesPanel, searchResults, false);
            }
        });

        loadEnrolledCourses();
        loadAvailableCourses();
    }

    private void showStatsDialog() {
        JDialog statsDialog = new JDialog(this, "My Statistics", true);
        statsDialog.setSize(900, 600);
        statsDialog.setLocationRelativeTo(this);

        ChartsView chartsView = new ChartsView();
        chartsView.updateStudentStats(student);

        statsDialog.setContentPane(chartsView);
        statsDialog.setVisible(true);
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
                card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
                panel.add(card);
                panel.add(Box.createRigidArea(new Dimension(0, 10)));
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
                .filter(course -> course.getStatus() == COURSE_STATUS.APPROVED)
                .collect(Collectors.toCollection(ArrayList::new));
        loadCoursesToPanel(availableCoursesPanel, availableCoursesList, false);
    }

    // --- COURSE VIEW ---

    private void showCourseView(Course course) {
        JPanel courseViewPanel = new JPanel(new BorderLayout());
        courseViewPanel.setBackground(StyleColors.BACKGROUND);

        CourseOverviewPanel overviewPanel = new CourseOverviewPanel(course);
        courseViewPanel.add(overviewPanel, BorderLayout.CENTER);

        JPanel sidePanel = new JPanel(new BorderLayout());
        sidePanel.setBackground(StyleColors.BACKGROUND);
        sidePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- NEW: Action Panel for Certificate ---
        // Check if course is complete
        boolean isComplete = student.isCourseComplete(course);
        if (isComplete) {
            JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
            actionPanel.setBackground(StyleColors.CARD);
            actionPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(40, 167, 69), 1, true), // Green border
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
            ));

            SLabel congratsLabel = new SLabel("Course Completed!");
            congratsLabel.setForeground(new Color(40, 167, 69));
            congratsLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));

            SBtn certBtn = new SBtn("Download Certificate");
            certBtn.setBackground(new Color(255, 193, 7)); // Gold
            certBtn.setForeground(Color.BLACK);
            certBtn.addActionListener(e -> {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Save Certificate");
                fileChooser.setSelectedFile(new File(parentPath + "Database\\Assets\\Certificate_" + course.getTitle().replaceAll(" ", "_") + ".pdf"));
                fileChooser.setFileFilter(new FileNameExtensionFilter("PDF Documents", "pdf"));

                int userSelection = fileChooser.showSaveDialog(this);
                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    File fileToSave = fileChooser.getSelectedFile();
                    // Ensure extension
                    if (!fileToSave.getAbsolutePath().endsWith(".pdf")) {
                        fileToSave = new File(fileToSave + ".pdf");
                    }

                    boolean success = studentController.generateCertificate(student.getId(), course.getCourseId(), fileToSave.getAbsolutePath());
                    if (success) {
                        SOptionPane.showMessageDialog(this, "Certificate saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        SOptionPane.showMessageDialog(this, "Failed to generate certificate.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            actionPanel.add(congratsLabel);
            actionPanel.add(certBtn);
            sidePanel.add(actionPanel, BorderLayout.NORTH);
        } else {
            // If not complete, maybe just progress bar at top
            // We reuse the progress section logic below but move it down if action panel exists
            // Actually, let's put progress in CENTER of sidePanel if action is NORTH.
        }

        // Progress (Moved to a wrapper if Action Panel is present, or just add below)
        // BorderLayout NORTH can only take one component.
        // Let's create a top container for SidePanel.
        JPanel sideTopContainer = new JPanel();
        sideTopContainer.setLayout(new BoxLayout(sideTopContainer, BoxLayout.Y_AXIS));
        sideTopContainer.setBackground(StyleColors.BACKGROUND);

        // Add Action Panel to container if exists
        if (isComplete) {
            // We need to retrieve the action panel created above...
            // Refactoring: Add to sideTopContainer directly.
            // (Recreating logic for cleaner code flow)
            // See above block, instead of sidePanel.add(..., NORTH), add to sideTopContainer.
            // I will just do it all in one flow.
        }

        // --- Progress Section ---
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

        // Add progress to container
        sideTopContainer.add(progressPanel);
        sideTopContainer.add(Box.createVerticalStrut(10));

        // Add Certificate Action if complete
        if (isComplete) {
            JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
            actionPanel.setBackground(StyleColors.CARD);
            actionPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(40, 167, 69), 1, true),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
            ));
            SBtn certBtn = new SBtn("Download Certificate");
            certBtn.setBackground(new Color(255, 193, 7));
            certBtn.setForeground(Color.BLACK);
            certBtn.addActionListener(e -> {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Save Certificate");
                fileChooser.setSelectedFile(new File("Certificate_" + course.getTitle().replaceAll(" ", "_") + ".pdf"));
                fileChooser.setFileFilter(new FileNameExtensionFilter("PDF Documents", "pdf"));
                if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    if (!file.getAbsolutePath().endsWith(".pdf")) file = new File(file + ".pdf");
                    if (studentController.generateCertificate(student.getId(), course.getCourseId(), file.getAbsolutePath())) {
                        SOptionPane.showMessageDialog(this, "Certificate saved!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        SOptionPane.showMessageDialog(this, "Generation failed.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
            actionPanel.add(certBtn);
            sideTopContainer.add(actionPanel, 0); // Add to top
        }

        sidePanel.add(sideTopContainer, BorderLayout.NORTH);

        // Lessons
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

        java.util.List<Lesson> lessons = course.getLessons();
        for (int i = 0; i < lessons.size(); i++) {
            Lesson lesson = lessons.get(i);
            int lessonIndex = i;

            boolean passed = studentController.getCompletedLessons(student.getId(), course.getCourseId()).contains(lesson.getLessonId());
            LessonCard lessonCard = new LessonCard(i + 1, lesson, passed);

            lessonCard.addCheckBoxListener(e -> {
                if (lesson.getQuiz() != null) {
                    lessonCard.setCompleted(passed);
                    SOptionPane.showMessageDialog(this, "Pass the quiz to complete this lesson.", "Locked", JOptionPane.WARNING_MESSAGE);
                } else {
                    if (lessonCard.isCompleted()) {
                        studentController.markLessonAsCompleted(student.getId(), course.getCourseId(), lesson.getLessonId());
                    } else {
                        studentController.unmarkLessonAsCompleted(student.getId(), course.getCourseId(), lesson.getLessonId());
                    }
                    int newCompletedLessons = studentController.getCompletedLessons(student.getId(), course.getCourseId()).size();
                    progressBar.setValue(newCompletedLessons);
                    progressBar.setString(newCompletedLessons + "/" + course.getLessons().size() + " Completed");

                    // Check for course completion after update
                    if (student.isCourseComplete(course)) {
                        showCourseView(course); // Refresh to show certificate button
                    }
                }
            });

            lessonCard.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    showLessonView(course, lessonIndex);
                }
            });

            lessonCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
            lessonsPanel.add(lessonCard);
            if (i < lessons.size() - 1) {
                lessonsPanel.add(Box.createRigidArea(new Dimension(0, 8)));
            }
        }

        SScrollPane lessonsScrollPane = new SScrollPane(lessonsPanel);
        lessonsContainer.add(lessonsScrollPane, BorderLayout.CENTER);

        sidePanel.add(lessonsContainer, BorderLayout.CENTER);

        // Buttons
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

        SSplitPane splitPane = new SSplitPane(JSplitPane.HORIZONTAL_SPLIT, courseViewPanel, sidePanel);
        splitPane.setDividerLocation(700);

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

        java.util.List<Lesson> lessons = course.getLessons();
        for (int i = 0; i < lessons.size(); i++) {
            Lesson lesson = lessons.get(i);
            LessonCard lessonCard = new LessonCard(i + 1, lesson);
            lessonCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
            lessonsPanel.add(lessonCard);
            if (i < lessons.size() - 1) {
                lessonsPanel.add(Box.createRigidArea(new Dimension(0, 8)));
            }
        }

        SScrollPane lessonsScrollPane = new SScrollPane(lessonsPanel);
        lessonsContainer.add(lessonsScrollPane, BorderLayout.CENTER);
        sidePanel.add(lessonsContainer, BorderLayout.CENTER);

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

        SSplitPane splitPane = new SSplitPane(JSplitPane.HORIZONTAL_SPLIT, courseViewPanel, sidePanel);
        splitPane.setDividerLocation(700);

        add(splitPane, COURSE_VIEW_PANEL);
        cardLayout.show(getContentPane(), COURSE_VIEW_PANEL);
    }

    private void showLessonView(Course course, int lessonIndex) {
        if (getContentPane().getComponentCount() > 1) {
            getContentPane().remove(1);
        }

        JPanel rightPanel = createCourseSidePanel(course);
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

        // --- Quiz / Next Logic ---
        SBtn nextButton = new SBtn("Next Lesson");
        nextButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        if (currentLesson.getQuiz() != null) {
            boolean passed = student.hasPassedQuiz(currentLesson.getLessonId(), currentLesson.getQuiz().getPassThreshold());

            SBtn quizButton = new SBtn(passed ? "Retake Quiz (Passed)" : "Take Quiz");
            quizButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
            quizButton.setBackground(passed ? StyleColors.ACCENT : new Color(255, 193, 7));
            if (passed) quizButton.setBackground(new Color(40, 167, 69));

            quizButton.addActionListener(e -> {
                openQuiz(currentLesson, course, lessonIndex);
            });

            buttonsPanel.add(quizButton);
            buttonsPanel.add(Box.createRigidArea(new Dimension(0, 10)));

            if (!passed) {
                nextButton.setEnabled(false);
                nextButton.setToolTipText("You must pass the quiz to proceed.");
            }
        } else {
            nextButton.addActionListener(e -> {
                studentController.markLessonAsCompleted(student.getId(), course.getCourseId(), currentLesson.getLessonId());
                proceedToNext(course, lessonIndex);
            });
        }

        if (currentLesson.getQuiz() != null) {
            nextButton.addActionListener(e -> proceedToNext(course, lessonIndex));
        }

        nextButton.setBackground(new Color(40, 167, 69));

        buttonsPanel.add(backButton);
        buttonsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonsPanel.add(homeButton);
        buttonsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonsPanel.add(nextButton);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(StyleColors.BACKGROUND);
        bottomPanel.add(buttonsPanel, BorderLayout.NORTH);
        leftPanel.add(bottomPanel, BorderLayout.SOUTH);

        SSplitPane splitPane = new SSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(800);
        splitPane.setBackground(StyleColors.BACKGROUND);
        splitPane.setBorder(null);

        add(splitPane, LESSON_VIEW_PANEL);
        cardLayout.show(getContentPane(), LESSON_VIEW_PANEL);
    }

    // Helper to open quiz and handle submission cleanly
    private void openQuiz(Lesson lesson, Course course, int lessonIndex) {
        final QuizPanel[] panelHolder = new QuizPanel[1];

        Runnable onSubmit = () -> {
            studentController.submitQuiz(
                    student.getId(),
                    course.getCourseId(),
                    lesson.getLessonId(),
                    panelHolder[0].getUserAnswers()
            );
            // Reload lesson view to show updated status
            showLessonView(course, lessonIndex);
        };

        panelHolder[0] = new QuizPanel(lesson.getQuiz(), onSubmit);

        // Re-render the split pane with the quiz
        JPanel rightPanel = createCourseSidePanel(course);

        // Wrap quiz in container with back button
        JPanel quizWrapper = new JPanel(new BorderLayout());
        quizWrapper.setBackground(StyleColors.BACKGROUND);
        quizWrapper.add(panelHolder[0], BorderLayout.CENTER);

        SBtn cancelBtn = new SBtn("Cancel Quiz");
        cancelBtn.setBackground(new Color(220, 53, 69));
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.addActionListener(e -> showLessonView(course, lessonIndex));

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnPanel.setBackground(StyleColors.BACKGROUND);
        btnPanel.add(cancelBtn);
        quizWrapper.add(btnPanel, BorderLayout.NORTH);

        SSplitPane splitPane = new SSplitPane(JSplitPane.HORIZONTAL_SPLIT, quizWrapper, rightPanel);
        splitPane.setDividerLocation(800);

        if (getContentPane().getComponentCount() > 1) {
            getContentPane().remove(1);
        }
        add(splitPane, LESSON_VIEW_PANEL);
        cardLayout.show(getContentPane(), LESSON_VIEW_PANEL);
    }

    private void proceedToNext(Course course, int currentIndex) {
        if (currentIndex + 1 < course.getLessons().size()) {
            showLessonView(course, currentIndex + 1);
        } else {
            SOptionPane.showMessageDialog(
                    StudentDashboardFrame.this,
                    "Congratulations! You've completed all lessons in this course.",
                    "Course Complete",
                    JOptionPane.INFORMATION_MESSAGE
            );
            // Update view to allow certificate download
            getContentPane().remove(1);
            showCourseView(course);
        }
    }

    private JPanel createCourseSidePanel(Course course) {
        JPanel sidePanel = new JPanel(new BorderLayout());
        sidePanel.setBackground(StyleColors.BACKGROUND);
        sidePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Unclickable CourseCard at the top
        CourseCard courseCard = new CourseCard(course);
        courseCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
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

        java.util.List<Lesson> lessons = course.getLessons();
        for (int i = 0; i < lessons.size(); i++) {
            Lesson lesson = lessons.get(i);
            int lessonIndex = i;

            boolean passed = studentController.getCompletedLessons(student.getId(), course.getCourseId()).contains(lesson.getLessonId());
            LessonCard lessonCard = new LessonCard(i + 1, lesson, passed);

            lessonCard.addCheckBoxListener(e -> {
                if (lesson.getQuiz() != null) {
                    lessonCard.setCompleted(passed);
                    SOptionPane.showMessageDialog(this, "Pass the quiz to complete this lesson.", "Locked", JOptionPane.WARNING_MESSAGE);
                } else {
                    if (lessonCard.isCompleted()) {
                        studentController.markLessonAsCompleted(student.getId(), course.getCourseId(), lesson.getLessonId());
                    } else {
                        studentController.unmarkLessonAsCompleted(student.getId(), course.getCourseId(), lesson.getLessonId());
                    }
                    int newCompletedLessons = studentController.getCompletedLessons(student.getId(), course.getCourseId()).size();
                    progressBar.setValue(newCompletedLessons);
                    progressBar.setString(newCompletedLessons + "/" + course.getLessons().size() + " Completed");

                    if (student.isCourseComplete(course)) {
                        showCourseView(course);
                    }
                }
            });

            lessonCard.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    showLessonView(course, lessonIndex);
                }
            });
            lessonCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
            lessonsPanel.add(lessonCard);
            if (i < lessons.size() - 1) {
                lessonsPanel.add(Box.createRigidArea(new Dimension(0, 8)));
            }
        }

        SScrollPane lessonsScrollPane = new SScrollPane(lessonsPanel);
        lessonsContainer.add(lessonsScrollPane, BorderLayout.CENTER);

        centerPanel.add(lessonsContainer, BorderLayout.CENTER);
        sidePanel.add(centerPanel, BorderLayout.CENTER);

        return sidePanel;
    }
}