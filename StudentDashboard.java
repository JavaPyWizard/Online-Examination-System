package exam;

import javax.swing.*;
import java.awt.*;

public class StudentDashboard extends JFrame {

    int student_id;

    StudentDashboard(int id) {
        student_id=id;

        setTitle("Student Dashboard");
        setSize(350,250);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(null);
        panel.setBackground(UIStyle.bg);

        JLabel title = new JLabel("Student Panel");
        title.setFont(UIStyle.titleFont);
        title.setBounds(100,30,200,30);

        JButton attempt = new JButton("Attempt Quiz");
        attempt.setBounds(80,100,180,35);

        attempt.setBackground(UIStyle.primary);
        attempt.setForeground(Color.WHITE);

        panel.add(title);
        panel.add(attempt);

        add(panel);

        attempt.addActionListener(e -> new QuizSelection(student_id));

        setVisible(true);
    }
}