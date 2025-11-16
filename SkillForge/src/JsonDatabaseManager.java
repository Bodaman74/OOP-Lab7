import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class JsonDatabaseManager {

    private static final String USERS_FILE = "users.json";
    private static final String COURSES_FILE = "courses.json";

    private Map<String, User> users = new HashMap<>();
    private Map<String, Course> courses = new HashMap<>();

    private Gson gson;

    public JsonDatabaseManager() {
        gson = new GsonBuilder().setPrettyPrinting().create();
        load();
    }

    // ======== LOADING ========
    private void load() {

        // Load users
        try {
            if (Files.exists(Paths.get(USERS_FILE))) {
                String json = new String(Files.readAllBytes(Paths.get(USERS_FILE)));
                Type type = new TypeToken<Map<String, JsonObject>>() {}.getType();
                Map<String, JsonObject> raw = gson.fromJson(json, type);

                if (raw != null) {
                    for (var entry : raw.entrySet()) {
                        JsonObject obj = entry.getValue();
                        String role = obj.get("role").getAsString();

                        User u;

                        if (role.equals("instructor"))
                            u = gson.fromJson(obj, Instructor.class);
                        else if (role.equals("student"))
                            u = gson.fromJson(obj, Student.class);
                        else
                            u = gson.fromJson(obj, User.class);

                        users.put(entry.getKey(), u);
                    }
                }
            }
        } catch (Exception e) { e.printStackTrace(); }

        // Load courses
        try {
            if (Files.exists(Paths.get(COURSES_FILE))) {
                String json = new String(Files.readAllBytes(Paths.get(COURSES_FILE)));
                Type type = new TypeToken<Map<String, Course>>() {}.getType();
                Map<String, Course> raw = gson.fromJson(json, type);

                if (raw != null)
                    courses.putAll(raw);
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    // ======== SAVE ========

    public synchronized void saveUsers() throws IOException {
        try (Writer w = new FileWriter(USERS_FILE)) {
            gson.toJson(users, w);
        }
    }

    public synchronized void saveCourses() throws IOException {
        try (Writer w = new FileWriter(COURSES_FILE)) {
            gson.toJson(courses, w);
        }
    }

    public synchronized void saveAll() throws IOException {
        saveUsers();
        saveCourses();
    }

    // ======== USER FUNCTIONS ========

    public User getUser(String id) { return users.get(id); }

    public void putUser(User user) {
        users.put(user.getUserId(), user);
    }

    public boolean userExists(String id) {
        return users.containsKey(id);
    }

    // ======== COURSE FUNCTIONS ========

    public Course getCourse(String id) { return courses.get(id); }

    public void putCourse(Course course) {
        courses.put(course.getCourseId(), course);
    }

    public void removeCourse(String id) {
        courses.remove(id);
    }

    public boolean courseExists(String id) {
        return courses.containsKey(id);
    }

    public List<Student> getEnrolledStudents(String courseId) {
        Course c = getCourse(courseId);
        List<Student> out = new ArrayList<>();

        if (c == null) return out;

        for (String studentId : c.getStudents()) {
            User u = users.get(studentId);
            if (u instanceof Student)
                out.add((Student) u);
        }
        return out;
    }
}
