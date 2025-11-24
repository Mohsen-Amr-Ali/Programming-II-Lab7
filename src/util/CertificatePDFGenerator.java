package util;

import Model.Course.Certificate;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.awt.*;

public class CertificatePDFGenerator {

    public static boolean generateCertificatePDF(Certificate certificate, String filePath) {
        try (PDDocument document = new PDDocument()) {

            PDPage page = new PDPage(PDRectangle.LETTER);
            document.addPage(page);

            PDRectangle box = page.getMediaBox();
            float width = box.getWidth();
            float height = box.getHeight();

            PDPageContentStream content = new PDPageContentStream(document, page);

            // ============================================================
            // Elegant Double Border
            // ============================================================
            content.setStrokingColor(new Color(60, 60, 60));
            content.setLineWidth(3);
            content.addRect(40, 40, width - 80, height - 80);
            content.stroke();

            content.setLineWidth(1);
            content.addRect(55, 55, width - 110, height - 110);
            content.stroke();

            // ============================================================
            // Title
            // ============================================================
            writeCentered(content, "CERTIFICATE OF COMPLETION",
                    PDType1Font.HELVETICA_BOLD, 34,
                    width / 2, height - 140, width - 120);

            drawSeparator(content, width, height - 155);

            writeCentered(content, "This is to certify that",
                    PDType1Font.HELVETICA_OBLIQUE, 16,
                    width / 2, height - 190, width - 120);

            // ============================================================
            // Student Name (auto-fit)
            // ============================================================
            writeCentered(content,
                    certificate.getStudentName() + "  (ID: " + certificate.getStudentId() + ")",
                    PDType1Font.HELVETICA_BOLD, 28,
                    width / 2, height - 235, width - 140);

            drawThinLine(content, width, height - 245);

            // ============================================================
            // Course Section (auto-fit)
            // ============================================================
            writeCentered(content,
                    "Has successfully completed the online course",
                    PDType1Font.HELVETICA_OBLIQUE, 16,
                    width / 2, height - 280, width - 120);

            writeCentered(content,
                    certificate.getCourseTitle() + "  (Course ID: " + certificate.getCourseId() + ")",
                    PDType1Font.HELVETICA_BOLD, 22,
                    width / 2, height - 320, width - 140);

            writeCentered(content,
                    "Issued on: " + certificate.getIssueDate(),
                    PDType1Font.HELVETICA_OBLIQUE, 14,
                    width / 2, height - 355, width - 140);

            // ============================================================
            // Footer
            // ============================================================
            drawFooterLine(content, width, 120);

            writeCentered(content,
                    "Verified by SkillForge",
                    PDType1Font.HELVETICA_BOLD_OBLIQUE, 14,
                    width / 2, 105, width - 140);

            content.close();
            document.save(filePath);

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ============================================================
    // Helper: centered text WITH auto-scaling if too long
    // ============================================================
    private static void writeCentered(PDPageContentStream content, String text,
                                      PDType1Font font, float fontSize,
                                      float centerX, float y,
                                      float maxWidth) throws Exception {

        // Calculate width of the text at the requested size
        float textWidth = (font.getStringWidth(text) / 1000) * fontSize;

        // Auto-scale if too wide
        if (textWidth > maxWidth) {
            float scale = maxWidth / textWidth;
            fontSize = fontSize * scale;
            textWidth = maxWidth; // now it fits
        }

        float startX = centerX - (textWidth / 2);

        content.beginText();
        content.setFont(font, fontSize);
        content.newLineAtOffset(startX, y);
        content.showText(text);
        content.endText();
    }

    private static void drawSeparator(PDPageContentStream content, float width, float y) throws Exception {
        content.setLineWidth(1.2f);
        content.moveTo(width * 0.25f, y);
        content.lineTo(width * 0.75f, y);
        content.stroke();
    }

    private static void drawThinLine(PDPageContentStream content, float width, float y) throws Exception {
        content.setLineWidth(0.8f);
        content.moveTo(width * 0.28f, y);
        content.lineTo(width * 0.72f, y);
        content.stroke();
    }

    private static void drawFooterLine(PDPageContentStream content, float width, float y) throws Exception {
        content.setLineWidth(1.0f);
        content.moveTo(width * 0.30f, y);
        content.lineTo(width * 0.70f, y);
        content.stroke();
    }
}
