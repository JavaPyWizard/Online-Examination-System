package exam;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class LoginFrame extends JFrame {

    JTextField user;
    JPasswordField pass;

    LoginFrame() {

        setTitle("Login");
        setSize(350,250);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(UIStyle.bg);

        JLabel title = new JLabel("Online Exam System");
        title.setFont(UIStyle.titleFont);
        title.setBounds(60,10,250,30);

        JLabel l1 = new JLabel("Username:");
        JLabel l2 = new JLabel("Password:");

        l1.setBounds(30,60,100,25);
        l2.setBounds(30,100,100,25);

        user = new JTextField();
        pass = new JPasswordField();

        user.setBounds(130,60,150,25);
        pass.setBounds(130,100,150,25);

        JButton login = new JButton("Login");
        login.setBounds(100,150,120,35);
        login.setBackground(UIStyle.primary);
        login.setForeground(Color.WHITE);

        panel.add(title);
        panel.add(l1); panel.add(l2);
        panel.add(user); panel.add(pass);
        panel.add(login);

        add(panel);

        login.addActionListener(e -> loginUser());

        setVisible(true);
    }

    void loginUser() {
        try {
            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(
                    "SELECT * FROM users WHERE username=? AND password=?"
            );

            ps.setString(1,user.getText());
            ps.setString(2,new String(pass.getPassword()));

            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                if(rs.getString("role").equals("teacher")) {
                    new TeacherDashboard(rs.getInt("user_id"));
                } else {
                    new StudentDashboard(rs.getInt("user_id"));
                }
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,"Invalid Login");
            }

        } catch(Exception e){
            e.printStackTrace();
        }
    }
}