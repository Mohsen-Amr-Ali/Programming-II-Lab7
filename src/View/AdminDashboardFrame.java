package View;

import Controller.AdminController;
import Controller.CourseController;
import Model.Course.Course;
import Model.Course.COURSE_STATUS;
import Model.JsonDatabaseManager;
import Model.User.Admin;
import View.AdminComponents.AdminNavBar;
import View.CommonComponents.ChartsView; // Import ChartsView
import View.CommonComponents.CourseCard;
import View.CommonComponents.CourseOverviewPanel;
import View.CommonComponents.LessonCard;
import View.CommonComponents.LessonViewPanel;
import View.StyledComponents.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class AdminDashboardFrame extends JFrame {
    private Admin admin;
    private AdminController adminController;
    private CourseController courseController; // For searching/filtering if needed

    private CardLayout cardLayout;
    private JPanel mainPanel;
    private AdminNavBar navBar;

    // Tabs
    private STabbedPane statusTabs;
    private JPanel pendingPanel;
    private JPanel approvedPanel;
    private JPanel rejectedPanel;

    // IDs for CardLayout
    private static final String MAIN_PANEL = "MainPanel";
    private static final String COURSE_VIEW_PANEL = "CourseViewPanel";
    private static final String LESSON_VIEW_PANEL = "LessonViewPanel";

    public AdminDashboardFrame(Admin admin) {
        this.admin = admin;
        this.adminController = new AdminController();
        this.courseController = new CourseController();

        setTitle("Admin Dashboard - SkillForge");
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
                    this,
                    "Are you sure you want to logout?",
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
        navBar = new AdminNavBar(admin);
        mainPanel.add(navBar, BorderLayout.NORTH);

        // --- Tabs ---
        statusTabs = new STabbedPane();

        pendingPanel = createCourseGridPanel();
        approvedPanel = createCourseGridPanel();
        rejectedPanel = createCourseGridPanel();

        // Add tabs (Pending first as it's the priority)
        statusTabs.addTab("Pending Review", new SScrollPane(pendingPanel));
        statusTabs.addTab("Approved History", new SScrollPane(approvedPanel));
        statusTabs.addTab("Rejected History", new SScrollPane(rejectedPanel));

        // Wrapper for margins
        JPanel tabsWrapper = new JPanel(new BorderLayout());
        tabsWrapper.setBackground(StyleColors.BACKGROUND);
        // Add 15px margin on all sides for consistent spacing
        tabsWrapper.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        tabsWrapper.add(statusTabs, BorderLayout.CENTER);

        mainPanel.add(tabsWrapper, BorderLayout.CENTER);

        // --- Navbar Logic ---
        // Analytics Button Listener
        navBar.getAnalyticsBtn().addActionListener(e -> showAnalyticsDialog());

        navBar.addRefreshListener(e -> {
            navBar.clearSearch();
            loadAllLists();
        });

        navBar.addSearchListener(e -> {
            String query = navBar.getSearchText();
            if (query.isEmpty() || query.equals("Search courses...")) {
                loadAllLists();
            } else {
                filterLists(query);
            }
        });

        // Initial Load
        loadAllLists();
    }

    private void showAnalyticsDialog() {
        JDialog statsDialog = new JDialog(this, "Platform Analytics", true);
        statsDialog.setSize(900, 600);
        statsDialog.setLocationRelativeTo(this);

        ChartsView chartsView = new ChartsView();
        // Assuming JsonDatabaseManager is singleton and accessible via getInstance inside
        // AdminController or we can call getInstance here directly.
        chartsView.updateAdminStats(Model.JsonDatabaseManager.getInstance());

        statsDialog.setContentPane(chartsView);
        statsDialog.setVisible(true);
    }

    private JPanel createCourseGridPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(StyleColors.BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
        return panel;
    }

    private void loadAllLists() {
        // 1. Pending
        ArrayList<Course> pending = adminController.getPendingCourses();
        loadCoursesToPanel(pendingPanel, pending);

        // 2. Approved
        ArrayList<Course> approved = adminController.getApprovedCourses();
        loadCoursesToPanel(approvedPanel, approved);

        // 3. Rejected
        ArrayList<Course> rejected = adminController.getRejectedCourses();
        loadCoursesToPanel(rejectedPanel, rejected);
    }

    private void filterLists(String query) {
        // Filter based on the currently selected tab
        int currentTab = statusTabs.getSelectedIndex();

        switch (currentTab) {
            case 0: // Pending tab
                ArrayList<Course> pending = adminController.getPendingCourses();
                ArrayList<Course> filteredPending = courseController.getCourseByTitle(pending, query.toLowerCase());
                loadCoursesToPanel(pendingPanel, filteredPending);
                break;

            case 1: // Approved tab
                ArrayList<Course> approved = adminController.getApprovedCourses();
                ArrayList<Course> filteredApproved = courseController.getCourseByTitle(approved, query.toLowerCase());
                loadCoursesToPanel(approvedPanel, filteredApproved);
                break;

            case 2: // Rejected tab
                ArrayList<Course> rejected = adminController.getRejectedCourses();
                ArrayList<Course> filteredRejected = courseController.getCourseByTitle(rejected, query.toLowerCase());
                loadCoursesToPanel(rejectedPanel, filteredRejected);
                break;
        }
        // Stay on current tab (don't switch)
    }

    private void loadCoursesToPanel(JPanel panel, ArrayList<Course> courses) {
        panel.removeAll();

        if (courses.isEmpty()) {
            JPanel noDataPanel = new JPanel(new BorderLayout());
            noDataPanel.setBackground(StyleColors.BACKGROUND);
            noDataPanel.setBorder(BorderFactory.createEmptyBorder(50, 0, 0, 0));

            SLabel lbl = new SLabel("No courses found in this category.");
            lbl.setHorizontalAlignment(SwingConstants.CENTER);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
            noDataPanel.add(lbl, BorderLayout.CENTER);

            panel.add(noDataPanel);
        } else {
            for (Course c : courses) {
                // Use the new Status Constructor
                CourseCard card = new CourseCard(c, c.getStatus());

                // Add click listener to open detail view
                card.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        showCourseDetailView(c);
                    }
                });

                card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
                panel.add(card);
                panel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }
        panel.revalidate();
        panel.repaint();
    }

    // --- DETAIL VIEW ---

    private void showCourseDetailView(Course course) {
        // Left: Overview Panel (Description, Image, Status)
        // Right: Action Panel (Approve/Decline/Statistics) + Lesson List

        // 1. Left Panel
        // Use Overloaded Overview Panel with Status
        CourseOverviewPanel overviewPanel = new CourseOverviewPanel(course, course.getStatus());

        // 2. Right Panel
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(StyleColors.BACKGROUND);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- Action Section (Top of Right Panel) ---
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        actionPanel.setBackground(StyleColors.CARD);
        actionPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(StyleColors.ACCENT_DARK, 1, true),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        SLabel actionLabel = new SLabel("Admin Actions:");
        actionLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        actionPanel.add(actionLabel);

        if (course.getStatus() == COURSE_STATUS.PENDING) {
            SBtn approveBtn = new SBtn("Approve Course");
            approveBtn.setBackground(new Color(40, 167, 69));
            approveBtn.setForeground(Color.WHITE);
            approveBtn.addActionListener(e -> {
                adminController.approveCourse(course.getCourseId());
                SOptionPane.showMessageDialog(this, "Course has been APPROVED and is now live.");
                loadAllLists();
                // Stay on view but refresh, or go back? Let's refresh view logic
                // Simple: go back to list for now
                cardLayout.show(getContentPane(), MAIN_PANEL);
                getContentPane().remove(1);
            });

            SBtn declineBtn = new SBtn("Reject Course");
            declineBtn.setBackground(new Color(220, 53, 69));
            declineBtn.setForeground(Color.WHITE);
            declineBtn.addActionListener(e -> {
                adminController.declineCourse(course.getCourseId());
                SOptionPane.showMessageDialog(this, "Course has been REJECTED.");
                loadAllLists();
                cardLayout.show(getContentPane(), MAIN_PANEL);
                getContentPane().remove(1);
            });

            actionPanel.add(approveBtn);
            actionPanel.add(declineBtn);
        } else {
            // Already processed
            SLabel statusInfo = new SLabel("This course is " + course.getStatus());
            statusInfo.setForeground(course.getStatus() == COURSE_STATUS.APPROVED ? Color.GREEN : Color.RED);
            actionPanel.add(statusInfo);

            // Placeholder for Statistics
            SBtn statsBtn = new SBtn("View Statistics");
            statsBtn.setEnabled(true); // Enabled now!
            statsBtn.addActionListener(e -> {
                // Open charts view for this specific course (Instructor view reused)
                JDialog statsDialog = new JDialog(this, "Course Analytics: " + course.getTitle(), true);
                statsDialog.setSize(900, 600);
                statsDialog.setLocationRelativeTo(this);
                ChartsView chartsView = new ChartsView();
                chartsView.updateInstructorStats(course); // Reuse instructor view for admin details
                statsDialog.setContentPane(chartsView);
                statsDialog.setVisible(true);
            });
            actionPanel.add(statsBtn);
        }

        rightPanel.add(actionPanel, BorderLayout.NORTH);

        // --- Lesson List (Center of Right Panel) ---
        // Just a read-only list so Admin can review content
        JPanel lessonsContainer = new JPanel(new BorderLayout());
        lessonsContainer.setBackground(StyleColors.BACKGROUND);
        SLabel listLabel = new SLabel("Course Content Review");
        listLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 5, 0));
        lessonsContainer.add(listLabel, BorderLayout.NORTH);

        JPanel listContent = new JPanel();
        listContent.setLayout(new BoxLayout(listContent, BoxLayout.Y_AXIS));
        listContent.setBackground(StyleColors.BACKGROUND);

        for (int i = 0; i < course.getLessons().size(); i++) {
            // Use simple LessonCard
            LessonCard card = new LessonCard(i + 1, course.getLessons().get(i));
            // Clicking a lesson could show its content in a popup or switch view
            // For now, just list them is enough for "Review" summary
            final int idx = i;
            card.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    showLessonView(course, course.getLessons().get(idx));
                }
            });

            card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
            listContent.add(card);
            listContent.add(Box.createRigidArea(new Dimension(0, 5)));
        }

        SScrollPane scrollLessons = new SScrollPane(listContent);
        lessonsContainer.add(scrollLessons, BorderLayout.CENTER);

        rightPanel.add(lessonsContainer, BorderLayout.CENTER);

        // --- Back Button ---
        JPanel bottomBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomBar.setBackground(StyleColors.BACKGROUND);
        SBtn backBtn = new SBtn("Back to Dashboard");
        backBtn.addActionListener(e -> {
            loadAllLists();
            cardLayout.show(getContentPane(), MAIN_PANEL);
            getContentPane().remove(1);
        });
        bottomBar.add(backBtn);
        rightPanel.add(bottomBar, BorderLayout.SOUTH);

        // Split Pane
        SSplitPane splitPane = new SSplitPane(JSplitPane.HORIZONTAL_SPLIT, overviewPanel, rightPanel);
        splitPane.setDividerLocation(600);

        if (getContentPane().getComponentCount() > 1) {
            getContentPane().remove(1);
        }
        add(splitPane, COURSE_VIEW_PANEL);
        cardLayout.show(getContentPane(), COURSE_VIEW_PANEL);
    }

    private void showLessonView(Course course, Model.Course.Lesson lesson) {
        // Left: Lesson Content
        // Right: Side Panel with Lesson Navigation (like Instructor/Student)

        // --- Left: Lesson Content ---
        int lessonIndex = course.getLessons().indexOf(lesson) + 1;
        LessonViewPanel contentPanel = new LessonViewPanel(lesson, lessonIndex);

        // --- Right: Side Panel ---
        JPanel sidePanel = new JPanel(new BorderLayout());
        sidePanel.setBackground(StyleColors.BACKGROUND);
        sidePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Lessons List Header
        SLabel lessonsLabel = new SLabel("Course Lessons");
        lessonsLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lessonsLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        sidePanel.add(lessonsLabel, BorderLayout.NORTH);

        // Lessons List
        JPanel lessonsPanel = new JPanel();
        lessonsPanel.setLayout(new BoxLayout(lessonsPanel, BoxLayout.Y_AXIS));
        lessonsPanel.setBackground(StyleColors.BACKGROUND);

        java.util.List<Model.Course.Lesson> allLessons = course.getLessons();
        for (int i = 0; i < allLessons.size(); i++) {
            Model.Course.Lesson l = allLessons.get(i);
            boolean isCurrent = (l == lesson);

            LessonCard card = new LessonCard(i + 1, l);

            // Highlight current lesson
            if (isCurrent) {
                card.setBackground(StyleColors.ACCENT);
            }

            card.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    showLessonView(course, l); // Navigate to clicked lesson
                }
            });

            card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
            lessonsPanel.add(card);
            lessonsPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        }

        SScrollPane lessonsScroll = new SScrollPane(lessonsPanel);
        sidePanel.add(lessonsScroll, BorderLayout.CENTER);

        // Bottom Action Bar
        JPanel bottomBar = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomBar.setBackground(StyleColors.BACKGROUND);

        SBtn backBtn = new SBtn("Back to Course");
        backBtn.addActionListener(e -> showCourseDetailView(course));

        bottomBar.add(backBtn);
        sidePanel.add(bottomBar, BorderLayout.SOUTH);

        // Split Pane
        SSplitPane splitPane = new SSplitPane(JSplitPane.HORIZONTAL_SPLIT, contentPanel, sidePanel);
        splitPane.setDividerLocation(750);

        if (getContentPane().getComponentCount() > 1) {
            getContentPane().remove(1);
        }
        add(splitPane, LESSON_VIEW_PANEL);
        cardLayout.show(getContentPane(), LESSON_VIEW_PANEL);
    }
}