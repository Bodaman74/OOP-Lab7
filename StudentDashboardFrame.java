import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class StudentDashboardFrame extends JFrame {

    private JsonDatabaseManager db;
    private Student student;

    private DefaultListModel<Course> allCoursesModel = new DefaultListModel<>();
    private JList<Course> allCoursesList;

    private DefaultListModel<Course> enrolledCoursesModel = new DefaultListModel<>();
    private JList<Course> enrolledCoursesList;

    private DefaultListModel<Lesson> lessonListModel = new DefaultListModel<>();
    private JList<Lesson> lessonList;

    private JTextArea lessonContentArea;
    private JTextField searchField;

    public StudentDashboardFrame(Student student, JsonDatabaseManager db) {
        this.student = student;
        this.db = db;

        setTitle("Student Dashboard – " + student.getUserName());
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createMainPanel(), BorderLayout.CENTER);

        loadAllCourses();
        loadEnrolledCourses();

        setVisible(true);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(40, 40, 45));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel title = new JLabel("Student Dashboard");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 22));

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });

        panel.add(title, BorderLayout.WEST);
        panel.add(logoutBtn, BorderLayout.EAST);

        return panel;
    }

    private JTabbedPane createMainPanel() {
        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Our Courses", createAllCoursesPanel());
        tabbedPane.addTab("Enrolled Courses", createEnrolledCoursesPanel());

        return tabbedPane;
    }

    private JPanel createAllCoursesPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel topPanel = new JPanel();
        searchField = new JTextField(20);
        JButton refreshBtn = new JButton("Refresh");
        JButton enrollBtn = new JButton("Enroll");

        topPanel.add(new JLabel("Search:"));
        topPanel.add(searchField);
        topPanel.add(refreshBtn);
        topPanel.add(enrollBtn);

        allCoursesList = new JList<>(allCoursesModel);
        allCoursesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scroll = new JScrollPane(allCoursesList);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);

        // Actions
        refreshBtn.addActionListener(e -> loadAllCourses());
        enrollBtn.addActionListener(e -> enrollSelectedCourse());
        searchField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) { filterCourses(); }
        });

        return panel;
    }

    private JPanel createEnrolledCoursesPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        enrolledCoursesList = new JList<>(enrolledCoursesModel);
        enrolledCoursesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        enrolledCoursesList.addListSelectionListener(e -> loadLessons());

        JScrollPane coursesScroll = new JScrollPane(enrolledCoursesList);

        lessonListModel = new DefaultListModel<>();
        lessonList = new JList<>(lessonListModel);
        lessonList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lessonList.addListSelectionListener(e -> loadLessonContent());

        lessonContentArea = new JTextArea();
        lessonContentArea.setEditable(false);
        lessonContentArea.setBorder(BorderFactory.createTitledBorder("Lesson Content"));

        JButton markCompleteBtn = new JButton("Mark as Completed");
        markCompleteBtn.addActionListener(e -> markLessonCompleted());

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(new JScrollPane(lessonList), BorderLayout.CENTER);
        rightPanel.add(lessonContentArea, BorderLayout.SOUTH);
        rightPanel.add(markCompleteBtn, BorderLayout.NORTH);

        panel.add(coursesScroll, BorderLayout.WEST);
        panel.add(rightPanel, BorderLayout.CENTER);

        return panel;
    }

    // ------------------ Backend Actions -------------------

    private void loadAllCourses() {
        allCoursesModel.clear();
        for (Course c : db.courses.values()) {
            allCoursesModel.addElement(c);
        }
    }

    private void filterCourses() {
        String query = searchField.getText().toLowerCase();
        allCoursesModel.clear();
        for (Course c : db.courses.values()) {
            if (c.getTitle().toLowerCase().contains(query))
                allCoursesModel.addElement(c);
        }
    }

    private void enrollSelectedCourse() {
        Course c = allCoursesList.getSelectedValue();
        if (c == null) return;

        if (student.getEnrolledCourses().contains(c.getCourseId())) {
            JOptionPane.showMessageDialog(this, "Already enrolled!");
            return;
        }

        student.getEnrolledCourses().add(c.getCourseId());
        c.enrollStudent(student.getUserId());

        db.putUser(student);
        db.putCourse(c);
        try { db.saveAll(); } catch (Exception e) { e.printStackTrace(); }

        JOptionPane.showMessageDialog(this, "Enrolled in course: " + c.getTitle());
        loadEnrolledCourses();
    }

    private void loadEnrolledCourses() {
        enrolledCoursesModel.clear();
        for (String courseId : student.getEnrolledCourses()) {
            Course c = db.getCourse(courseId);
            if (c != null) enrolledCoursesModel.addElement(c);
        }
        lessonListModel.clear();
        lessonContentArea.setText("");
    }

    private void loadLessons() {
        lessonListModel.clear();
        Course c = enrolledCoursesList.getSelectedValue();
        if (c == null) return;

        for (Lesson l : c.getLessons()) lessonListModel.addElement(l);
    }

    private void loadLessonContent() {
        Lesson l = lessonList.getSelectedValue();
        if (l == null) { lessonContentArea.setText(""); return; }
        lessonContentArea.setText(l.getContent());
    }

    private void markLessonCompleted() {
        Course c = enrolledCoursesList.getSelectedValue();
        Lesson l = lessonList.getSelectedValue();
        if (c == null || l == null) return;

        c.markLessonCompleted(student.getUserId(), l.getLessonId());

        db.putCourse(c);
        try { db.saveCourses(); } catch (Exception e) { e.printStackTrace(); }

        JOptionPane.showMessageDialog(this, "Lesson marked as completed!");
    }
}
