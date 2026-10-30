/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package gui;

import dao.DashboardDAO;
/**
 *
 * @author User
 */
public class MainForm extends javax.swing.JFrame {
    
    private model.User loggedInUser;
    
    private static final java.util.logging.Logger logger = 
            java.util.logging.Logger.getLogger(MainForm.class.getName());

   private javax.swing.JPanel contentPanel;

   private void addHoverEffect(javax.swing.JPanel card) {
    java.awt.Color originalColor = card.getBackground();
    java.awt.Color hoverColor = new java.awt.Color(220, 230, 245);

    card.addMouseListener(new java.awt.event.MouseAdapter() {
        @Override
        public void mouseEntered(java.awt.event.MouseEvent e) {
            card.setBackground(hoverColor);
        }

        @Override
        public void mouseExited(java.awt.event.MouseEvent e) {
            card.setBackground(originalColor);
        }
    });
}
   private void setActiveButton(javax.swing.JButton activeButton) {

    javax.swing.JButton[] buttons = {
        btnDashboard,
        btnProjects,
        btnTasks,
        btnTeam,
        btnReports,
        btnDatabase
    };

    for (javax.swing.JButton button : buttons) {
        button.setBackground(new java.awt.Color(23, 59, 108));
    }

    activeButton.setBackground(new java.awt.Color(45, 101, 163));
}
public MainForm() {

    initComponents();

    // =====================================
    // HOVER EFFECTS
    // =====================================
    addHoverEffect(cardProjects);
    addHoverEffect(cardTasks);
    addHoverEffect(cardCompleted1);
    addHoverEffect(cardPending);

    // =====================================
    // CARD CLICK EVENTS
    // =====================================
    cardProjects.addMouseListener(new java.awt.event.MouseAdapter() {
        @Override
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            showProjectForm();
        }
    });

    cardTasks.addMouseListener(new java.awt.event.MouseAdapter() {
        @Override
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            showTaskForm();
        }
    });

    cardCompleted1.addMouseListener(new java.awt.event.MouseAdapter() {
        @Override
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            showTaskForm();
        }
    });

    cardPending.addMouseListener(new java.awt.event.MouseAdapter() {
        @Override
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            showTaskForm();
        }
    });

    // =====================================
    // LOAD DASHBOARD STATISTICS
    // =====================================
    DashboardDAO dashboardDAO = new DashboardDAO();

    lblTotalProjects.setText(
            String.valueOf(dashboardDAO.getTotalProjects())
    );

    lblTotalTasks.setText(
            String.valueOf(dashboardDAO.getTotalTasks())
    );

    lblCompletedTasks.setText(
            String.valueOf(dashboardDAO.getCompletedTasks())
    );

    lblPendingTasks.setText(
            String.valueOf(dashboardDAO.getPendingTasks())
    );

    // =====================================
    // LOAD RECENT TASKS
    // =====================================
    javax.swing.table.DefaultTableModel model =
            (javax.swing.table.DefaultTableModel) tblRecentTasks.getModel();

    dashboardDAO.loadRecentTasks(model);

    // =====================================
    // LOAD UPCOMING DEADLINES
    // =====================================
    String deadlines = dashboardDAO.getUpcomingDeadlines()
            .replace("\n", "<br>");

    jLabel13.setText(
            "<html>" + deadlines + "</html>"
    );

    // =====================================
    // TABLE HEADER FONT
    // =====================================
    tblRecentTasks.getTableHeader().setFont(
            new java.awt.Font(
                    "Segoe UI",
                    java.awt.Font.BOLD,
                    13
            )
    );

    // =====================================
    // WINDOW SETTINGS
    // =====================================
    // Use a fixed window size close to the NetBeans Design view.
    // Do not maximize, because the dashboard uses AbsoluteLayout.
    setSize(1400, 900);
    setLocationRelativeTo(null);
    setResizable(false);

    // =====================================
    // MAIN LAYOUT
    // =====================================
    getContentPane().removeAll();

    getContentPane().setLayout(
            new java.awt.BorderLayout()
    );

    // =====================================
    // SIDEBAR
    // =====================================
    jPanel1.setPreferredSize(
            new java.awt.Dimension(250, 0)
    );

    // =====================================
    // CONTENT PANEL
    // =====================================
    contentPanel = new javax.swing.JPanel(
            new java.awt.BorderLayout()
    );

    contentPanel.setBackground(
            new java.awt.Color(245, 248, 252)
    );

    contentPanel.add(
            mainPanel,
            java.awt.BorderLayout.CENTER
    );

    // =====================================
    // ADD SIDEBAR
    // =====================================
    getContentPane().add(
            jPanel1,
            java.awt.BorderLayout.WEST
    );

    // =====================================
    // ADD CONTENT PANEL
    // =====================================
    getContentPane().add(
            contentPanel,
            java.awt.BorderLayout.CENTER
    );

    // =====================================
    // BUTTON CONNECTIONS
    // =====================================
   
    btnTeam.addActionListener(e -> {
        setActiveButton(btnTeam);
        showMemberForm();
    });

    btnReports.addActionListener(e -> {
        setActiveButton(btnReports);
        showReportsForm();
    });

    btnDatabase.addActionListener(e -> {
        setActiveButton(btnDatabase);
        showDataStructureForm();
    });

    btnDashboard6.addActionListener(e -> {
        logout();
    });

    // =====================================
    // SHOW DASHBOARD
    // =====================================
    showDashboard();

    setActiveButton(btnDashboard);

    revalidate();
    repaint();
}


// =====================================
// CONSTRUCTOR FOR LOGGED-IN USER
// =====================================
public MainForm(model.User user) {
    this();
    this.loggedInUser = user;

    lblWelcome.setText(
        "Welcome, " + user.getFullName() + " (" + user.getRole() + ")"
    );

    applyRolePermissions();
}

private void applyRolePermissions() {

    String role = loggedInUser.getRole();

    if (role.equalsIgnoreCase("Admin")) {

        // Admin has full access
        btnProjects.setVisible(true);
        btnTasks.setVisible(true);
        btnTeam.setVisible(true);
        btnReports.setVisible(true);
        btnDatabase.setVisible(true);

    } else if (role.equalsIgnoreCase("Teacher")) {

        // Teacher access
        btnProjects.setVisible(true);
        btnTasks.setVisible(true);
        btnTeam.setVisible(true);
        btnReports.setVisible(true);
        btnDatabase.setVisible(false);

    } else if (role.equalsIgnoreCase("Student")) {

        // Student access
        btnProjects.setVisible(true);
        btnTasks.setVisible(true);
        btnTeam.setVisible(true);
        btnReports.setVisible(false);
        btnDatabase.setVisible(false);
    }
}


// =====================================
// DASHBOARD
// =====================================
public void showDashboard() {

    contentPanel.removeAll();

    contentPanel.add(
            mainPanel,
            java.awt.BorderLayout.CENTER
    );

    refreshMainPanel();
}


// =====================================
// PROJECT MANAGEMENT
// =====================================
private void showProjectForm() {

    ProjectForm projectForm = new ProjectForm();

    contentPanel.removeAll();

    contentPanel.add(
            projectForm.getContentPane(),
            java.awt.BorderLayout.CENTER
    );

    refreshMainPanel();
}


// =====================================
// TASK MANAGEMENT
// =====================================
private void showTaskForm() {

    TaskForm taskForm = new TaskForm();

    contentPanel.removeAll();

    contentPanel.add(
            taskForm.getContentPane(),
            java.awt.BorderLayout.CENTER
    );

    refreshMainPanel();
}


// =====================================
// TEAM MANAGEMENT
// =====================================
private void showMemberForm() {

    MemberForm memberForm = new MemberForm();

    contentPanel.removeAll();

    contentPanel.add(
            memberForm.getContentPane(),
            java.awt.BorderLayout.CENTER
    );

    refreshMainPanel();
}


// =====================================
// REPORTS
// =====================================
private void showReportsForm() {

    ReportsForm reportsForm = new ReportsForm();

    contentPanel.removeAll();

    contentPanel.add(
            reportsForm.getContentPane(),
            java.awt.BorderLayout.CENTER
    );

    refreshMainPanel();
}


// =====================================
// DATA STRUCTURES
// =====================================
private void showDataStructureForm() {

    DataStructureForm dataForm =
            new DataStructureForm();

    contentPanel.removeAll();

    contentPanel.add(
            dataForm.getContentPane(),
            java.awt.BorderLayout.CENTER
    );

    refreshMainPanel();
}


// =====================================
// REFRESH MAIN PANEL
// =====================================
private void refreshMainPanel() {

    contentPanel.revalidate();
    contentPanel.repaint();

    getContentPane().revalidate();
    getContentPane().repaint();
}


// =====================================
// LOGOUT
// =====================================
private void logout() {

    int choice = javax.swing.JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to logout?",
            "Logout",
            javax.swing.JOptionPane.YES_NO_OPTION
    );

    if (choice == javax.swing.JOptionPane.YES_OPTION) {

        LoginForm loginForm = new LoginForm();

        loginForm.setLocationRelativeTo(null);
        loginForm.setVisible(true);

        this.dispose();
        
    }
}
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        mainPanel = new javax.swing.JPanel();
        lblDashboardTitle = new javax.swing.JLabel();
        lblWelcome = new javax.swing.JLabel();
        cardProjects = new javax.swing.JPanel();
        jLabelproject = new javax.swing.JLabel();
        lblTotalProjects = new javax.swing.JLabel();
        cardPending = new javax.swing.JPanel();
        lblPendingTitle = new javax.swing.JLabel();
        lblCompletedTasks = new javax.swing.JLabel();
        cardTasks = new javax.swing.JPanel();
        jLabeltasks = new javax.swing.JLabel();
        lblTotalTasks = new javax.swing.JLabel();
        cardCompleted1 = new javax.swing.JPanel();
        lblCompletedTitle = new javax.swing.JLabel();
        lblPendingTasks = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblRecentTasks = new javax.swing.JTable();
        jLabel12 = new javax.swing.JLabel();
        deadlinesPanel = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        pnlMyProjects = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        lblLogo = new javax.swing.JLabel();
        btnDashboard = new javax.swing.JButton();
        btnProjects = new javax.swing.JButton();
        btnTasks = new javax.swing.JButton();
        btnTeam = new javax.swing.JButton();
        btnReports = new javax.swing.JButton();
        btnDatabase = new javax.swing.JButton();
        btnDashboard6 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        mainPanel.setBackground(new java.awt.Color(245, 248, 252));
        mainPanel.setPreferredSize(new java.awt.Dimension(1000, 780));

        lblDashboardTitle.setFont(new java.awt.Font("Segoe UI", 1, 28)); // NOI18N
        lblDashboardTitle.setForeground(new java.awt.Color(23, 59, 108));
        lblDashboardTitle.setText("DASHBOARD");

        lblWelcome.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblWelcome.setForeground(new java.awt.Color(107, 114, 128));
        lblWelcome.setText("Welcome to TASKFLOW");

        cardProjects.setBackground(new java.awt.Color(239, 246, 255));
        cardProjects.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(210, 226, 247)));
        cardProjects.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        cardProjects.setPreferredSize(new java.awt.Dimension(180, 180));

        jLabelproject.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabelproject.setForeground(new java.awt.Color(23, 59, 108));
        jLabelproject.setText("PROJECTS");

        lblTotalProjects.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        lblTotalProjects.setForeground(new java.awt.Color(23, 59, 108));
        lblTotalProjects.setText("5");

        javax.swing.GroupLayout cardProjectsLayout = new javax.swing.GroupLayout(cardProjects);
        cardProjects.setLayout(cardProjectsLayout);
        cardProjectsLayout.setHorizontalGroup(
            cardProjectsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardProjectsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(cardProjectsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelproject)
                    .addComponent(lblTotalProjects))
                .addContainerGap(115, Short.MAX_VALUE))
        );
        cardProjectsLayout.setVerticalGroup(
            cardProjectsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardProjectsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelproject)
                .addGap(18, 18, 18)
                .addComponent(lblTotalProjects)
                .addContainerGap(97, Short.MAX_VALUE))
        );

        cardPending.setBackground(new java.awt.Color(239, 246, 255));
        cardPending.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(210, 226, 247)));
        cardPending.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        cardPending.setPreferredSize(new java.awt.Dimension(180, 180));

        lblPendingTitle.setBackground(new java.awt.Color(23, 59, 108));
        lblPendingTitle.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblPendingTitle.setForeground(new java.awt.Color(23, 59, 108));
        lblPendingTitle.setText("PENDING TASKS");

        lblCompletedTasks.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        lblCompletedTasks.setForeground(new java.awt.Color(23, 59, 108));
        lblCompletedTasks.setText("5");

        javax.swing.GroupLayout cardPendingLayout = new javax.swing.GroupLayout(cardPending);
        cardPending.setLayout(cardPendingLayout);
        cardPendingLayout.setHorizontalGroup(
            cardPendingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardPendingLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(cardPendingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblPendingTitle)
                    .addComponent(lblCompletedTasks))
                .addContainerGap(78, Short.MAX_VALUE))
        );
        cardPendingLayout.setVerticalGroup(
            cardPendingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardPendingLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblPendingTitle)
                .addGap(18, 18, 18)
                .addComponent(lblCompletedTasks)
                .addContainerGap(97, Short.MAX_VALUE))
        );

        cardTasks.setBackground(new java.awt.Color(239, 246, 255));
        cardTasks.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(210, 226, 247)));
        cardTasks.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        cardTasks.setPreferredSize(new java.awt.Dimension(180, 180));

        jLabeltasks.setBackground(new java.awt.Color(23, 59, 108));
        jLabeltasks.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabeltasks.setForeground(new java.awt.Color(23, 59, 108));
        jLabeltasks.setText("TASKS");

        lblTotalTasks.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        lblTotalTasks.setForeground(new java.awt.Color(23, 59, 108));
        lblTotalTasks.setText("5");

        javax.swing.GroupLayout cardTasksLayout = new javax.swing.GroupLayout(cardTasks);
        cardTasks.setLayout(cardTasksLayout);
        cardTasksLayout.setHorizontalGroup(
            cardTasksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardTasksLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(cardTasksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabeltasks)
                    .addComponent(lblTotalTasks))
                .addContainerGap(135, Short.MAX_VALUE))
        );
        cardTasksLayout.setVerticalGroup(
            cardTasksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardTasksLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabeltasks)
                .addGap(18, 18, 18)
                .addComponent(lblTotalTasks)
                .addContainerGap(97, Short.MAX_VALUE))
        );

        cardCompleted1.setBackground(new java.awt.Color(239, 246, 255));
        cardCompleted1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(210, 226, 247)));
        cardCompleted1.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        cardCompleted1.setPreferredSize(new java.awt.Dimension(180, 180));

        lblCompletedTitle.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblCompletedTitle.setForeground(new java.awt.Color(23, 59, 108));
        lblCompletedTitle.setText("COMPLETED TASKS");

        lblPendingTasks.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        lblPendingTasks.setForeground(new java.awt.Color(23, 59, 108));
        lblPendingTasks.setText("5");

        javax.swing.GroupLayout cardCompleted1Layout = new javax.swing.GroupLayout(cardCompleted1);
        cardCompleted1.setLayout(cardCompleted1Layout);
        cardCompleted1Layout.setHorizontalGroup(
            cardCompleted1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardCompleted1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(cardCompleted1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblCompletedTitle)
                    .addComponent(lblPendingTasks))
                .addContainerGap(62, Short.MAX_VALUE))
        );
        cardCompleted1Layout.setVerticalGroup(
            cardCompleted1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardCompleted1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblCompletedTitle)
                .addGap(18, 18, 18)
                .addComponent(lblPendingTasks)
                .addContainerGap(97, Short.MAX_VALUE))
        );

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(23, 59, 108));
        jLabel10.setText("Recent Tasks");

        tblRecentTasks.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        tblRecentTasks.setForeground(new java.awt.Color(23, 59, 108));
        tblRecentTasks.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Task Name", "Project", "Priority", "Deadline", "Status"
            }
        ));
        tblRecentTasks.setPreferredSize(new java.awt.Dimension(460, 250));
        tblRecentTasks.setRowHeight(30);
        jScrollPane1.setViewportView(tblRecentTasks);

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(23, 59, 108));
        jLabel12.setText("Upcoming Deadlines");

        deadlinesPanel.setBackground(new java.awt.Color(255, 255, 255));
        deadlinesPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(225, 231, 239)));

        jLabel13.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(107, 114, 128));
        jLabel13.setText("No upcoming deadlines");

        javax.swing.GroupLayout deadlinesPanelLayout = new javax.swing.GroupLayout(deadlinesPanel);
        deadlinesPanel.setLayout(deadlinesPanelLayout);
        deadlinesPanelLayout.setHorizontalGroup(
            deadlinesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(deadlinesPanelLayout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(jLabel13)
                .addContainerGap(63, Short.MAX_VALUE))
        );
        deadlinesPanelLayout.setVerticalGroup(
            deadlinesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(deadlinesPanelLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel13)
                .addContainerGap(385, Short.MAX_VALUE))
        );

        pnlMyProjects.setPreferredSize(new java.awt.Dimension(460, 250));

        javax.swing.GroupLayout pnlMyProjectsLayout = new javax.swing.GroupLayout(pnlMyProjects);
        pnlMyProjects.setLayout(pnlMyProjectsLayout);
        pnlMyProjectsLayout.setHorizontalGroup(
            pnlMyProjectsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 378, Short.MAX_VALUE)
        );
        pnlMyProjectsLayout.setVerticalGroup(
            pnlMyProjectsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 250, Short.MAX_VALUE)
        );

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(23, 59, 108));
        jLabel11.setText("My Projects");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 770, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 200, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(lblDashboardTitle))
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(lblWelcome))
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(pnlMyProjects, javax.swing.GroupLayout.PREFERRED_SIZE, 378, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(13, 13, 13)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 372, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addComponent(cardProjects, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(cardTasks, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(13, 13, 13)
                        .addComponent(cardCompleted1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(cardPending, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addGap(281, 281, 281)
                        .addComponent(jLabel10)))
                .addGap(8, 8, 8)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12)
                    .addComponent(deadlinesPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(lblDashboardTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(lblWelcome)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel12)
                .addGap(6, 6, 6)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 65, Short.MAX_VALUE)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cardProjects, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cardTasks, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cardCompleted1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cardPending, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(6, 6, 6)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11)
                            .addComponent(jLabel10)))
                    .addComponent(deadlinesPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlMyProjects, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jPanel1.setBackground(new java.awt.Color(11, 45, 92));
        jPanel1.setName(""); // NOI18N
        jPanel1.setPreferredSize(new java.awt.Dimension(290, 780));

        lblLogo.setFont(new java.awt.Font("Segoe UI", 1, 28)); // NOI18N
        lblLogo.setForeground(new java.awt.Color(255, 255, 255));
        lblLogo.setText("TASKFLOW");

        btnDashboard.setBackground(new java.awt.Color(11, 45, 92));
        btnDashboard.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnDashboard.setForeground(new java.awt.Color(255, 255, 255));
        btnDashboard.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/white home.png"))); // NOI18N
        btnDashboard.setText("DASHBOARD");
        btnDashboard.setBorderPainted(false);
        btnDashboard.setContentAreaFilled(false);
        btnDashboard.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnDashboard.setFocusPainted(false);
        btnDashboard.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnDashboard.setMargin(new java.awt.Insets(2, 0, 3, 14));
        btnDashboard.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnDashboardMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnDashboardMouseExited(evt);
            }
        });
        btnDashboard.addActionListener(this::btnDashboardActionPerformed);

        btnProjects.setBackground(new java.awt.Color(11, 45, 92));
        btnProjects.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnProjects.setForeground(new java.awt.Color(255, 255, 255));
        btnProjects.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/white folder 1.png"))); // NOI18N
        btnProjects.setText("PROJECTS");
        btnProjects.setBorder(null);
        btnProjects.setBorderPainted(false);
        btnProjects.setContentAreaFilled(false);
        btnProjects.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnProjects.setFocusPainted(false);
        btnProjects.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnProjects.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnProjectsMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnProjectsMouseExited(evt);
            }
        });
        btnProjects.addActionListener(this::btnProjectsActionPerformed);

        btnTasks.setBackground(new java.awt.Color(11, 45, 92));
        btnTasks.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnTasks.setForeground(new java.awt.Color(255, 255, 255));
        btnTasks.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/white checklist.png"))); // NOI18N
        btnTasks.setText("TASKS");
        btnTasks.setBorder(null);
        btnTasks.setBorderPainted(false);
        btnTasks.setContentAreaFilled(false);
        btnTasks.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnTasks.setFocusPainted(false);
        btnTasks.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnTasks.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnTasksMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnTasksMouseExited(evt);
            }
        });
        btnTasks.addActionListener(this::btnTasksActionPerformed);

        btnTeam.setBackground(new java.awt.Color(11, 45, 92));
        btnTeam.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnTeam.setForeground(new java.awt.Color(255, 255, 255));
        btnTeam.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/white users.png"))); // NOI18N
        btnTeam.setText("TEAM");
        btnTeam.setBorder(null);
        btnTeam.setBorderPainted(false);
        btnTeam.setContentAreaFilled(false);
        btnTeam.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnTeam.setFocusPainted(false);
        btnTeam.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnTeam.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnTeamMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnTeamMouseExited(evt);
            }
        });
        btnTeam.addActionListener(this::btnTeamActionPerformed);

        btnReports.setBackground(new java.awt.Color(11, 45, 92));
        btnReports.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnReports.setForeground(new java.awt.Color(255, 255, 255));
        btnReports.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/white report.png"))); // NOI18N
        btnReports.setText("REPORTS");
        btnReports.setBorder(null);
        btnReports.setBorderPainted(false);
        btnReports.setContentAreaFilled(false);
        btnReports.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnReports.setFocusPainted(false);
        btnReports.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnReports.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnReportsMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnReportsMouseExited(evt);
            }
        });

        btnDatabase.setBackground(new java.awt.Color(11, 45, 92));
        btnDatabase.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnDatabase.setForeground(new java.awt.Color(255, 255, 255));
        btnDatabase.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/white db.png"))); // NOI18N
        btnDatabase.setText("DATA STRUCTURES");
        btnDatabase.setBorder(null);
        btnDatabase.setBorderPainted(false);
        btnDatabase.setContentAreaFilled(false);
        btnDatabase.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnDatabase.setFocusPainted(false);
        btnDatabase.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnDatabase.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnDatabaseMouseEntered(evt);
            }
        });

        btnDashboard6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnDashboard6.setText("LOGOUT");

        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Plan • Organize • Achieve");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(57, 57, 57)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnTeam, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(btnProjects, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnTasks, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnDashboard))
                            .addComponent(btnReports, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnDatabase)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnDashboard6)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 79, Short.MAX_VALUE)
                .addComponent(lblLogo)
                .addGap(61, 61, 61))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(lblLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(96, 96, 96)
                .addComponent(btnDashboard)
                .addGap(18, 18, 18)
                .addComponent(btnProjects)
                .addGap(23, 23, 23)
                .addComponent(btnTasks, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnTeam, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnReports, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnDatabase, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnDashboard6)
                .addGap(97, 97, 97)
                .addComponent(jLabel1)
                .addGap(88, 88, 88))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 1014, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 828, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 828, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnProjectsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProjectsActionPerformed
      setActiveButton(btnProjects);
    showProjectForm();
    }//GEN-LAST:event_btnProjectsActionPerformed

    private void btnDashboardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDashboardActionPerformed
         setActiveButton(btnDashboard);
    showDashboard();
    }//GEN-LAST:event_btnDashboardActionPerformed

    private void btnTasksActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTasksActionPerformed
        setActiveButton(btnTasks);
    showTaskForm();
    }//GEN-LAST:event_btnTasksActionPerformed

    private void btnTeamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTeamActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnTeamActionPerformed

    private void btnDashboardMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDashboardMouseExited
      btnProjects.setBackground(new java.awt.Color(11, 45, 92));
    }//GEN-LAST:event_btnDashboardMouseExited

    private void btnDashboardMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDashboardMouseEntered
    btnProjects.setBackground(new java.awt.Color(45, 101, 163));
    }//GEN-LAST:event_btnDashboardMouseEntered

    private void btnProjectsMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnProjectsMouseEntered
    btnProjects.setBackground(new java.awt.Color(45, 101, 163));
    }//GEN-LAST:event_btnProjectsMouseEntered

    private void btnProjectsMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnProjectsMouseExited
    btnProjects.setBackground(new java.awt.Color(11, 45, 92));
    }//GEN-LAST:event_btnProjectsMouseExited

    private void btnTasksMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTasksMouseEntered
    btnProjects.setBackground(new java.awt.Color(45, 101, 163));
    }//GEN-LAST:event_btnTasksMouseEntered

    private void btnTasksMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTasksMouseExited
    btnProjects.setBackground(new java.awt.Color(11, 45, 92));
    }//GEN-LAST:event_btnTasksMouseExited

    private void btnTeamMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTeamMouseEntered
    btnProjects.setBackground(new java.awt.Color(45, 101, 163));
    }//GEN-LAST:event_btnTeamMouseEntered

    private void btnTeamMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTeamMouseExited
    btnProjects.setBackground(new java.awt.Color(11, 45, 92));
    }//GEN-LAST:event_btnTeamMouseExited

    private void btnReportsMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnReportsMouseEntered
    btnProjects.setBackground(new java.awt.Color(45, 101, 163));
    }//GEN-LAST:event_btnReportsMouseEntered

    private void btnReportsMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnReportsMouseExited
    btnProjects.setBackground(new java.awt.Color(11, 45, 92));
    }//GEN-LAST:event_btnReportsMouseExited

    private void btnDatabaseMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDatabaseMouseEntered
    btnProjects.setBackground(new java.awt.Color(45, 101, 163));
    }//GEN-LAST:event_btnDatabaseMouseEntered

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new MainForm().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDashboard;
    private javax.swing.JButton btnDashboard6;
    private javax.swing.JButton btnDatabase;
    private javax.swing.JButton btnProjects;
    private javax.swing.JButton btnReports;
    private javax.swing.JButton btnTasks;
    private javax.swing.JButton btnTeam;
    private javax.swing.JPanel cardCompleted1;
    private javax.swing.JPanel cardPending;
    private javax.swing.JPanel cardProjects;
    private javax.swing.JPanel cardTasks;
    private javax.swing.JPanel deadlinesPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabelproject;
    private javax.swing.JLabel jLabeltasks;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblCompletedTasks;
    private javax.swing.JLabel lblCompletedTitle;
    private javax.swing.JLabel lblDashboardTitle;
    private javax.swing.JLabel lblLogo;
    private javax.swing.JLabel lblPendingTasks;
    private javax.swing.JLabel lblPendingTitle;
    private javax.swing.JLabel lblTotalProjects;
    private javax.swing.JLabel lblTotalTasks;
    private javax.swing.JLabel lblWelcome;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JPanel pnlMyProjects;
    private javax.swing.JTable tblRecentTasks;
    // End of variables declaration//GEN-END:variables
}
