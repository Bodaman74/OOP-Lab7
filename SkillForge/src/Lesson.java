import java.util.ArrayList;
import java.util.List;

public class Lesson {

    private String lessonId;
    private String title;
    private String content;
    private List<String> resources = new ArrayList<>();

    public Lesson() {}

    public Lesson(String id, String title, String content) {
        this.lessonId = id;
        this.title = title;
        this.content = content;
    }

    public String getLessonId() { return lessonId; }
    public void setLessonId(String lessonId) { this.lessonId = lessonId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public List<String> getResources() { return resources; }
}
