package util;

    public class Certificate {

        private String certificateId;
        private String studentId;
        private String courseId;
        private String issueDate;

        public Certificate(String certificateId, String studentId, String courseId, String issueDate) {
            this.certificateId = certificateId;
            this.studentId = studentId;
            this.courseId = courseId;
            this.issueDate = issueDate;
        }

        // Getters
        public String getCertificateId() {
            return certificateId;
        }

        public String getStudentId() {
            return studentId;
        }

        public String getCourseId() {
            return courseId;
        }

        public String getIssueDate() {
            return issueDate;
        }

        public void setCertificateId(String certificateId) {
            this.certificateId = certificateId;
        }

        public void setStudentId(String studentId) {
            this.studentId = studentId;
        }

        public void setCourseId(String courseId) {
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
