/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */

package gui;

import database.DBConnection;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

public class ReportsPanel extends JPanel {

    private JLabel lblTotalProjects;
    private JLabel lblTotalTasks;
    private JLabel lblCompletedTasks;
    private JLabel lblPendingTasks;
    private JLabel lblOverdueTasks;

    private JTable tblReports;
    private DefaultTableModel tableModel;

    private JButton btnRefresh;
    private JButton btnExport;

    private final Color NAVY = new Color(23, 59, 108);
    private final Color BACKGROUND = new Color(245, 248, 252);
    private final Color GREEN = new Color(24, 130, 70);
    private final Color ORANGE = new Color(220, 125, 25);
    private final Color PURPLE = new Color(115, 75, 180);

    public ReportsPanel() {
        buildReportsUI();
        loadReports();
    }

    // Build the interface programmatically
    private void buildReportsUI() {

        setLayout(new BorderLayout(15, 15));
        setBackground(BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        JLabel lblTitle = new JLabel("REPORTS AND ANALYTICS");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(NAVY);

        JLabel lblSubtitle = new JLabel(
                "Monitor project progress and task performance."
        );
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSubtitle.setForeground(Color.DARK_GRAY);

        JPanel titlePanel = new JPanel(new GridLayout(2, 1, 0, 5));
        titlePanel.setOpaque(false);
        titlePanel.add(lblTitle);
        titlePanel.add(lblSubtitle);

        // Summary cards
        JPanel cardsPanel = new JPanel(new GridLayout(1, 5, 12, 0));
        cardsPanel.setOpaque(false);

        lblTotalProjects = new JLabel("0");
        lblTotalTasks = new JLabel("0");
        lblCompletedTasks = new JLabel("0");
        lblPendingTasks = new JLabel("0");
        lblOverdueTasks = new JLabel("0");

        cardsPanel.add(createCard(
                "TOTAL PROJECTS", lblTotalProjects, NAVY
        ));
        cardsPanel.add(createCard(
                "TOTAL TASKS", lblTotalTasks, PURPLE
        ));
        cardsPanel.add(createCard(
                "COMPLETED", lblCompletedTasks, GREEN
        ));
        cardsPanel.add(createCard(
                "PENDING", lblPendingTasks, ORANGE
        ));
        cardsPanel.add(createCard(
                "OVERDUE", lblOverdueTasks, Color.RED
        ));

        // Header section
        JPanel topPanel = new JPanel(new BorderLayout(0, 18));
        topPanel.setOpaque(false);
        topPanel.add(titlePanel, BorderLayout.NORTH);
        topPanel.add(cardsPanel, BorderLayout.CENTER);

        // Buttons
        btnRefresh = new JButton("REFRESH REPORTS");
        btnExport = new JButton("EXPORT CSV");

        styleButton(btnRefresh);
        styleButton(btnExport);

        JPanel buttonPanel = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 10, 5)
        );
        buttonPanel.setOpaque(false);
        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnExport);

        // Report table
        tableModel = new DefaultTableModel(
                new String[]{
                    "Report Category",
                    "Description",
                    "Count",
                    "Status"
                }, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblReports = new JTable(tableModel);
        tblReports.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tblReports.setRowHeight(28);
        tblReports.setAutoResizeMode(
                JTable.AUTO_RESIZE_ALL_COLUMNS
        );
        tblReports.setFillsViewportHeight(true);

        tblReports.getTableHeader().setFont(
                new Font("Segoe UI", Font.BOLD, 13)
        );

        JScrollPane scrollPane = new JScrollPane(tblReports);
        scrollPane.setBorder(
                BorderFactory.createLineBorder(
                        new Color(220, 226, 235)
                )
        );

        JPanel centerPanel = new JPanel(new BorderLayout(0, 10));
        centerPanel.setOpaque(false);
        centerPanel.add(buttonPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);

        // Button actions
        btnRefresh.addActionListener(e -> loadReports());
        btnExport.addActionListener(e -> exportCSV());
    }

    // Create summary cards
    private JPanel createCard(
            String title,
            JLabel valueLabel,
            Color accentColor) {

        JPanel card = new JPanel(new BorderLayout(5, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        new Color(225, 230, 238)
                ),
                BorderFactory.createEmptyBorder(15, 12, 15, 12)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        titleLabel.setForeground(Color.DARK_GRAY);

        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        valueLabel.setForeground(accentColor);
        valueLabel.setHorizontalAlignment(SwingConstants.LEFT);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    // Style buttons
    private void styleButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setForeground(Color.WHITE);
        button.setBackground(NAVY);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(
                new java.awt.Cursor(
                        java.awt.Cursor.HAND_CURSOR
                )
        );
        button.setPreferredSize(new Dimension(170, 36));
    }

    // Load report data from MySQL
    private void loadReports() {

        tableModel.setRowCount(0);

        try (Connection connection = DBConnection.getConnection();
             Statement statement = connection.createStatement()) {

            // Count projects
            int totalProjects = getCount(
                    statement,
                    "SELECT COUNT(*) FROM projects"
            );

            // Count tasks
            int totalTasks = getCount(
                    statement,
                    "SELECT COUNT(*) FROM tasks"
            );

            // Completed tasks
            int completedTasks = getCount(
                    statement,
                    "SELECT COUNT(*) FROM tasks "
                    + "WHERE LOWER(task_status) = 'completed'"
            );

            // Pending tasks
            int pendingTasks = getCount(
                    statement,
                    "SELECT COUNT(*) FROM tasks "
                    + "WHERE LOWER(task_status) IN "
                    + "('pending', 'not started', 'todo', 'to do')"
            );

            // Overdue tasks that are not completed
            int overdueTasks = getCount(
                    statement,
                    "SELECT COUNT(*) FROM tasks "
                    + "WHERE deadline < CURDATE() "
                    + "AND LOWER(task_status) <> 'completed'"
                    
            );
int inProgressTasks = getCount(statement,
    "SELECT COUNT(*) FROM tasks WHERE LOWER(task_status) = 'in progress'");

            // Update summary cards
            lblTotalProjects.setText(
                    String.valueOf(totalProjects)
            );
            lblTotalTasks.setText(
                    String.valueOf(totalTasks)
            );
            lblCompletedTasks.setText(
                    String.valueOf(completedTasks)
            );
            lblPendingTasks.setText(
                    String.valueOf(pendingTasks)
            );
            lblOverdueTasks.setText(
                    String.valueOf(overdueTasks)
            );

            // Add report rows
            addReport(
                    "Projects",
                    "All registered projects",
                    totalProjects,
                    "Overall"
            );

            addReport(
                    "Tasks",
                    "All registered tasks",
                    totalTasks,
                    "Overall"
            );

            addReport(
                    "Tasks Performance",
                    "Tasks marked completed",
                    completedTasks,
                    "Completed"
            );

            addReport(
                    "Tasks Performance",
                    "Tasks not yet started or pending",
                    pendingTasks,
                    "Pending"
            );

            addReport(
                    "Deadlines",
                    "Past-deadline tasks not completed",
                    overdueTasks,
                    "Overdue"
            );

            addReport(
        "Tasks Performance",
        "Tasks in progress",
        inProgressTasks,
        "In Progress"
);

            addReport(
                    "Projects",
                    "Projects marked completed",
                    getCount(
                            statement,
                            "SELECT COUNT(*) FROM projects "
                            + "WHERE LOWER(project_status) = 'completed'"
                    ),
                    "Completed"
            );

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to load reports.\n\n"
                    + "Check your database connection and "
                    + "table/column names.\n\n"
                    + e.getMessage(),
                    "Reports Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // Run a count query
    private int getCount(
            Statement statement,
            String sql) throws SQLException {

        try (ResultSet rs = statement.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }

        return 0;
    }

    // Add a row to the report table
    private void addReport(
            String category,
            String description,
            int count,
            String status) {

        tableModel.addRow(new Object[]{
            category,
            description,
            count,
            status
        });
    }

    // Export reports to CSV
    private void exportCSV() {

        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "There are no reports to export.",
                    "No Data",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Save TASKFLOW Report");
        chooser.setSelectedFile(
                new File("TASKFLOW_Reports.csv")
        );

        if (chooser.showSaveDialog(this)
                != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File file = chooser.getSelectedFile();

        if (!file.getName().toLowerCase().endsWith(".csv")) {
            file = new File(file.getAbsolutePath() + ".csv");
        }

        try (FileWriter writer = new FileWriter(file)) {

            // Column headings
            for (int col = 0; col < tableModel.getColumnCount(); col++) {
                if (col > 0) {
                    writer.append(',');
                }

                writer.append(csvEscape(
                        tableModel.getColumnName(col)
                ));
            }

            writer.append("\n");

            // Report data
            for (int row = 0; row < tableModel.getRowCount(); row++) {
                for (int col = 0;
                        col < tableModel.getColumnCount();
                        col++) {

                    if (col > 0) {
                        writer.append(',');
                    }

                    Object value = tableModel.getValueAt(row, col);

                    writer.append(csvEscape(
                            value == null ? "" : value.toString()
                    ));
                }

                writer.append("\n");
            }

            JOptionPane.showMessageDialog(
                    this,
                    "Report exported successfully!\n"
                    + file.getAbsolutePath(),
                    "Export Complete",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (IOException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to export report:\n" + e.getMessage(),
                    "Export Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // Escape CSV values correctly
    private String csvEscape(String value) {

        String escaped = value.replace("\"", "\"\"");

        if (escaped.contains(",")
                || escaped.contains("\"")
                || escaped.contains("\n")
                || escaped.contains("\r")) {

            return "\"" + escaped + "\"";
        }

        return escaped;
    }


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        contentPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();

        contentPanel4.setBackground(new java.awt.Color(245, 248, 252));
        contentPanel4.setPreferredSize(new java.awt.Dimension(1000, 780));

        jLabel1.setText("Date Range");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jButton1.setText("Generate Report");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 245, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 169, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 245, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 245, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 169, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 245, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 169, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 508, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 508, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 303, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout contentPanel4Layout = new javax.swing.GroupLayout(contentPanel4);
        contentPanel4.setLayout(contentPanel4Layout);
        contentPanel4Layout.setHorizontalGroup(
            contentPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contentPanel4Layout.createSequentialGroup()
                .addGroup(contentPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(contentPanel4Layout.createSequentialGroup()
                        .addContainerGap(1167, Short.MAX_VALUE)
                        .addComponent(jButton1))
                    .addGroup(contentPanel4Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addGroup(contentPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(contentPanel4Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(151, 151, 151))
                            .addGroup(contentPanel4Layout.createSequentialGroup()
                                .addGroup(contentPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1)
                                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 302, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(contentPanel4Layout.createSequentialGroup()
                                .addGroup(contentPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(contentPanel4Layout.createSequentialGroup()
                                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(contentPanel4Layout.createSequentialGroup()
                                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(18, 18, 18)
                                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        contentPanel4Layout.setVerticalGroup(
            contentPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contentPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(contentPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(contentPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(contentPanel4Layout.createSequentialGroup()
                        .addGroup(contentPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(contentPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(contentPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(contentPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 1290, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(contentPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, 752, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel contentPanel4;
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    // End of variables declaration//GEN-END:variables

}
