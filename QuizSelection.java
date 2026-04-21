package exam;

import javax.swing.*;
import java.sql.*;
import java.awt.*;
import java.util.HashMap;

public class QuizSelection extends JFrame {

    int student_id;
    JComboBox<String> list;
    HashMap<String,Integer> map=new HashMap<>();

    QuizSelection(int id) {
        student_id=id;

        setTitle("Select Quiz");
        setSize(450,250);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(null);
        panel.setBackground(UIStyle.bg);

        JPanel card = new JPanel(null);
        card.setBounds(40,40,350,130);
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        JLabel title = new JLabel("Select a Quiz");
        title.setFont(UIStyle.titleFont);
        title.setBounds(100,10,200,25);

        list = new JComboBox<>();
        list.setBounds(40,50,260,30);

        JButton start = new JButton("Start");
        start.setBounds(110,90,120,30);

        start.setBackground(UIStyle.primary);
        start.setForeground(Color.WHITE);

        card.add(title);
        card.add(list);
        card.add(start);

        panel.add(card);
        add(panel);

        load();

        start.addActionListener(e -> {
            String s=(String)list.getSelectedItem();
            new AttemptQuiz(student_id,map.get(s));
            dispose();
        });

        setVisible(true);
    }

    void load() {
        try {
            Connection con=DBConnection.getConnection();

            ResultSet rs=con.createStatement().executeQuery(
                "SELECT quizzes.quiz_id,title,username FROM quizzes JOIN users ON quizzes.created_by=users.user_id"
            );

            while(rs.next()) {
                String name=rs.getString("title")+" (by "+rs.getString("username")+")";
                list.addItem(name);
                map.put(name,rs.getInt("quiz_id"));
            }

        } catch(Exception e){ e.printStackTrace(); }
    }
}