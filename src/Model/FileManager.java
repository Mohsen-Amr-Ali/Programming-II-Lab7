package Model;

import java.io.File;

public class FileManager {
    public static String saveFileToBinary(String txtFilePath, int courseId, int lessonId) {
        createCourseDirectory(courseId);
        String fileName = "Lesson_" + lessonId;
        String destDirPath = ASSETS_DIR + "Course_" + courseId + "/";
        File destFile = new File(destDirPath + fileName);
        File sourceFile = new File(txtFilePath);
        try (java.io.InputStream in = new java.io.FileInputStream(sourceFile);
             java.io.OutputStream out = new java.io.FileOutputStream(destFile)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
            return null; // or handle error as needed
        }
        // Return the relative path (excluding "src/")
        return "Database/Assets/Course_" + courseId + "/" + fileName;
    }
    private static final String ASSETS_DIR = "src/Database/Assets/";

    // Ensures the directory exists: src/Database/Assets/Course_[ID]/
    public static void createCourseDirectory(int courseId) {
        File directory = new File(ASSETS_DIR + "Course_" + courseId);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    public static String saveCourseImage(File sourceImage, int courseId) {
        createCourseDirectory(courseId);

        // Get the file extension (e.g., ".jpg")
        String fileName = sourceImage.getName();
        String extension = "";
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            extension = fileName.substring(dotIndex);
        }

        // Destination path: src/Database/Assets/Course_[ID]/title.jpg
        String destDirPath = ASSETS_DIR + "Course_" + courseId + "/";
        String destFileName = "title" + extension;
        File destFile = new File(destDirPath + destFileName);

        try (java.io.InputStream in = new java.io.FileInputStream(sourceImage);
                java.io.OutputStream out = new java.io.FileOutputStream(destFile)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
            return null; // or handle error as needed
        }

        // Return only the course folder and filename (e.g., "Course_7001\\title.jpg")
        return "Course_" + courseId + "\\" + destFileName;
    }

    public static String saveTextToBinary(String textContent, int courseId, int lessonId){
        createCourseDirectory(courseId);
        String fileName = "Lesson_" + lessonId;
        String destDirPath = ASSETS_DIR + "Course_" + courseId + "/";
        File destFile = new File(destDirPath + fileName);
        try (java.io.OutputStream out = new java.io.FileOutputStream(destFile)) {
            byte[] bytes = textContent.getBytes(java.nio.charset.StandardCharsets.UTF_8);
            out.write(bytes);
        } catch (java.io.IOException e) {
            e.printStackTrace();
            return null; // or handle error as needed
        }
        // Return only the course folder and filename
        return "Course_" + courseId + "\\" + fileName;
    }

    public static String readBinaryToText(String filePath) {
        File file = new File(filePath); // Use the absolute path directly
        try (java.io.InputStream in = new java.io.FileInputStream(file)) {
            byte[] bytes = in.readAllBytes();
            return new String(bytes, java.nio.charset.StandardCharsets.UTF_8);
        } catch (java.io.IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}