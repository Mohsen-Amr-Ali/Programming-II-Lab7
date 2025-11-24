package View;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;

import javax.swing.*;

import Controller.CourseController;
import Controller.InstructorController;
import Model.Course.Course;
import Model.Course.Lesson;
import Model.Course.COURSE_STATUS;
import Model.User.Instructor;
import View.CommonComponents.ChartsView; // Import ChartsView
import View.CommonComponents.CourseCard;
import View.CommonComponents.CourseOverviewPanel;
import View.CommonComponents.LessonCard;
import View.CommonComponents.LessonViewPanel;
import View.InstructorComponents.AddCoursePanel;
import View.InstructorComponents.AddLessonPanel;
import View.InstructorComponents.EditCoursePanel;
import View.InstructorComponents.EditLessonPanel;
import View.InstructorComponents.InstructorNavBar;
import View.StyledComponents.*;

public class InstructorDashboardFrame extends JFrame {
    private Instructor instructor;
    private InstructorController instructorController;
    private CourseController courseController;

    private CardLayout cardLayout;
    private JPanel mainPanel;
    private InstructorNavBar navBar;

    // Status Tabs (Using STabbedPane to fix the blue color issue)
    private STabbedPane statusTabs;
    private JPanel allCoursesPanel;
    private JPanel pendingCoursesPanel;
    private JPanel approvedCoursesPanel;
    private JPanel rejectedCoursesPanel;

    // Analytics Tab Components
    private JPanel insightsPanel;
    private SComboBox<String> courseSelector;
    private ChartsView chartsView;
    private ArrayList<Course> instructorCourses;

    private static final String MAIN_PANEL = "MainPanel";
    private static final String COURSE_VIEW_PANEL = "CourseViewPanel";
    private static final String LESSON_VIEW_PANEL = "LessonViewPanel";

    public InstructorDashboardFrame(Instructor instructor) {
        this.instructor = instructor;
        this.instructorController = new InstructorController();
        this.courseController = new CourseController();

        setTitle("Instructor Dashboard - SkillForge");
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

        // Navigation Bar
        navBar = new InstructorNavBar(instructor);
        mainPanel.add(navBar, BorderLayout.NORTH);

        // --- Content Area with Tabs ---
        statusTabs = new STabbedPane(); // Uses the styled tabs

        // Create Panels for each tab
        allCoursesPanel = createCourseGridPanel();
        pendingCoursesPanel = createCourseGridPanel();
        approvedCoursesPanel = createCourseGridPanel();
        rejectedCoursesPanel = createCourseGridPanel();

        // Insights Panel Setup
        createInsightsPanel();

        // Wrap in ScrollPanes using SScrollPane
        statusTabs.addTab("All Courses", new SScrollPane(allCoursesPanel));
        statusTabs.addTab("Approved", new SScrollPane(approvedCoursesPanel));
        statusTabs.addTab("Pending", new SScrollPane(pendingCoursesPanel));
        statusTabs.addTab("Rejected", new SScrollPane(rejectedCoursesPanel));
        statusTabs.addTab("Insights", insightsPanel); // Add Insights Tab

        // WRAP TABS IN A PANEL TO ADD MARGINS (Align with Navbar)
        JPanel tabsWrapper = new JPanel(new BorderLayout());
        tabsWrapper.setBackground(StyleColors.BACKGROUND);
        tabsWrapper.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Add Course Button at Top Right (above tabs)
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        topBar.setBackground(StyleColors.BACKGROUND);
        topBar.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        SBtn addCourseBtn = new SBtn("+ Create New Course");
        addCourseBtn.setBackground(StyleColors.ACCENT);
        addCourseBtn.setForeground(Color.WHITE);
        addCourseBtn.addActionListener(e -> showAddCourseDialog());
        topBar.add(addCourseBtn);

        tabsWrapper.add(topBar, BorderLayout.NORTH);
        tabsWrapper.add(statusTabs, BorderLayout.CENTER);

        mainPanel.add(tabsWrapper, BorderLayout.CENTER);

        // Navbar Listeners
        navBar.addSearchButtonListener(e -> {
            String searchText = navBar.getSearchText();
            if (searchText.equals("Search courses...") || searchText.isEmpty()) {
                loadAllCourses();
                return;
            }
            filterCourses(searchText);
        });

        navBar.addRefreshButtonListener(e -> {
            navBar.clearSearchText();
            loadAllCourses();
            refreshInsights(); // Refresh insights too
        });

        // Tab Change Listener for Insights
        statusTabs.addChangeListener(e -> {
            if (statusTabs.getSelectedIndex() == 4) { // Index 4 is Insights
                refreshInsights();
            }
        });

        // Initial Load
        loadAllCourses();
    }

    private void createInsightsPanel() {
        insightsPanel = new JPanel(new BorderLayout());
        insightsPanel.setBackground(StyleColors.BACKGROUND);
        insightsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top: Course Selector
        JPanel selectorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        selectorPanel.setBackground(StyleColors.BACKGROUND);

        SLabel selectLabel = new SLabel("Select Course for Analysis: ");
        selectLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));

        courseSelector = new SComboBox<>();
        courseSelector.setPreferredSize(new Dimension(300, 30));
        courseSelector.addActionListener(e -> updateCharts());

        selectorPanel.add(selectLabel);
        selectorPanel.add(courseSelector);

        insightsPanel.add(selectorPanel, BorderLayout.NORTH);

        // Center: Charts View
        chartsView = new ChartsView();
        // Wrap in ScrollPane in case charts are tall
        SScrollPane chartsScroll = new SScrollPane(chartsView);
        insightsPanel.add(chartsScroll, BorderLayout.CENTER);
    }

    private void refreshInsights() {
        // Reload instructor courses for the dropdown
        instructorCourses = instructorController.getCreatedCourses(instructor.getId());
        courseSelector.removeAllItems();

        if (instructorCourses.isEmpty()) {
            courseSelector.addItem("No Courses Available");
            courseSelector.setEnabled(false);
        } else {
            courseSelector.setEnabled(true);
            for (Course c : instructorCourses) {
                courseSelector.addItem(c.getTitle()); // Store title, map index to list
            }
            // Select first by default
            if (courseSelector.getItemCount() > 0) {
                courseSelector.setSelectedIndex(0);
            }
        }
    }

    private void updateCharts() {
        int selectedIndex = courseSelector.getSelectedIndex();
        if (selectedIndex >= 0 && selectedIndex < instructorCourses.size()) {
            Course selectedCourse = instructorCourses.get(selectedIndex);
            chartsView.updateInstructorStats(selectedCourse);
        }
    }

    private JPanel createCourseGridPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(StyleColors.BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5)); // Inner padding for scroll content
        return panel;
    }

    private void loadCoursesToPanel(JPanel panel, ArrayList<Course> courses) {
        panel.removeAll();

        if (courses.isEmpty()) {
            JPanel noResultsPanel = new JPanel(new BorderLayout());
            noResultsPanel.setBackground(StyleColors.BACKGROUND);
            noResultsPanel.setBorder(BorderFactory.createEmptyBorder(50, 20, 20, 20));

            SLabel noResultsLabel = new SLabel("No courses found");
            noResultsLabel.setFont(noResultsLabel.getFont().deriveFont(18f).deriveFont(java.awt.Font.BOLD));
            noResultsLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            noResultsPanel.add(noResultsLabel, BorderLayout.CENTER);

            panel.add(noResultsPanel);
        } else {
            for (Course course : courses) {
                CourseCard card = new CourseCard(course);
                // Add click listener to open Course View
                card.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        showCourseView(course);
                    }
                });

                card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120)); // Fixed height
                panel.add(card);
                panel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }
        panel.revalidate();
        panel.repaint();
    }

    private void loadAllCourses() {
        // 1. All
        ArrayList<Course> all = instructorController.getCreatedCourses(instructor.getId());
        loadCoursesToPanel(allCoursesPanel, all);

        // 2. Approved
        ArrayList<Course> approved = instructorController.getCoursesByStatus(instructor.getId(), COURSE_STATUS.APPROVED);
        loadCoursesToPanel(approvedCoursesPanel, approved);

        // 3. Pending
        ArrayList<Course> pending = instructorController.getCoursesByStatus(instructor.getId(), COURSE_STATUS.PENDING);
        loadCoursesToPanel(pendingCoursesPanel, pending);

        // 4. Rejected
        ArrayList<Course> rejected = instructorController.getCoursesByStatus(instructor.getId(), COURSE_STATUS.REJECTED);
        loadCoursesToPanel(rejectedCoursesPanel, rejected);
    }

    private void filterCourses(String query) {
        String lowerQuery = query.toLowerCase();
        ArrayList<Course> all = instructorController.getCreatedCourses(instructor.getId());
        ArrayList<Course> filtered = courseController.getCourseByTitle(all, lowerQuery);
        loadCoursesToPanel(allCoursesPanel, filtered);
        // For simplicity, filtering only updates the "All" tab currently
        statusTabs.setSelectedIndex(0);
    }

    // --- CRUD DIALOGS ---

    private void showAddCourseDialog() {
        JDialog dialog = new JDialog(this, "Create New Course", true);
        AddCoursePanel panel = new AddCoursePanel();

        panel.getAddButton().addActionListener(e -> {
            String title = panel.getTitleText();
            String desc = panel.getDescriptionText();
            File imageFile = panel.getSelectedImageFile();

            if (title.isEmpty() || desc.isEmpty()) {
                SOptionPane.showMessageDialog(dialog, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            instructorController.addCourse(title, desc, instructor.getId(), imageFile);
            SOptionPane.showMessageDialog(dialog, "Course created successfully! Sent for Admin approval.");
            dialog.dispose();
            loadAllCourses();
        });

        dialog.setContentPane(panel.getRootPanel());
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    // --- COURSE VIEW (Detailed) ---

    private void showCourseView(Course course) {
        // Use SSplitPane
        JPanel courseViewPanel = new JPanel(new BorderLayout());
        courseViewPanel.setBackground(StyleColors.BACKGROUND);

        CourseOverviewPanel overviewPanel = new CourseOverviewPanel(course);
        courseViewPanel.add(overviewPanel, BorderLayout.CENTER);

        JPanel sidePanel = new JPanel(new BorderLayout());
        sidePanel.setBackground(StyleColors.BACKGROUND);
        sidePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- Lessons List (Top/Center) ---
        JPanel lessonsContainer = new JPanel(new BorderLayout());
        lessonsContainer.setBackground(StyleColors.BACKGROUND);

        SLabel lessonsLabel = new SLabel("Lessons");
        lessonsLabel.setFont(lessonsLabel.getFont().deriveFont(16f).deriveFont(java.awt.Font.BOLD));
        lessonsLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        lessonsContainer.add(lessonsLabel, BorderLayout.NORTH);

        JPanel lessonsPanel = new JPanel();
        lessonsPanel.setLayout(new BoxLayout(lessonsPanel, BoxLayout.Y_AXIS));
        lessonsPanel.setBackground(StyleColors.BACKGROUND);

        java.util.List<Lesson> lessons = course.getLessons();
        for (int i = 0; i < lessons.size(); i++) {
            Lesson lesson = lessons.get(i);
            // Use LessonCard with Edit button (Constructor 3)
            LessonCard lessonCard = new LessonCard(i + 1, lesson);

            // Make the whole card clickable to view
            lessonCard.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    showLessonView(course, lesson);
                }
            });

            lessonCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
            lessonsPanel.add(lessonCard);
            lessonsPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        }

        // Add "Add Lesson" Button at bottom of list
        SBtn addLessonBtn = new SBtn("+ Add Lesson");
        addLessonBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        addLessonBtn.addActionListener(e -> showAddLessonDialog(course));

        JPanel listWrapper = new JPanel(new BorderLayout());
        listWrapper.setBackground(StyleColors.BACKGROUND);
        listWrapper.add(lessonsPanel, BorderLayout.NORTH);
        listWrapper.add(Box.createVerticalStrut(10));
        listWrapper.add(addLessonBtn, BorderLayout.CENTER);

        SScrollPane lessonsScroll = new SScrollPane(listWrapper);
        lessonsContainer.add(lessonsScroll, BorderLayout.CENTER);

        sidePanel.add(lessonsContainer, BorderLayout.CENTER);

        // --- Bottom Action Bar (Back, Edit, Delete) ---
        JPanel bottomBar = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomBar.setBackground(StyleColors.BACKGROUND);

        SBtn backButton = new SBtn("Back");
        backButton.addActionListener(e -> {
            loadAllCourses();
            cardLayout.show(getContentPane(), MAIN_PANEL);
            getContentPane().remove(1); // Cleanup
        });

        SBtn editCourseBtn = new SBtn("Edit Course");
        editCourseBtn.addActionListener(e -> showEditCourseDialog(course));

        SBtn deleteCourseBtn = new SBtn("Delete Course");
        deleteCourseBtn.setBackground(new Color(220, 53, 69));
        deleteCourseBtn.setForeground(Color.WHITE);
        deleteCourseBtn.addActionListener(e -> {
            int result = SOptionPane.showConfirmDialog(
                    this,
                    "<html>Are you sure you want to delete this course?<br><b>This action is irreversible!</b></html>",
                    "Delete Course",
                    JOptionPane.YES_NO_OPTION
            );
            if (result == JOptionPane.YES_OPTION) {
                instructorController.deleteCourse(course);
                loadAllCourses();
                // Go back to main
                cardLayout.show(getContentPane(), MAIN_PANEL);
                // Remove the view panel
                getContentPane().remove(1);
            }
        });

        bottomBar.add(backButton);
        bottomBar.add(editCourseBtn);
        bottomBar.add(deleteCourseBtn);

        sidePanel.add(bottomBar, BorderLayout.SOUTH);

        // Use SSplitPane
        SSplitPane splitPane = new SSplitPane(JSplitPane.HORIZONTAL_SPLIT, courseViewPanel, sidePanel);
        splitPane.setDividerLocation(750);

        // Add to layout
        if (getContentPane().getComponentCount() > 1) {
            getContentPane().remove(1);
        }
        add(splitPane, COURSE_VIEW_PANEL);
        cardLayout.show(getContentPane(), COURSE_VIEW_PANEL);
    }

    // --- LESSON VIEW (Detailed) ---

    private void showLessonView(Course course, Lesson lesson) {
        // Main Layout: SplitPane
        // Left: Content (LessonViewPanel)
        // Right: Sidebar with Lesson List (so instructor sees context)

        // --- Left: Content ---
        int lessonIndex = course.getLessons().indexOf(lesson) + 1;
        LessonViewPanel contentPanel = new LessonViewPanel(lesson, lessonIndex);

        // Add Bottom bar for Lesson Actions inside the content panel
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionPanel.setBackground(StyleColors.BACKGROUND);

        SBtn editBtn = new SBtn("Edit Lesson");
        editBtn.addActionListener(e -> showEditLessonDialog(course, lesson));

        SBtn deleteBtn = new SBtn("Delete Lesson");
        deleteBtn.setBackground(new Color(220, 53, 69));
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.addActionListener(e -> {
            int result = SOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete this lesson?",
                    "Delete Lesson",
                    JOptionPane.YES_NO_OPTION
            );
            if (result == JOptionPane.YES_OPTION) {
                instructorController.deleteLesson(course, lesson);
                // Go back to Course View
                showCourseView(course);
            }
        });

        SBtn backBtn = new SBtn("Back to Course");
        backBtn.addActionListener(e -> showCourseView(course)); // Go back to course view

        actionPanel.add(backBtn);
        actionPanel.add(editBtn);
        actionPanel.add(deleteBtn);

        contentPanel.add(actionPanel, BorderLayout.SOUTH); // Add buttons to bottom of content view

        // --- Right: Sidebar (Lesson List - Recreated) ---
        JPanel sidePanel = new JPanel(new BorderLayout());
        sidePanel.setBackground(StyleColors.BACKGROUND);
        sidePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        SLabel listLabel = new SLabel("Course Lessons");
        listLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        // Fix: Add Padding between label and list
        listLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        sidePanel.add(listLabel, BorderLayout.NORTH);

        JPanel listContainer = new JPanel();
        listContainer.setLayout(new BoxLayout(listContainer, BoxLayout.Y_AXIS));
        listContainer.setBackground(StyleColors.BACKGROUND);

        java.util.List<Lesson> allLessons = course.getLessons();
        for(int i=0; i<allLessons.size(); i++) {
            Lesson l = allLessons.get(i);
            // Highlighting current lesson
            boolean isCurrent = (l == lesson);

            LessonCard card = new LessonCard(i+1, l);
            if (isCurrent) {
                card.setBackground(StyleColors.ACCENT); // Highlight current
            }

            card.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    showLessonView(course, l); // Switch lesson
                }
            });

            card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
            listContainer.add(card);
            listContainer.add(Box.createRigidArea(new Dimension(0, 5)));
        }

        SScrollPane scrollSide = new SScrollPane(listContainer);
        sidePanel.add(scrollSide, BorderLayout.CENTER);

        // --- Split Pane ---
        SSplitPane splitPane = new SSplitPane(JSplitPane.HORIZONTAL_SPLIT, contentPanel, sidePanel);
        splitPane.setDividerLocation(800);

        // Add to layout
        if (getContentPane().getComponentCount() > 1) {
            getContentPane().remove(1);
        }
        add(splitPane, LESSON_VIEW_PANEL);
        cardLayout.show(getContentPane(), LESSON_VIEW_PANEL);
    }

    // --- SUB-DIALOGS ---

    private void showEditCourseDialog(Course course) {
        JDialog dialog = new JDialog(this, "Edit Course", true);
        EditCoursePanel panel = new EditCoursePanel(course);

        panel.addSaveListener(e -> {
            String title = panel.getTitleText();
            String desc = panel.getDescriptionText();
            File image = panel.getSelectedImageFile();

            instructorController.updateCourse(course, title, desc, image);
            SOptionPane.showMessageDialog(dialog, "Course updated successfully.");
            dialog.dispose();
            showCourseView(course); // Refresh view
        });

        dialog.setContentPane(panel.getRootPanel());
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void showAddLessonDialog(Course course) {
        JDialog dialog = new JDialog(this, "Add New Lesson", true);
        AddLessonPanel panel = new AddLessonPanel();
        // Populate position
        panel.setLessonCount(course.getLessons().size());

        panel.getAddButton().addActionListener(e -> {
            String title = panel.getTitleText();
            // Get Content
            boolean isFile = panel.isFileMode();
            String contentOrPath = isFile ? panel.getSelectedContentFile().getPath() : panel.getContentText();
            int pos = panel.getSelectedPosition();

            if (title.isEmpty()) {
                SOptionPane.showMessageDialog(dialog, "Title required.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            instructorController.addLesson(course.getCourseId(), title, contentOrPath, isFile, pos);
            SOptionPane.showMessageDialog(dialog, "Lesson added.");
            dialog.dispose();
            showCourseView(course); // Refresh
        });

        dialog.setContentPane(panel.getRootPanel());
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void showEditLessonDialog(Course course, Lesson lesson) {
        JDialog dialog = new JDialog(this, "Edit Lesson", true);
        int currentIndex = course.getLessons().indexOf(lesson);
        EditLessonPanel panel = new EditLessonPanel(lesson, currentIndex, course.getLessons().size());

        panel.addSaveListener(e -> {
            String title = panel.getTitleText();
            boolean isFile = panel.isFileMode();
            // If user didn't change tab, we take from that tab.
            String contentOrPath = isFile ? panel.getSelectedContentFile().getPath() : panel.getContentText();
            int pos = panel.getSelectedPosition();

            instructorController.updateLesson(course, lesson, title, contentOrPath, isFile, pos);
            SOptionPane.showMessageDialog(dialog, "Lesson updated.");
            dialog.dispose();
            showLessonView(course, lesson); // Refresh view
        });

        dialog.setContentPane(panel.getRootPanel());
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
}