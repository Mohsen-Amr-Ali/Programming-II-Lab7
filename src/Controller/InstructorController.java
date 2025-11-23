package Controller;

import Model.User.Instructor;
import Model.JsonDatabaseManager;
import Model.FileManager;
import Model.Course.Course;
import Model.Course.Lesson;
import Model.Course.COURSE_STATUS;

import java.io.File;
import java.util.ArrayList;

public class InstructorController {
    private JsonDatabaseManager dbManager;

    public InstructorController() {
        this.dbManager = JsonDatabaseManager.getInstance();
    }

    // --- GETTERS ---

    public ArrayList<Course> getCreatedCourses(int instructorId) {
        Instructor instructor = (Instructor) dbManager.getUserById(instructorId);
        ArrayList<Course> createdCourses = new ArrayList<>();
        if (instructor != null) {
            for (int courseId : instructor.getCreatedCourses()) {
                Course c = dbManager.getCourseById(courseId);
                if (c != null) {
                    createdCourses.add(c);
                }
            }
        }
        return createdCourses;
    }

    public ArrayList<Course> getCoursesByStatus(int instructorId, COURSE_STATUS status) {
        ArrayList<Course> allCreated = getCreatedCourses(instructorId);
        ArrayList<Course> filtered = new ArrayList<>();
        for (Course c : allCreated) {
            if (c.getStatus() == status) {
                filtered.add(c);
            }
        }
        return filtered;
    }

    // --- COURSE OPERATIONS ---

    public void addCourse(String title, String description, int instructorId, File imageFile) {
        // 1. Create Course Object (ID 0 initially)
        // Description is passed directly as plain text here.
        Course newCourse = new Course(0, title, description, instructorId);
        newCourse.setStatus(COURSE_STATUS.PENDING);

        // 2. Add to DB -> This generates the official ID (70xxxx) inside the object
        dbManager.addCourse(newCourse);

        // 3. Retrieve the generated ID for file operations
        int finalCourseId = newCourse.getCourseId();

        // 4. Link to Instructor
        Instructor instructor = (Instructor) dbManager.getUserById(instructorId);
        if (instructor != null) {
            instructor.addCreatedCourse(finalCourseId);
            dbManager.updateUser(instructor);
        }

        // 5. Handle Assets (Now using the correct final ID)
        boolean assetsUpdated = false;

        // Image Handling
        if (imageFile != null) {
            String savedImagePath = FileManager.saveCourseImage(imageFile, finalCourseId);
            if (savedImagePath != null) {
                newCourse.setImagePath(savedImagePath);
                assetsUpdated = true;
            }
        } else {
            // Default image logic
            newCourse.setImagePath("Database/Assets/Default_Img.png");
            assetsUpdated = true;
        }

        // 6. Update Course if assets changed (to save the image path)
        if (assetsUpdated) {
            dbManager.updateCourses(newCourse);
        }
    }

    public void updateCourse(Course course, String newTitle, String newDescription, File newImageFile) {
        course.setTitle(newTitle);

        // Update Description (Plain Text)
        if (newDescription != null) {
            course.setDescription(newDescription);
        }

        // Update Image
        if (newImageFile != null) {
            String savedImagePath = FileManager.saveCourseImage(newImageFile, course.getCourseId());
            course.setImagePath(savedImagePath);
        }

        // Revert to Pending on edit so Admin can review changes
        course.setStatus(COURSE_STATUS.PENDING);

        dbManager.updateCourses(course);
    }

    public void deleteCourse(Course course) {
        dbManager.removeCourse(course);
    }

    // --- LESSON OPERATIONS ---

    public void addLesson(int courseId, String title, String contentOrPath, boolean isFile, int position) {
        Course course = dbManager.getCourseById(courseId);
        if (course == null) return;

        // 1. Create Lesson (Temp ID)
        Lesson newLesson = new Lesson(0, title, "");

        // 2. Add to Course -> Generates official Lesson ID internally
        course.addLesson(newLesson);
        int finalLessonId = newLesson.getLessonId();

        // 3. Save Content (Binary) using official ID
        // Lesson content IS stored as a path to a binary file.
        String savedContentPath;
        if (isFile) {
            // Input is file path -> copy file to assets -> return new path
            savedContentPath = FileManager.saveFileToBinary(contentOrPath, courseId, finalLessonId);
        } else {
            // Input is text -> save text to file -> return new path
            savedContentPath = FileManager.saveTextToBinary(contentOrPath, courseId, finalLessonId);
        }

        if (savedContentPath != null) {
            newLesson.setContent(savedContentPath);
        }

        // 4. Handle Position
        // addLesson appends to end by default. If specific position requested:
        if (position >= 0 && position < course.getLessons().size()) {
            course.getLessons().remove(newLesson); // Remove from end
            course.getLessons().add(position, newLesson); // Insert at desired index
        }

        dbManager.updateCourses(course);
    }

    public void updateLesson(Course course, Lesson lesson, String newTitle, String newContentOrPath, boolean isFile, int newPosition) {
        lesson.setTitle(newTitle);

        // Update Content
        if (newContentOrPath != null) {
            String savedContentPath;
            if (isFile) {
                savedContentPath = FileManager.saveFileToBinary(newContentOrPath, course.getCourseId(), lesson.getLessonId());
            } else {
                savedContentPath = FileManager.saveTextToBinary(newContentOrPath, course.getCourseId(), lesson.getLessonId());
            }
            if (savedContentPath != null) {
                lesson.setContent(savedContentPath);
            }
        }

        // Update Position
        ArrayList<Lesson> lessons = course.getLessons();
        int currentPos = lessons.indexOf(lesson);

        // Only move if position changed and is valid
        if (currentPos != -1 && currentPos != newPosition && newPosition >= 0 && newPosition < lessons.size()) {
            lessons.remove(currentPos);
            lessons.add(newPosition, lesson);
        }

        dbManager.updateCourses(course);
    }

    public void deleteLesson(Course course, Lesson lesson) {
        dbManager.removeLesson(lesson);
    }
}