/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package lab7;
import com.google.gson.Gson;
import javax.swing.SwingUtilities;
/**
 *
 * @author DELL
 */
public class Lab7 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
   JsonDatabaseManager db = new JsonDatabaseManager("data/courses.json");

        // Student view
        SwingUtilities.invokeLater(() -> {
            StudentView studentView = new StudentView(db, "C101", "S001");
            studentView.setVisible(true);
        });

        // Instructor view
        SwingUtilities.invokeLater(() -> {
            InstructorView instructorView = new InstructorView(db, "C101");
            instructorView.setVisible(true);
        });
    }
}