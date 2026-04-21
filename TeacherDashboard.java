package exam;

import javax.swing.*;
import java.awt.*;

public class TeacherDashboard extends JFrame {

    int teacher_id;

    TeacherDashboard(int id) {
        teacher_id = id;

        setTitle("Teacher Dashboard");
        setSize(350,250);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(UIStyle.bg);

        JLabel title = new JLabel("Teacher Panel");
        title.setFont(UIStyle.titleFont);
        title.setBounds(100,20,200,30);

        JButton create = new JButton("Create Quiz");
        JButton view = new JButton("View Results");

        create.setBounds(80,80,180,35);
        view.setBounds(80,130,180,35);

        styleButton(create);
        styleButton(view);

        panel.add(title);
        panel.add(create);
        panel.add(view);

        add(panel);

        create.addActionListener(e -> new CreateQuizFrame(teacher_id));
        view.addActionListener(e -> new ViewResults(teacher_id));

        setVisible(true);
    }

    void styleButton(JButton b) {
        b.setBackground(UIStyle.primary);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
    }
}