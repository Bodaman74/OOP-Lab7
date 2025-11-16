/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab7;

/**
 *
 * @author DELL
 */
// JsonDatabaseManager.java
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.io.*;
import java.util.ArrayList;

public class JsonDatabaseManager {
    private final File coursesFile;
    private final Gson gson;

    public JsonDatabaseManager(String coursesFilePath) {
        this.coursesFile = new File(coursesFilePath);
        this.gson = new Gson();
        ensureFileExists();
    }

    private void ensureFileExists() {
        try {
            if(!coursesFile.exists()){
                coursesFile.getParentFile().mkdirs();
                try (Writer w = new FileWriter(coursesFile)) {
                    w.write("[]");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Course> loadCourses(){
        try (Reader r = new FileReader(coursesFile)) {
            Type listType = new TypeToken<ArrayList<Course>>(){}.getType();
           ArrayList<Course> list = gson.fromJson(r, listType);
            return list == null ? new ArrayList<>() : list;
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public synchronized void saveCourses(ArrayList<Course> courses){
        try (Writer w = new FileWriter(coursesFile)) {
            gson.toJson(courses, w);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // helpers
    public Course getCourseById(String id){
        return loadCourses().stream().filter(c -> c.getCourseId().equals(id)).findFirst().orElse(null);
    }

    public boolean isCourseIdDuplicate(String id){
        return getCourseById(id) != null;
    }
    private void validateCourse(Course c) {
    if (c.getCourseId() == null || c.getCourseId().isEmpty())
        throw new IllegalArgumentException("Course ID cannot be empty");

    if (c.getTitle() == null || c.getTitle().isEmpty())
        throw new IllegalArgumentException("Course title cannot be empty");

    if (c.getDescription() == null || c.getDescription().isEmpty())
        throw new IllegalArgumentException("Course description cannot be empty");

    if (c.getInstructorId() == null || c.getInstructorId().isEmpty())
        throw new IllegalArgumentException("Instructor ID cannot be empty");

    if (isCourseIdDuplicate(c.getCourseId()))
        throw new IllegalArgumentException("Duplicate Course ID");
}


    public void addCourse(Course course) {
    validateCourse(course);
ArrayList<Course> list = loadCourses();
    list.add(course);
    saveCourses(list);
}


    public void updateCourse(Course updated){
        ArrayList<Course> courses = loadCourses();
        for (int i = 0; i < courses.size(); i++) {
            if(courses.get(i).getCourseId().equals(updated.getCourseId())){
                courses.set(i, updated);
                saveCourses(courses);
                return;
            }
        }
        // not found -> optional: throw
    }

    public void deleteCourse(String courseId){
        ArrayList<Course> courses = loadCourses();
        courses.removeIf(c -> c.getCourseId().equals(courseId));
        saveCourses(courses);
    }
    private void validateLesson(Lesson l) {
    if (l.getLessonId() == null || l.getLessonId().isEmpty())
        throw new IllegalArgumentException("Lesson ID cannot be empty");

    if (l.getTitle() == null || l.getTitle().isEmpty())
        throw new IllegalArgumentException("Lesson title cannot be empty");

    if (l.getContent() == null || l.getContent().isEmpty())
        throw new IllegalArgumentException("Lesson content cannot be empty");
}
    // add lesson
    public void addLessonToCourse(String courseId, Lesson lesson) {
    validateLesson(lesson);

    Course c = getCourseById(courseId);
    if (c == null) return;

    // check duplicate lessonId
    if (c.getLessonById(lesson.getLessonId()) != null)
        throw new IllegalArgumentException("Duplicate Lesson ID");

    c.addLesson(lesson);
    updateCourse(c);
}


    // delete lesson
    public void deleteLessonFromCourse(String courseId, String lessonId) {
    Course c = getCourseById(courseId);
    if (c == null) return;

    c.getLessons().removeIf(l -> l.getLessonId().equals(lessonId));
    updateCourse(c);
}   
    // edit lessons
    public void editLessonInCourse(String courseId, Lesson updatedLesson) {
    Course c = getCourseById(courseId);
    if (c == null) return;

    for (int i = 0; i < c.getLessons().size(); i++) {
        if (c.getLessons().get(i).getLessonId().equals(updatedLesson.getLessonId())) {
            c.getLessons().set(i, updatedLesson);
            updateCourse(c);
            return;
        }
    }
}
// add student ID to the course
public void enrollStudent(String courseId, String studentId) {
    Course c = getCourseById(courseId);
    if (c == null) return;

    if (!c.getStudents().contains(studentId)) {
        c.getStudents().add(studentId);
        updateCourse(c);
    }
}
// fetch lessons by course
public ArrayList<Lesson> getLessonsByCourse(String courseId) {
    Course c = getCourseById(courseId);
    if (c != null) return c.getLessons();
    return new ArrayList<>(); // 
}
public void markLessonCompletedInCourse(String courseId, String studentId, String lessonId) {
    Course c = getCourseById(courseId);
    if (c == null) return;

    c.markLessonCompleted(studentId, lessonId);
    updateCourse(c);
}

public boolean isLessonCompleted(String courseId, String studentId, String lessonId) {
    Course c = getCourseById(courseId);
    if (c == null) return false;
    ArrayList<String> studentProgress = c.getProgress().get(studentId);
    return studentProgress != null && studentProgress.contains(lessonId);
}

}
