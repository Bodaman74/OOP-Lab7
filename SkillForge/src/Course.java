import java.util.ArrayList;
import java.util.List;

public class Course {

    private String courseId;
    private String title;
    private String description;
    private String instructorId;

    private List<Lesson> lessons = new ArrayList<>();
    private List<String> students = new ArrayList<>();

    public Course() {}

    public Course(String id, String title, String description, String instructorId) {
        this.courseId = id;
        this.title = title;
        this.description = description;
        this.instructorId = instructorId;
    }

    public String getCourseId() { return courseId; }
    public void setCourseId(String courseId) { this.courseId = courseId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getInstructorId() { return instructorId; }
    public void setInstructorId(String instructorId) { this.instructorId = instructorId; }

    public List<Lesson> getLessons() { return lessons; }
    public List<String> getStudents() { return students; }

    public void addLesson(Lesson lesson) { lessons.add(lesson); }

    public void removeLesson(String lessonId) {
        lessons.removeIf(l -> l.getLessonId().equals(lessonId));
    }

    public Lesson findLesson(String id) {
        for (Lesson l : lessons) {
            if (l.getLessonId().equals(id))
                return l;
        }
        return null;
    }

    public void enrollStudent(String studentId) {
        if (!students.contains(studentId))
            students.add(studentId);
    }
}
