import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.UUID;

public class InstructorDashboardFrame extends JFrame {

    private Instructor instructor;
    private JsonDatabaseManager db;

    private DefaultListModel<Course> courseListModel = new DefaultListModel<>();
    private JList<Course> courseList;

    private DefaultListModel<Lesson> lessonListModel = new DefaultListModel<>();
    private JList<Lesson> lessonList;

    private JTextArea lessonContentArea;

    public InstructorDashboardFrame(Instructor instructor, JsonDatabaseManager db) {
        this.instructor = instructor;
        this.db = db;

        setTitle("Instructor Dashboard – " + instructor.getUsername());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createMainPanel(), BorderLayout.CENTER);

        loadInstructorCourses();

        setVisible(true);
    }

  
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(40, 40, 45));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel title = new JLabel("Instructor Dashboard");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 22));

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.addActionListener(e -> {
            dispose();
            JOptionPane.showMessageDialog(null, "Logged out.");
            new LoginFrame(db);
        });

        panel.add(title, BorderLayout.WEST);
        panel.add(logoutBtn, BorderLayout.EAST);

        return panel;
    }

    // ---------------------- MAIN PANEL -------------------------
    private JPanel createMainPanel() {

        JPanel main = new JPanel(new GridLayout(1, 2));

        main.add(createCoursesPanel());
        main.add(createLessonsPanel());

        return main;
    }

 
    private JPanel createCoursesPanel() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel lbl = new JLabel("Your Courses");
        lbl.setFont(new Font("Arial", Font.BOLD, 18));

        courseList = new JList<>(courseListModel);
        courseList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        courseList.addListSelectionListener(e -> loadCourseLessons());

        JScrollPane scroll = new JScrollPane(courseList);

        JPanel btnPanel = new JPanel(new GridLayout(1, 3, 10, 10));

        JButton add = new JButton("Add");
        JButton edit = new JButton("Edit");
        JButton delete = new JButton("Delete");

        add.addActionListener(e -> createCourseDialog());
        edit.addActionListener(e -> editCourseDialog());
        delete.addActionListener(e -> deleteCourse());

        btnPanel.add(add);
        btnPanel.add(edit);
        btnPanel.add(delete);

        panel.add(lbl, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(btnPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void loadInstructorCourses() {
        courseListModel.clear();
        for (String courseId : instructor.getCreatedCourses()) {
            Course c = db.getCourse(courseId);
            if (c != null)
                courseListModel.addElement(c);
        }
    }

    private void createCourseDialog() {
        JTextField titleField = new JTextField();
        JTextArea descArea = new JTextArea(4, 20);

        int option = JOptionPane.showConfirmDialog(
                null,
                new Object[]{"Title:", titleField, "Description:", new JScrollPane(descArea)},
                "Create Course",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (option == JOptionPane.OK_OPTION) {

            String id = UUID.randomUUID().toString();
            Course c = new Course(id, titleField.getText(), descArea.getText(), instructor.getUserId());

            instructor.addCreatedCourse(id);
            db.putUser(instructor);
            db.putCourse(c);

            try {
                db.saveAll();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            courseListModel.addElement(c);
        }
    }

  
    private void editCourseDialog() {
        Course c = courseList.getSelectedValue();
        if (c == null) {
            JOptionPane.showMessageDialog(null, "Select a course first.");
            return;
        }

        JTextField titleField = new JTextField(c.getTitle());
        JTextArea descArea = new JTextArea(c.getDescription(), 4, 20);

        int option = JOptionPane.showConfirmDialog(
                null,
                new Object[]{"Title:", titleField, "Description:", new JScrollPane(descArea)},
                "Edit Course",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (option == JOptionPane.OK_OPTION) {

            c.setTitle(titleField.getText());
            c.setDescription(descArea.getText());

            try {
                db.saveCourses();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            courseList.repaint();
        }
    }

 
    private void deleteCourse() {
        Course c = courseList.getSelectedValue();
        if (c == null) {
            JOptionPane.showMessageDialog(null, "Select a course first.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(null,
                "Are you sure you want to delete this course?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            instructor.removeCreatedCourse(c.getCourseId());
            db.removeCourse(c.getCourseId());

            try {
                db.saveAll();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            courseListModel.removeElement(c);
            lessonListModel.clear();
        }
    }


   
    private JPanel createLessonsPanel() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel lbl = new JLabel("Lessons");
        lbl.setFont(new Font("Arial", Font.BOLD, 18));

        lessonList = new JList<>(lessonListModel);
        lessonList.addListSelectionListener(e -> loadLessonContent());

        JScrollPane scroll = new JScrollPane(lessonList);

        JPanel btnPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        JButton add = new JButton("Add");
        JButton edit = new JButton("Edit");
        JButton delete = new JButton("Delete");

        add.addActionListener(e -> createLessonDialog());
        edit.addActionListener(e -> editLessonDialog());
        delete.addActionListener(e -> deleteLesson());

        btnPanel.add(add);
        btnPanel.add(edit);
        btnPanel.add(delete);

        lessonContentArea = new JTextArea();
        lessonContentArea.setEditable(false);
        lessonContentArea.setBorder(BorderFactory.createTitledBorder("Lesson Content"));

        panel.add(lbl, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(btnPanel, BorderLayout.SOUTH);
        panel.add(lessonContentArea, BorderLayout.EAST);

        return panel;
    }

    private void loadCourseLessons() {
        lessonListModel.clear();

        Course c = courseList.getSelectedValue();
        if (c == null) return;

        for (Lesson l : c.getLessons()) {
            lessonListModel.addElement(l);
        }
    }

    private void loadLessonContent() {
        Lesson l = lessonList.getSelectedValue();
        if (l == null) {
            lessonContentArea.setText("");
            return;
        }
        lessonContentArea.setText(l.getContent());
    }


    private void createLessonDialog() {
        Course c = courseList.getSelectedValue();
        if (c == null) {
            JOptionPane.showMessageDialog(null, "Select a course first.");
            return;
        }

        JTextField titleField = new JTextField();
        JTextArea contentArea = new JTextArea(4, 20);

        int option = JOptionPane.showConfirmDialog(
                null,
                new Object[]{"Title:", titleField, "Content:", new JScrollPane(contentArea)},
                "Create Lesson",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (option == JOptionPane.OK_OPTION) {

            Lesson l = new Lesson(UUID.randomUUID().toString(),
                    titleField.getText(),
                    contentArea.getText());

            c.addLesson(l);

            try {
                db.saveCourses();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            lessonListModel.addElement(l);
        }
    }

 
    private void editLessonDialog() {
        Course c = courseList.getSelectedValue();
        Lesson l = lessonList.getSelectedValue();

        if (c == null || l == null) {
            JOptionPane.showMessageDialog(null, "Select a lesson first.");
            return;
        }

        JTextField titleField = new JTextField(l.getTitle());
        JTextArea contentArea = new JTextArea(l.getContent(), 4, 20);

        int option = JOptionPane.showConfirmDialog(
                null,
                new Object[]{"Title:", titleField, "Content:", new JScrollPane(contentArea)},
                "Edit Lesson",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (option == JOptionPane.OK_OPTION) {

            l.setTitle(titleField.getText());
            l.setContent(contentArea.getText());

            try {
                db.saveCourses();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            lessonList.repaint();
            lessonContentArea.setText(l.getContent());
        }
    }

 
    private void deleteLesson() {
        Course c = courseList.getSelectedValue();
        Lesson l = lessonList.getSelectedValue();

        if (c == null || l == null) {
            JOptionPane.showMessageDialog(null, "Select a lesson first.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                null,
                "Are you sure you want to delete this lesson?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            c.removeLesson(l.getLessonId());

            try {
                db.saveCourses();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            lessonListModel.removeElement(l);
            lessonContentArea.setText("");
        }
    }
}
