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

public class StudentDashboardFrame extends JFrame {

    private AuthManager auth;

    public StudentDashboardFrame() {
        auth = new AuthManager();

        setTitle("Student Dashboard");
        setSize(400, 300);
        setLayout(null);

        JLabel welcome = new JLabel("Welcome, Student!");
        welcome.setBounds(130, 20, 200, 30);
        add(welcome);

        JButton viewCourses = new JButton("View Available Courses");
        viewCourses.setBounds(100, 70, 200, 30);
        add(viewCourses);

        JButton enrolled = new JButton("My Enrolled Courses");
        enrolled.setBounds(100, 120, 200, 30);
        add(enrolled);

        JButton logout = new JButton("Logout");
        logout.setBounds(140, 180, 120, 30);
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
