package util;

import Model.Course.Lesson;
import Model.JsonDatabaseManager;

public class Certificate {

        private int certificateId;
        private int studentId;
        private int courseId;
        private String issueDate;

        public Certificate(int certificateId, int studentId, int courseId, String issueDate) {
            this.certificateId = certificateId;
            this.studentId = studentId;
            this.courseId = courseId;
            this.issueDate = issueDate;
        }

        // Getters
        public int getCertificateId() {
            return certificateId;
        }

        public int getStudentId() {
            return studentId;
        }

        public int getCourseId() {
            return courseId;
        }

        public String getIssueDate() {
            return issueDate;
        }

        public void setCertificateId(int certificateId) {
            this.certificateId = certificateId;
        }

        public void setStudentId(int studentId) {
            this.studentId = studentId;
        }

        public void setCourseId(int courseId) {
            this.courseId = courseId;
        }

        public void setIssueDate(String issueDate) {
            this.issueDate = issueDate;
        }

        @Override
        public String toString() {
            return "Certificate{" +
                    "certificateId='" + certificateId + '\'' +
                    ", studentId='" + studentId + '\'' +
                    ", courseId='" + courseId + '\'' +
                    ", issueDate='" + issueDate + '\'' +
                    '}';
        }
    }
