package View;

import Controller.AdminController;
import Model.Course.Course;
import Model.User.Admin;
import View.CommonComponents.CourseCard;
import View.StyledComponents.*;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class AdminDashboardFrame extends JFrame {
    private Admin admin;
    private AdminController adminController;
    private JPanel mainPanel;
    private JPanel pendingCoursesPanel;
    private SBtn logoutBtn;

    public AdminDashboardFrame(Admin admin) {
        this.admin = admin;
        this.adminController = new AdminController();

        setTitle("Admin Dashboard - SkillForge");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(StyleColors.BACKGROUND);

        createNavBar();
        createContentPanel();

        add(mainPanel);
    }

    private void createNavBar() {
        JPanel navBar = new JPanel(new BorderLayout());
        navBar.setBackground(StyleColors.BACKGROUND);
        navBar.setPreferredSize(new Dimension(getWidth(), 60));
        navBar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        SLabel titleLabel = new SLabel("Admin Dashboard");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);

        logoutBtn = new SBtn("Logout");
        logoutBtn.setBackground(StyleColors.ACCENT);
        logoutBtn.setForeground(Color.WHITE);

        navBar.add(titleLabel, BorderLayout.WEST);
        navBar.add(logoutBtn, BorderLayout.EAST);

        mainPanel.add(navBar, BorderLayout.NORTH);
    }

    private void createContentPanel() {
        pendingCoursesPanel = new JPanel();
        pendingCoursesPanel.setLayout(new BoxLayout(pendingCoursesPanel, BoxLayout.Y_AXIS));
        pendingCoursesPanel.setBackground(StyleColors.BACKGROUND);

        JScrollPane scrollPane = new JScrollPane(pendingCoursesPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        refreshPendingCourses();
    }

    private void refreshPendingCourses() {
        pendingCoursesPanel.removeAll();
        ArrayList<Course> pending = adminController.getPendingCourses();

        if (pending.isEmpty()) {
            SLabel emptyLbl = new SLabel("No pending courses to review.");
            emptyLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
            pendingCoursesPanel.add(Box.createVerticalStrut(50));
            pendingCoursesPanel.add(emptyLbl);
        } else {
            for (Course c : pending) {
                addPendingCourseCard(c);
            }
        }
        pendingCoursesPanel.revalidate();
        pendingCoursesPanel.repaint();
    }

    private void addPendingCourseCard(Course course) {
        JPanel cardPanel = new JPanel(new BorderLayout());
        cardPanel.setBackground(StyleColors.CARD);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 20, 10, 20),
                BorderFactory.createLineBorder(StyleColors.ACCENT_DARK, 1)
        ));
        cardPanel.setMaximumSize(new Dimension(900, 150));

        // Course Info
        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        infoPanel.setBackground(StyleColors.CARD);
        SLabel title = new SLabel(course.getTitle());
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        SLabel desc = new SLabel("Instructor ID: " + course.getInstructorId()); // Simple info for now
        infoPanel.add(title);
        infoPanel.add(desc);

        // Action Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(StyleColors.CARD);

        SBtn approveBtn = new SBtn("Approve");
        approveBtn.setBackground(new Color(40, 167, 69)); // Green
        approveBtn.addActionListener(e -> {
            adminController.approveCourse(course.getCourseId());
            refreshPendingCourses();
            SOptionPane.showMessageDialog(this, "Course Approved!");
        });

        SBtn rejectBtn = new SBtn("Reject");
        rejectBtn.setBackground(new Color(220, 53, 69)); // Red
        rejectBtn.addActionListener(e -> {
            adminController.declineCourse(course.getCourseId());
            refreshPendingCourses();
            SOptionPane.showMessageDialog(this, "Course Rejected.");
        });

        btnPanel.add(approveBtn);
        btnPanel.add(rejectBtn);

        cardPanel.add(infoPanel, BorderLayout.CENTER);
        cardPanel.add(btnPanel, BorderLayout.EAST);

        pendingCoursesPanel.add(cardPanel);
        pendingCoursesPanel.add(Box.createVerticalStrut(10));
    }

    public void setLogoutListener(Runnable listener) {
        logoutBtn.addActionListener(e -> listener.run());
    }
}