package util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

    public class CertificatePDFGenerator {

        public static boolean generateCertificatePDF(Certificate certificate, String filePath) {
            try {
                PDDocument document = new PDDocument();
                PDPage page = new PDPage();
                document.addPage(page);

                PDPageContentStream content = new PDPageContentStream(document, page);

                // Title
                content.beginText();
                content.setFont(PDType1Font.HELVETICA_BOLD, 30);
                content.newLineAtOffset(100, 700);
                content.showText("Certificate of Completion");
                content.endText();

                // Student
                content.beginText();
                content.setFont(PDType1Font.HELVETICA, 18);
                content.newLineAtOffset(100, 650);
                content.showText("Awarded to Student: " + certificate.getStudentName() + " ( "+ certificate.getStudentId() + " ) ");
                content.endText();

                // Course
                content.beginText();
                content.setFont(PDType1Font.HELVETICA, 18);
                content.newLineAtOffset(100, 620);
                content.showText("For completing Course: " + certificate.getCourseTitle() + " ( " + certificate.getCourseId() + " )");
                content.endText();

                // Date
                content.beginText();
                content.setFont(PDType1Font.HELVETICA_OBLIQUE, 16);
                content.newLineAtOffset(100, 590);
                content.showText("Date Issued: " + certificate.getIssueDate());
                content.endText();

                content.close();

                document.save(filePath);
                document.close();

                return true;

            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }
