package Model.Course;

import Model.User.Student;

public class Certificate {

        private int certificateId;
        private Student student;
        private Course course;
        private String issueDate;

        public Certificate(int certificateId, Course course,Student student, String issueDate) {
            this.certificateId = certificateId;
            this.course = course;
            this.student = student;
            this.issueDate = issueDate;
        }

        // Getters
        public int getCertificateId() {
            return certificateId;
        }

        public String getIssueDate() {
            return issueDate;
        }

        public void setCertificateId(int certificateId) {
            this.certificateId = certificateId;
        }

        public void setIssueDate(String issueDate) {
            this.issueDate = issueDate;
        }
        public String getStudentName() {
            return student.getUsername();
        }
        public int getStudentId() {
            return student.getId();
        }
        public int getCourseId() {
            return course.getCourseId();
        }
        public String getCourseTitle() {
            return course.getTitle();
        }
    }
