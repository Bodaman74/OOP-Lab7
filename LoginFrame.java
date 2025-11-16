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

public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;

    private AuthManager auth = new AuthManager();

    public LoginFrame() {
        setTitle("Login");
        setSize(350, 250);
        setLayout(null);

        JLabel u = new JLabel("Username:");
        u.setBounds(30, 30, 100, 30);
        add(u);

        usernameField = new JTextField();
        usernameField.setBounds(130, 30, 150, 30);
        add(usernameField);

        JLabel p = new JLabel("Password:");
        p.setBounds(30, 80, 100, 30);
        add(p);

        passwordField = new JPasswordField();
        passwordField.setBounds(130, 80, 150, 30);
        add(passwordField);

        JButton loginBtn = new JButton("Login");
        loginBtn.setBounds(50, 140, 100, 30);
        add(loginBtn);

        JButton signupBtn = new JButton("Signup");
        signupBtn.setBounds(170, 140, 100, 30);
        add(signupBtn);

        loginBtn.addActionListener(e -> login());
        signupBtn.addActionListener(e -> openSignup());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void login() {
        String user = usernameField.getText();
        String pass = new String(passwordField.getPassword());

        String result = auth.login(user, pass);

        if (!result.equals("SUCCESS")) {
            JOptionPane.showMessageDialog(this, result);
            return;
        }

        JOptionPane.showMessageDialog(this, "Login successful!");

        user logged = auth.getLoggedUser();

        if (logged.getRole().equals("student")) {
            new StudentDashboardFrame().setVisible(true);
        } else {
            new InstructorDashboardFrame().setVisible(true);
        }

        this.dispose();
    }

    private void openSignup() {
        new SignupFrame().setVisible(true);
        this.dispose();
    }
}
