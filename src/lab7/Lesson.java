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

public class Lesson {
    private String lessonId;
    private String title;
    private String content;
    private ArrayList<String> resources;

    public Lesson() {
        resources = new ArrayList<>();
    }
    public Lesson(String lessonId, String title, String content) {
        this.lessonId = lessonId;
        this.title = title;
        this.content = content;
        this.resources = new ArrayList<>();
    }
    // getters & setters
    public String getLessonId(){ 
        return lessonId; 
    }
    public void setLessonId(String id){ 
        this.lessonId = id;
    }
    public String getTitle(){ 
        return title;
    }
    public void setTitle(String t){ 
        this.title = t;
    }
    public String getContent(){ 
        return content;
    }
    public void setContent(String c){ 
        this.content = c;
    }
    public ArrayList<String> getResources(){ 
        return resources; 
    }
    public void setResources(ArrayList<String> r){
        this.resources = r;
    }
}

