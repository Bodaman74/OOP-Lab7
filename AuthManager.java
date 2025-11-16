package Lab7 ;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AuthManager {

    private final String USERS_FILE = "users.json";
    private List<user> users = new ArrayList<>();
    private user loggeduser = null;

    private Gson gson = new Gson();

    public AuthManager() {
        loadusers();
    }

    // Get logged user
    public user getLoggeduser() {
        return loggeduser;
    }

    // =============================
    // Load users from JSON file
    // =============================
    private void loadusers() {
        try {
            File file = new File(USERS_FILE);

            if (!file.exists()) {
                saveusers(); // Create empty file
                return;
            }

            FileReader reader = new FileReader(file);

            users = gson.fromJson(reader, new TypeToken<List<user>>(){}.getType());

            if (users == null) {
                users = new ArrayList<>();
            }

            reader.close();

        } catch (Exception e) {
            System.out.println("Error loading users!");
        }
    }

    // =============================
    // Save users to JSON file
    // =============================
    private void saveusers() {
        try {
            FileWriter writer = new FileWriter(USERS_FILE);
            gson.toJson(users, writer);
            writer.close();
        } catch (Exception e) {
            System.out.println("Error saving users!");
        }
    }

    // =============================
    // SIGNUP
    // =============================
    public String signup(String username, String email, String password, String role) {

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            return "All fields are required!";
        }

        if (!email.contains("@") || !email.contains(".")) {
            return "Invalid email!";
        }

        for (user u : users) {
            if (u.getUserName().equalsIgnoreCase(username)) {
                return "username already exists!";
            }
        }

        String hashed = hashPassword(password);

        String id = UUID.randomUUID().toString();

        user newuser = new user(id, username, email, hashed, role);

        users.add(newuser);
        saveusers();

        return "SUCCESS";
    }

    // =============================
    // LOGIN
    // =============================
    public String login(String username, String password) {

        String hashed = hashPassword(password);

        for (user u : users) {
            if (u.getUserName().equalsIgnoreCase(username)
                    && u.getPasswordHash().equals(hashed)) {

                loggeduser = u;
                return "SUCCESS";
            }
        }

        return "Wrong username or password!";
    }

    // =============================
    // LOGOUT
    // =============================
    public void logout() {
        loggeduser = null;
    }

    // =============================
    // PASSWORD HASHING (SHA-256)
    // =============================
    private String hashPassword(String pass) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(pass.getBytes());

            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }

            return hexString.toString();

        } catch (Exception e) {
            return null;
        }
    }
}
