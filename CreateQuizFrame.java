package exam;

import javax.swing.*;
import java.sql.*;
import java.awt.*;

public class CreateQuizFrame extends JFrame {

    int teacher_id, quiz_id;
    JTextField title, timeField, q,o1,o2,o3,o4,correct;

    CreateQuizFrame(int id) {

        teacher_id = id;

        setTitle("Create Quiz");
        setSize(450,450);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(null);
        panel.setBackground(UIStyle.bg);

        JPanel card = new JPanel(null);
        card.setBounds(40,30,350,180);
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        JLabel l0 = new JLabel("Quiz Title:");
        JLabel ltime = new JLabel("Time (seconds):");

        title = new JTextField();
        timeField = new JTextField();

        JButton createQuiz = new JButton("Create Quiz");

        l0.setBounds(20,30,120,25);
        title.setBounds(150,30,160,25);

        ltime.setBounds(20,70,120,25);
        timeField.setBounds(150,70,160,25);

        createQuiz.setBounds(100,120,140,30);

        createQuiz.setBackground(UIStyle.primary);
        createQuiz.setForeground(Color.WHITE);

        card.add(l0); card.add(title);
        card.add(ltime); card.add(timeField);
        card.add(createQuiz);

        panel.add(card);
        add(panel);

        createQuiz.addActionListener(e -> createQuiz());

        setVisible(true);
    }

    void createQuiz() {
        try {
            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO quizzes(title,created_by,time_limit) VALUES(?,?,?)",
                Statement.RETURN_GENERATED_KEYS
            );

            ps.setString(1,title.getText());
            ps.setInt(2,teacher_id);
            ps.setInt(3,Integer.parseInt(timeField.getText()));

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            quiz_id = rs.getInt(1);

            showForm();

        } catch(Exception e){ e.printStackTrace(); }
    }

    void showForm() {

        getContentPane().removeAll();

        JPanel panel = new JPanel(null);
        panel.setBackground(UIStyle.bg);

        JPanel form = new JPanel(null);
        form.setBounds(40,30,350,340);
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        JLabel title = new JLabel("Add Question");
        title.setFont(UIStyle.titleFont);
        title.setBounds(110,10,200,25);

        JLabel l1=new JLabel("Question:");
        JLabel l2=new JLabel("Option A:");
        JLabel l3=new JLabel("Option B:");
        JLabel l4=new JLabel("Option C:");
        JLabel l5=new JLabel("Option D:");
        JLabel l6=new JLabel("Correct:");

        q=new JTextField();
        o1=new JTextField();
        o2=new JTextField();
        o3=new JTextField();
        o4=new JTextField();
        correct=new JTextField();

        int y=50;

        l1.setBounds(20,y,100,25); q.setBounds(120,y,200,25); y+=35;
        l2.setBounds(20,y,100,25); o1.setBounds(120,y,200,25); y+=35;
        l3.setBounds(20,y,100,25); o2.setBounds(120,y,200,25); y+=35;
        l4.setBounds(20,y,100,25); o3.setBounds(120,y,200,25); y+=35;
        l5.setBounds(20,y,100,25); o4.setBounds(120,y,200,25); y+=35;
        l6.setBounds(20,y,100,25); correct.setBounds(120,y,200,25);

        JButton addQ=new JButton("Add");
        JButton finish=new JButton("Finish");

        addQ.setBounds(70,290,90,30);
        finish.setBounds(180,290,90,30);

        addQ.setBackground(UIStyle.primary);
        finish.setBackground(Color.GRAY);

        addQ.setForeground(Color.WHITE);
        finish.setForeground(Color.WHITE);

        form.add(title);
        form.add(l1); form.add(q);
        form.add(l2); form.add(o1);
        form.add(l3); form.add(o2);
        form.add(l4); form.add(o3);
        form.add(l5); form.add(o4);
        form.add(l6); form.add(correct);
        form.add(addQ); form.add(finish);

        panel.add(form);
        add(panel);

        addQ.addActionListener(e -> saveQ());
        finish.addActionListener(e -> dispose());

        revalidate();
        repaint();
    }

    void saveQ() {
        try {
            Connection con = DBConnection.getConnection();

            int correctOption = switch(correct.getText().toUpperCase()) {
                case "A" -> 1;
                case "B" -> 2;
                case "C" -> 3;
                case "D" -> 4;
                default -> 0;
            };

            if(correctOption==0) {
                JOptionPane.showMessageDialog(this,"Enter A-D");
                return;
            }

            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO questions VALUES(NULL,?,?,?,?,?,?,?)"
            );

            ps.setInt(1,quiz_id);
            ps.setString(2,q.getText());
            ps.setString(3,o1.getText());
            ps.setString(4,o2.getText());
            ps.setString(5,o3.getText());
            ps.setString(6,o4.getText());
            ps.setInt(7,correctOption);

            ps.executeUpdate();

            JOptionPane.showMessageDialog(this,"Question Added!");

            q.setText("");o1.setText("");o2.setText("");
            o3.setText("");o4.setText("");correct.setText("");

        } catch(Exception e){ e.printStackTrace(); }
    }
}