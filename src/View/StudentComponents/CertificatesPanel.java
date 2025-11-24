package View.StudentComponents;

import Model.Course.Course;
import Model.User.Student;
import View.StyledComponents.SBtn;
import View.StyledComponents.SLabel;
import View.StyledComponents.SScrollPane;
import View.StyledComponents.StyleColors;
import Controller.StudentController;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;

public class CertificatesPanel extends JPanel {
    private Student student;
    private StudentController studentController;
    private JPanel certificatesContainer;

    public CertificatesPanel(Student student, StudentController studentController) {
        this.student = student;
        this.studentController = studentController;

        setLayout(new BorderLayout());
        setBackground(StyleColors.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        SLabel titleLabel = new SLabel("My Certificates");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(StyleColors.ACCENT);
        add(titleLabel, BorderLayout.NORTH);

        certificatesContainer = new JPanel();
        certificatesContainer.setLayout(new BoxLayout(certificatesContainer, BoxLayout.Y_AXIS));
        certificatesContainer.setBackground(StyleColors.BACKGROUND);

        SScrollPane scrollPane = new SScrollPane(certificatesContainer);
        add(scrollPane, BorderLayout.CENTER);

        loadCertificates();
    }

    private void loadCertificates() {
        certificatesContainer.removeAll();
        ArrayList<Course> completedCourses = studentController.getCompletedCourses(student.getId());

        if (completedCourses.isEmpty()) {
            SLabel noCertsLabel = new SLabel("No certificates earned yet. Complete a course to earn one!");
            noCertsLabel.setHorizontalAlignment(SwingConstants.CENTER);
            certificatesContainer.add(noCertsLabel);
        } else {
            for (Course course : completedCourses) {
                certificatesContainer.add(createCertificateCard(course));
                certificatesContainer.add(Box.createVerticalStrut(10));
            }
        }
        revalidate();
        repaint();
    }

    private JPanel createCertificateCard(Course course) {
        JPanel card = new JPanel(new BorderLayout(15, 0));
        card.setBackground(StyleColors.CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(StyleColors.ACCENT_DARK, 1, true),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));

        SLabel courseTitleLabel = new SLabel(course.getTitle());
        courseTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        card.add(courseTitleLabel, BorderLayout.CENTER);

        SBtn downloadBtn = new SBtn("Download");
        downloadBtn.addActionListener(e -> downloadCertificate(course));
        card.add(downloadBtn, BorderLayout.EAST);

        return card;
    }

    private void downloadCertificate(Course course) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Certificate");
        fileChooser.setSelectedFile(new File("Certificate_" + course.getTitle().replaceAll(" ", "_") + ".pdf"));
        fileChooser.setFileFilter(new FileNameExtensionFilter("PDF Documents", "pdf"));

        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            if (!fileToSave.getAbsolutePath().endsWith(".pdf")) {
                fileToSave = new File(fileToSave.getAbsolutePath() + ".pdf");
            }

            boolean success = studentController.generateCertificate(student.getId(), course.getCourseId(), fileToSave.getAbsolutePath());
            if (success) {
                JOptionPane.showMessageDialog(this, "Certificate saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to generate certificate.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

