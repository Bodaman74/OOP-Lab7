/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab7;

/**
 *
 * @author Marwan
 */
import javax.swing.*;

public class InstructorDashboardFrame extends JFrame {

    private AuthManager auth;

    public InstructorDashboardFrame() {
        auth = new AuthManager();

        setTitle("Instructor Dashboard");
        setSize(400, 350);
        setLayout(null);

        JLabel welcome = new JLabel("Welcome, Instructor!");
        welcome.setBounds(130, 20, 200, 30);
        add(welcome);

        JButton createCourse = new JButton("Create Course");
        createCourse.setBounds(100, 70, 200, 30);
        add(createCourse);

        JButton manageCourses = new JButton("My Courses");
        manageCourses.setBounds(100, 120, 200, 30);
        add(manageCourses);

        JButton viewStudents = new JButton("View Enrolled Students");
        viewStudents.setBounds(100, 170, 200, 30);
        add(viewStudents);

        JButton logout = new JButton("Logout");
        logout.setBounds(140, 230, 120, 30);
        add(logout);

        logout.addActionListener(e -> {
            auth.logout();
            new LoginFrame().setVisible(true);
            this.dispose();
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}

