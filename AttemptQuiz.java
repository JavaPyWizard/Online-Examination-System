package exam;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.awt.*;

public class AttemptQuiz extends JFrame {

    int student_id, quiz_id, index = 0, score = 0;
    int timeLeft;

    ArrayList<String[]> qs = new ArrayList<>();

    JLabel questionLabel, counter, timerLabel;
    JRadioButton opt1, opt2, opt3, opt4;
    ButtonGroup group;
    JButton next;

    javax.swing.Timer timer;

    AttemptQuiz(int sid, int qid) {

        student_id = sid;
        quiz_id = qid;

        setTitle("Quiz Attempt");
        setSize(600,400);
        setLocationRelativeTo(null);
        setLayout(null);

        getContentPane().setBackground(UIStyle.bg);

        counter = new JLabel();
        timerLabel = new JLabel();
        questionLabel = new JLabel();

        opt1 = new JRadioButton();
        opt2 = new JRadioButton();
        opt3 = new JRadioButton();
        opt4 = new JRadioButton();

        group = new ButtonGroup();
        group.add(opt1);
        group.add(opt2);
        group.add(opt3);
        group.add(opt4);

        next = new JButton("Next");

        counter.setBounds(20,10,200,25);
        timerLabel.setBounds(450,10,120,25);

        counter.setFont(UIStyle.normalFont);
        timerLabel.setFont(UIStyle.normalFont);

        JPanel card = new JPanel(null);
        card.setBounds(30,50,520,220);
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        questionLabel.setFont(new Font("Segoe UI",Font.BOLD,16));

        opt1.setBackground(Color.WHITE);
        opt2.setBackground(Color.WHITE);
        opt3.setBackground(Color.WHITE);
        opt4.setBackground(Color.WHITE);

        opt1.setFont(UIStyle.normalFont);
        opt2.setFont(UIStyle.normalFont);
        opt3.setFont(UIStyle.normalFont);
        opt4.setFont(UIStyle.normalFont);

        questionLabel.setBounds(10,10,500,30);
        opt1.setBounds(10,50,500,25);
        opt2.setBounds(10,80,500,25);
        opt3.setBounds(10,110,500,25);
        opt4.setBounds(10,140,500,25);

        card.add(questionLabel);
        card.add(opt1);
        card.add(opt2);
        card.add(opt3);
        card.add(opt4);

        next.setBounds(230,300,120,35);
        next.setBackground(UIStyle.primary);
        next.setForeground(Color.WHITE);

        add(counter);
        add(timerLabel);
        add(card);
        add(next);

        loadTime();
        loadQ();
        startTimer();

        next.addActionListener(e -> checkAnswer());

        setVisible(true);
    }

    void loadTime() {
        try {
            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(
                "SELECT time_limit FROM quizzes WHERE quiz_id=?"
            );

            ps.setInt(1, quiz_id);
            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                timeLeft = rs.getInt("time_limit");
                timerLabel.setText("Time: " + timeLeft + "s");
            }

        } catch(Exception e){ e.printStackTrace(); }
    }

    void startTimer() {
        timer = new javax.swing.Timer(1000, e -> {
            timeLeft--;
            timerLabel.setText("Time: " + timeLeft + "s");

            if(timeLeft <= 0) {
                timer.stop();
                JOptionPane.showMessageDialog(this,"Time's Up!");
                finishQuiz();
            }
        });
        timer.start();
    }

    void loadQ() {
        try {
            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(
                "SELECT * FROM questions WHERE quiz_id=?"
            );

            ps.setInt(1, quiz_id);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                qs.add(new String[]{
                        rs.getString("question"),
                        rs.getString("option1"),
                        rs.getString("option2"),
                        rs.getString("option3"),
                        rs.getString("option4"),
                        String.valueOf(rs.getInt("correct_option"))
                });
            }

            showQuestion();

        } catch(Exception e){ e.printStackTrace(); }
    }

    void showQuestion() {
        if(index < qs.size()) {

            String[] q = qs.get(index);

            counter.setText("Question " + (index+1) + "/" + qs.size());
            questionLabel.setText(q[0]);

            opt1.setText("A. " + q[1]);
            opt2.setText("B. " + q[2]);
            opt3.setText("C. " + q[3]);
            opt4.setText("D. " + q[4]);

            group.clearSelection();

            if(index == qs.size()-1) next.setText("Submit");

        } else finishQuiz();
    }

    void checkAnswer() {
        int selected = 0;

        if(opt1.isSelected()) selected=1;
        else if(opt2.isSelected()) selected=2;
        else if(opt3.isSelected()) selected=3;
        else if(opt4.isSelected()) selected=4;

        int correct = Integer.parseInt(qs.get(index)[5]);

        if(selected == correct) score++;

        index++;
        showQuestion();
    }

    void finishQuiz() {
        try {
            if(timer != null) timer.stop();

            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO results(student_id,quiz_id,score) VALUES(?,?,?)"
            );

            ps.setInt(1, student_id);
            ps.setInt(2, quiz_id);
            ps.setInt(3, score);

            ps.executeUpdate();

            JOptionPane.showMessageDialog(this,
                    "Score: " + score + "/" + qs.size());

        } catch(Exception e){ e.printStackTrace(); }

        dispose();
    }
}