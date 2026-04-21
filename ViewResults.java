package exam;

import javax.swing.*;
import javax.swing.table.*;
import java.sql.*;
import java.awt.*;
import java.io.FileWriter;

public class ViewResults extends JFrame {

    JTable table;
    DefaultTableModel model;
    JTextField searchField;
    TableRowSorter<DefaultTableModel> sorter;

    ViewResults(int teacher_id) {

        setTitle("Leaderboard");
        setSize(650, 420);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(null);
        panel.setBackground(UIStyle.bg);

        JPanel card = new JPanel(null);
        card.setBounds(20, 20, 600, 340);
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(220,220,220)));

        JLabel title = new JLabel("Leaderboard", JLabel.CENTER);
        title.setFont(UIStyle.titleFont);
        title.setBounds(0, 10, 600, 30);

        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setBounds(80, 50, 60, 25);

        searchField = new JTextField();
        searchField.setBounds(140, 50, 200, 25);

        JButton exportBtn = new JButton("Export CSV");
        exportBtn.setBounds(370, 50, 120, 25);
        exportBtn.setBackground(UIStyle.primary);
        exportBtn.setForeground(Color.WHITE);

        String[] columns = {"Rank", "Quiz", "Student", "Score", "Percentage"};

        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);

        table.setRowHeight(25);
        table.setFont(UIStyle.normalFont);
        table.setBackground(Color.WHITE);

        table.setSelectionBackground(UIStyle.primary);
        table.setSelectionForeground(Color.WHITE);

        JTableHeader header = table.getTableHeader();
        header.setBackground(UIStyle.primary);
        header.setForeground(Color.WHITE);

        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(20, 90, 560, 220);

        card.add(title);
        card.add(searchLabel);
        card.add(searchField);
        card.add(exportBtn);
        card.add(scroll);

        panel.add(card);
        add(panel);

        load(teacher_id);

        searchField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent e) {
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchField.getText(), 1, 2));
            }
        });

        exportBtn.addActionListener(e -> exportToCSV());

        setVisible(true);
    }

    void load(int teacher_id) {

        try {
            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(
                "SELECT u.username, r.score, q.title, COUNT(qu.question_id) AS total_q " +
                "FROM results r " +
                "JOIN quizzes q ON r.quiz_id = q.quiz_id " +
                "JOIN users u ON r.student_id = u.user_id " +
                "JOIN questions qu ON q.quiz_id = qu.quiz_id " +
                "WHERE q.created_by = ? " +
                "GROUP BY u.username, r.score, q.title " +
                "ORDER BY r.score DESC"
            );

            ps.setInt(1, teacher_id);

            ResultSet rs = ps.executeQuery();

            int rank = 1;

            while (rs.next()) {

                int score = rs.getInt("score");
                int total = rs.getInt("total_q");

                int percent = (total != 0) ? (score * 100) / total : 0;

                model.addRow(new Object[]{
                        rank++,
                        rs.getString("title"),
                        rs.getString("username"),
                        score,
                        percent + "%"
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void exportToCSV() {

        try {
            FileWriter writer = new FileWriter("results.csv");

            for (int i = 0; i < model.getColumnCount(); i++) {
                writer.write(model.getColumnName(i) + ",");
            }
            writer.write("\n");

            for (int i = 0; i < model.getRowCount(); i++) {
                for (int j = 0; j < model.getColumnCount(); j++) {
                    writer.write(model.getValueAt(i, j).toString() + ",");
                }
                writer.write("\n");
            }

            writer.close();

            JOptionPane.showMessageDialog(this, "Exported as results.csv");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}