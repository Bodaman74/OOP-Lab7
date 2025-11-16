/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab7;

/**
 *
 * @author DELL
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Course {
    private String courseId;
    private String title;
    private String description;
    private String instructorId;
    private ArrayList<Lesson> lessons;
    private ArrayList<String> students; // student IDs
    private Map<String, ArrayList<String>> progress = new HashMap<>();


    public Course() {
        lessons = new ArrayList<>();
        students = new ArrayList<>();
    }

    public Course(String courseId, String title, String desc, String instructorId) {
        this();
        this.courseId = courseId;
        this.title = title;
        this.description = desc;
        this.instructorId = instructorId;
    }

    // getters & setters
    public String getCourseId(){
        return courseId;
    }
    public void setCourseId(String id){ 
        this.courseId = id;
    }
    public String getTitle(){ 
        return title; 
    }
    public void setTitle(String t){ 
        this.title = t;
    }
    public String getDescription(){ 
        return description;
    }
    public void setDescription(String d){ 
        this.description = d;
    }
    public String getInstructorId(){
        return instructorId;
    }
    public void setInstructorId(String id){
        this.instructorId = id;
    }
    public ArrayList<Lesson> getLessons(){
        return lessons;
    }
    public ArrayList<String> getStudents(){
        return students;
    }
    public void setLessons(ArrayList<Lesson> lessons) {
    this.lessons = lessons;
}

public void setStudents(ArrayList<String> students) {
    this.students = students;
}
public Map<String, ArrayList<String>> getProgress() {
    return progress;
}

public void setProgress(Map<String, ArrayList<String>> progress) {
    this.progress = progress;
}


    // convenience methods
    public void addLesson(Lesson l){ 
        lessons.add(l);
    }
    public boolean removeLessonById(String lessonId){
        return lessons.removeIf(l -> l.getLessonId().equals(lessonId));
    }
    public Lesson getLessonById(String lessonId){
        return lessons.stream().filter(l -> l.getLessonId().equals(lessonId)).findFirst().orElse(null);
    }
    public void enrollStudent(String studentId){
        if(!students.contains(studentId)) students.add(studentId);
    }
    public void unenrollStudent(String studentId){
        students.remove(studentId);
    }
    public void markLessonCompleted(String studentId, String lessonId){
    progress.putIfAbsent(studentId, new ArrayList<>());
    if (!progress.get(studentId).contains(lessonId)) {
        progress.get(studentId).add(lessonId);
    }
}

}

