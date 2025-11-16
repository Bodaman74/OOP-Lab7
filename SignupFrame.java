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

public class SignupFrame extends JFrame {

    private JTextField usernameField, emailField;
    private JPasswordField passwordField;
    private JComboBox<String> roleBox;

    private AuthManager auth = new AuthManager();

    public SignupFrame() {
        setTitle("Signup");
        setSize(350, 350);
        setLayout(null);

        JLabel u = new JLabel("Username:");
        u.setBounds(30, 30, 100, 30);
        add(u);

        usernameField = new JTextField();
        usernameField.setBounds(130, 30, 150, 30);
        add(usernameField);

        JLabel e = new JLabel("Email:");
        e.setBounds(30, 80, 100, 30);
        add(e);

        emailField = new JTextField();
        emailField.setBounds(130, 80, 150, 30);
        add(emailField);

        JLabel p = new JLabel("Password:");
        p.setBounds(30, 130, 100, 30);
        add(p);

        passwordField = new JPasswordField();
        passwordField.setBounds(130, 130, 150, 30);
        add(passwordField);

        JLabel r = new JLabel("Role:");
        r.setBounds(30, 180, 100, 30);
        add(r);

        roleBox = new JComboBox<>(new String[]{"student", "instructor"});
        roleBox.setBounds(130, 180, 150, 30);
        add(roleBox);

        JButton signupBtn = new JButton("Signup");
        signupBtn.setBounds(100, 240, 120, 40);
        add(signupBtn);

        signupBtn.addActionListener(e1 -> signup());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void signup() {
        String username = usernameField.getText();
        String email = emailField.getText();
        String pass = new String(passwordField.getPassword());
        String role = roleBox.getSelectedItem().toString();

        String result = auth.signup(username, email, pass, role);

        if (!result.equals("SUCCESS")) {
            JOptionPane.showMessageDialog(this, result);
            return;
        }

        JOptionPane.showMessageDialog(this, "Account created successfully!");
        new LoginFrame().setVisible(true);
        this.dispose();
    }
}
