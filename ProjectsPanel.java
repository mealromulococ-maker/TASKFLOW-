/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */

package gui;

import dao.ProjectDAO;
import model.Project;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ProjectsPanel extends JPanel {

    private final Color NAVY = new Color(23, 59, 108);
    private final Color BLUE = new Color(45, 101, 163);
    private final Color BACKGROUND = new Color(245, 248, 252);
    private final Color BORDER = new Color(225, 230, 238);

    private final ProjectDAO projectDAO = new ProjectDAO();

    private JTable tblProjects;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;

    private JTextField txtSearch;
    private JComboBox<String> cmbStatus;
    private JLabel lblTotalProjects;
    private JLabel lblMessage;

    // Change this to the ID of the logged-in user if needed.
    private final int currentUserId = 1;

    public ProjectsPanel() {
        setLayout(new BorderLayout(18, 18));
        setBackground(BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        createInterface();
        loadProjects();
    }

    private void createInterface() {
        // HEADER
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);

        JLabel title = new JLabel("Projects");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(NAVY);

        JLabel subtitle = new JLabel(
                "Create, organize, and manage your projects");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(Color.GRAY);

        titlePanel.add(title);
        titlePanel.add(Box.createVerticalStrut(5));
        titlePanel.add(subtitle);

        JPanel summary = new JPanel();
        summary.setLayout(new BoxLayout(summary, BoxLayout.Y_AXIS));
        summary.setBackground(Color.WHITE);
        summary.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(10, 16, 10, 16)));

        JLabel summaryTitle = new JLabel("TOTAL PROJECTS");
        summaryTitle.setFont(new Font("Segoe UI", Font.BOLD, 10));
        summaryTitle.setForeground(Color.GRAY);

        lblTotalProjects = new JLabel("0");
        lblTotalProjects.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTotalProjects.setForeground(BLUE);

        summary.add(summaryTitle);
        summary.add(lblTotalProjects);

        header.add(titlePanel, BorderLayout.CENTER);
        header.add(summary, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        // MAIN CONTENT
        JPanel main = new JPanel(new BorderLayout(12, 12));
        main.setBackground(Color.WHITE);
        main.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)));

        // TOOLBAR
        JPanel toolbar = new JPanel(new BorderLayout(10, 10));
        toolbar.setOpaque(false);

        JPanel searchPanel = new JPanel(new BorderLayout(8, 0));
        searchPanel.setOpaque(false);

        txtSearch = new JTextField();
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtSearch.setPreferredSize(new Dimension(230, 38));
        txtSearch.setToolTipText("Search project name or description");

        cmbStatus = new JComboBox<>(new String[]{
            "All Statuses",
            "Not Started",
            "In Progress",
            "Completed"
        });
        cmbStatus.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cmbStatus.setPreferredSize(new Dimension(145, 38));

        searchPanel.add(txtSearch, BorderLayout.CENTER);
        searchPanel.add(cmbStatus, BorderLayout.EAST);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        buttons.setOpaque(false);

        JButton btnAdd = createButton("+ Add Project", BLUE);
        JButton btnEdit = createButton("Edit", new Color(70, 120, 170));
        JButton btnDelete = createButton("Delete", new Color(190, 55, 55));
        JButton btnClear = createButton("Clear", new Color(100, 110, 125));

        buttons.add(btnAdd);
        buttons.add(btnEdit);
        buttons.add(btnDelete);
        buttons.add(btnClear);

        toolbar.add(searchPanel, BorderLayout.CENTER);
        toolbar.add(buttons, BorderLayout.EAST);

        // TABLE
        tableModel = new DefaultTableModel(
                new String[]{
                    "ID", "Project Name", "Description",
                    "Start Date", "Deadline", "Status"
                }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblProjects = new JTable(tableModel);
        tblProjects.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tblProjects.setRowHeight(34);
        tblProjects.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION);
        tblProjects.setGridColor(BORDER);
        tblProjects.setSelectionBackground(new Color(220, 233, 250));
        tblProjects.setSelectionForeground(Color.BLACK);
        tblProjects.setFillsViewportHeight(true);

        tblProjects.getTableHeader().setFont(
                new Font("Segoe UI", Font.BOLD, 12));
        tblProjects.getTableHeader().setBackground(
                new Color(235, 241, 249));
        tblProjects.getTableHeader().setForeground(NAVY);
        tblProjects.getTableHeader().setReorderingAllowed(false);

        // Hide the ID column while keeping IDs available internally.
        tblProjects.getColumnModel().getColumn(0).setMinWidth(0);
        tblProjects.getColumnModel().getColumn(0).setMaxWidth(0);
        tblProjects.getColumnModel().getColumn(0).setWidth(0);

        sorter = new TableRowSorter<>(tableModel);
        tblProjects.setRowSorter(sorter);

        JScrollPane scrollPane = new JScrollPane(tblProjects);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER));

        lblMessage = new JLabel("Ready");
        lblMessage.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblMessage.setForeground(Color.GRAY);

        main.add(toolbar, BorderLayout.NORTH);
        main.add(scrollPane, BorderLayout.CENTER);
        main.add(lblMessage, BorderLayout.SOUTH);

        add(main, BorderLayout.CENTER);

        // BUTTON ACTIONS
        btnAdd.addActionListener(e -> showProjectDialog(-1));
        btnEdit.addActionListener(e -> editSelectedProject());
        btnDelete.addActionListener(e -> deleteSelectedProject());

        btnClear.addActionListener(e -> {
            txtSearch.setText("");
            cmbStatus.setSelectedIndex(0);
            tblProjects.clearSelection();
            lblMessage.setText("Search and filters cleared.");
        });

        // SEARCH
        txtSearch.getDocument().addDocumentListener(
                new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                applyFilters();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                applyFilters();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                applyFilters();
            }
        });

        cmbStatus.addActionListener(e -> applyFilters());

        // Double-click a row to edit the project.
        tblProjects.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2
                        && tblProjects.getSelectedRow() >= 0) {
                    editSelectedProject();
                }
            }
        });
    }

    private JButton createButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    // LOAD PROJECTS FROM MYSQL
    private void loadProjects() {
        try {
            tableModel.setRowCount(0);

            List<Project> projects = projectDAO.getAllProjects();

            for (Project project : projects) {
                tableModel.addRow(new Object[]{
                    project.getProjectId(),
                    project.getProjectName(),
                    project.getDescription(),
                    project.getStartDate() == null
                            ? "" : project.getStartDate().toString(),
                    project.getDeadline() == null
                            ? "" : project.getDeadline().toString(),
                    project.getStatus()
                });
            }

            lblTotalProjects.setText(
                    String.valueOf(projects.size()));

            applyFilters();
            lblMessage.setText(
                    projects.size() + " project(s) loaded.");

        } catch (Exception ex) {
            lblMessage.setText("Could not load projects.");
            JOptionPane.showMessageDialog(this,
                    "Could not load projects from MySQL.\n"
                    + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // SEARCH AND STATUS FILTER
    
private void applyFilters() {
    String searchText = txtSearch.getText().trim();
    String selectedStatus = cmbStatus.getSelectedItem().toString();

    RowFilter<DefaultTableModel, Object> filter =
            new RowFilter<DefaultTableModel, Object>() {
        @Override
        public boolean include(
                Entry<? extends DefaultTableModel, ? extends Object> entry) {

            String projectName = entry.getStringValue(1);
            String description = entry.getStringValue(2);
            String status = entry.getStringValue(5);

            boolean matchesSearch =
                    projectName.toLowerCase().contains(searchText.toLowerCase())
                    || description.toLowerCase().contains(searchText.toLowerCase());

            boolean matchesStatus =
                    selectedStatus.equals("All Statuses")
                    || status.equalsIgnoreCase(selectedStatus);

            return matchesSearch && matchesStatus;
        }
    };

    sorter.setRowFilter(filter);
}

    // ADD OR EDIT PROJECT
    private void showProjectDialog(int modelRow) {
        boolean editing = modelRow >= 0;

        JTextField txtName = new JTextField();
        JTextField txtDescription = new JTextField();
        JTextField txtStartDate = new JTextField();
        JTextField txtDeadline = new JTextField();

        JComboBox<String> cmbProjectStatus = new JComboBox<>(
                new String[]{
                    "Not Started", "In Progress", "Completed"
                });

        if (editing) {
            txtName.setText(String.valueOf(
                    tableModel.getValueAt(modelRow, 1)));
            txtDescription.setText(String.valueOf(
                    tableModel.getValueAt(modelRow, 2)));
            txtStartDate.setText(String.valueOf(
                    tableModel.getValueAt(modelRow, 3)));
            txtDeadline.setText(String.valueOf(
                    tableModel.getValueAt(modelRow, 4)));
            cmbProjectStatus.setSelectedItem(
                    tableModel.getValueAt(modelRow, 5));
        } else {
            txtStartDate.setText(LocalDate.now().toString());
            txtDeadline.setText(
                    LocalDate.now().plusDays(7).toString());
        }

        JPanel form = new JPanel(new GridLayout(0, 2, 10, 10));
        form.add(new JLabel("Project Name:"));
        form.add(txtName);
        form.add(new JLabel("Description:"));
        form.add(txtDescription);
        form.add(new JLabel("Start Date (YYYY-MM-DD):"));
        form.add(txtStartDate);
        form.add(new JLabel("Deadline (YYYY-MM-DD):"));
        form.add(txtDeadline);
        form.add(new JLabel("Status:"));
        form.add(cmbProjectStatus);

        int result = JOptionPane.showConfirmDialog(
                this,
                form,
                editing ? "Edit Project" : "Add Project",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        String name = txtName.getText().trim();
        String description = txtDescription.getText().trim();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Project name is required.");
            return;
        }

        try {
            LocalDate startDate = LocalDate.parse(
                    txtStartDate.getText().trim());
            LocalDate deadline = LocalDate.parse(
                    txtDeadline.getText().trim());

            if (deadline.isBefore(startDate)) {
                JOptionPane.showMessageDialog(this,
                        "Deadline cannot be earlier than start date.");
                return;
            }

            String status = String.valueOf(
                    cmbProjectStatus.getSelectedItem());

            int projectId = editing
                    ? Integer.parseInt(String.valueOf(
                            tableModel.getValueAt(modelRow, 0)))
                    : 0;

            Project project = new Project(
                    projectId,
                    name,
                    description,
                    startDate,
                    deadline,
                    status,
                    currentUserId
            );

            boolean success;

            if (editing) {
                success = projectDAO.updateProject(project);
            } else {
                success = projectDAO.addProject(project);
            }

            if (success) {
                JOptionPane.showMessageDialog(this,
                        editing
                                ? "Project updated successfully!"
                                : "Project added successfully!");

                loadProjects();
            } else {
                JOptionPane.showMessageDialog(this,
                        "The project could not be saved. "
                        + "Check your database and user ID.",
                        "Save Failed",
                        JOptionPane.ERROR_MESSAGE);
            }

        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this,
                    "Enter dates in YYYY-MM-DD format.\n"
                    + "Example: 2026-10-20");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error saving project:\n" + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editSelectedProject() {
        int selectedRow = tblProjects.getSelectedRow();

        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this,
                    "Please select a project to edit.");
            return;
        }

        int modelRow = tblProjects.convertRowIndexToModel(selectedRow);
        showProjectDialog(modelRow);
    }

    // DELETE PROJECT FROM MYSQL
    private void deleteSelectedProject() {
        int selectedRow = tblProjects.getSelectedRow();

        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this,
                    "Please select a project to delete.");
            return;
        }

        int modelRow = tblProjects.convertRowIndexToModel(selectedRow);

        int projectId = Integer.parseInt(String.valueOf(
                tableModel.getValueAt(modelRow, 0)));

        String projectName = String.valueOf(
                tableModel.getValueAt(modelRow, 1));

        int result = JOptionPane.showConfirmDialog(
                this,
                "Delete project \"" + projectName + "\"?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (result != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            boolean success = projectDAO.deleteProject(projectId);

            if (success) {
                JOptionPane.showMessageDialog(this,
                        "Project deleted successfully!");
                loadProjects();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Project was not deleted.",
                        "Delete Failed",
                        JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Could not delete the project.\n"
                    + "It may be linked to existing tasks or members.\n"
                    + ex.getMessage(),
                    "Delete Error",
                    JOptionPane.ERROR_MESSAGE);
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

        contentPanel4 = new javax.swing.JPanel();
        panelProjectOverview = new javax.swing.JPanel();
        lblId = new javax.swing.JLabel();
        lblProjectname = new javax.swing.JLabel();
        lblTeam = new javax.swing.JLabel();
        lblAction = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        jSeparator6 = new javax.swing.JSeparator();
        lblTaskflow = new javax.swing.JLabel();
        lblProgressValue = new javax.swing.JLabel();
        lblTeamIcon = new javax.swing.JLabel();
        lblStatus1 = new javax.swing.JLabel();
        jSeparator7 = new javax.swing.JSeparator();
        lblDescription = new javax.swing.JLabel();
        lblStartdate = new javax.swing.JLabel();
        lblDeadlines = new javax.swing.JLabel();
        lblStatus = new javax.swing.JLabel();
        lblProgress = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        lbldescription1 = new javax.swing.JLabel();
        lblStartDate1 = new javax.swing.JLabel();
        lblDeadlines1 = new javax.swing.JLabel();
        jProgressBar1 = new javax.swing.JProgressBar();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtSearchProject = new javax.swing.JTextField();
        cmbStatusFilter = new javax.swing.JComboBox<>();
        cmbTeamFilter = new javax.swing.JComboBox<>();
        lblProjectOverview1 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        btnAddProject = new javax.swing.JButton();

        contentPanel4.setBackground(new java.awt.Color(245, 248, 252));
        contentPanel4.setPreferredSize(new java.awt.Dimension(1000, 780));

        panelProjectOverview.setBackground(new java.awt.Color(255, 255, 255));
        panelProjectOverview.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(214, 228, 240), 1, true));
        panelProjectOverview.setPreferredSize(new java.awt.Dimension(354, 450));

        lblId.setBackground(new java.awt.Color(35, 116, 209));
        lblId.setForeground(new java.awt.Color(71, 85, 105));
        lblId.setText("ID");

        lblProjectname.setBackground(new java.awt.Color(100, 116, 139));
        lblProjectname.setForeground(new java.awt.Color(71, 85, 105));
        lblProjectname.setText("Project Name");

        lblTeam.setBackground(new java.awt.Color(100, 116, 139));
        lblTeam.setForeground(new java.awt.Color(71, 85, 105));
        lblTeam.setText("Team");

        lblAction.setBackground(new java.awt.Color(100, 116, 139));
        lblAction.setForeground(new java.awt.Color(71, 85, 105));
        lblAction.setText("Actions");

        lblTaskflow.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblTaskflow.setForeground(new java.awt.Color(23, 59, 108));
        lblTaskflow.setText("TASKFLOW");

        lblProgressValue.setBackground(new java.awt.Color(23, 59, 108));
        lblProgressValue.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        lblProgressValue.setForeground(new java.awt.Color(23, 59, 108));
        lblProgressValue.setText("50%");

        lblTeamIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/group_18dp_173B6C_FILL0_wght400_GRAD0_opszTWO.png"))); // NOI18N

        lblStatus1.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        lblStatus1.setForeground(new java.awt.Color(39, 101, 181));
        lblStatus1.setText("In Progress");

        lblDescription.setBackground(new java.awt.Color(100, 116, 139));
        lblDescription.setForeground(new java.awt.Color(71, 85, 105));
        lblDescription.setText("Description");

        lblStartdate.setBackground(new java.awt.Color(100, 116, 139));
        lblStartdate.setForeground(new java.awt.Color(71, 85, 105));
        lblStartdate.setText("Start Date");

        lblDeadlines.setBackground(new java.awt.Color(100, 116, 139));
        lblDeadlines.setForeground(new java.awt.Color(71, 85, 105));
        lblDeadlines.setText("Deadlines");

        lblStatus.setBackground(new java.awt.Color(100, 116, 139));
        lblStatus.setForeground(new java.awt.Color(71, 85, 105));
        lblStatus.setText("Status");

        lblProgress.setBackground(new java.awt.Color(100, 116, 139));
        lblProgress.setForeground(new java.awt.Color(71, 85, 105));
        lblProgress.setText("Progress");

        jLabel8.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(23, 59, 108));
        jLabel8.setText("3");

        lbldescription1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lbldescription1.setForeground(new java.awt.Color(23, 59, 108));
        lbldescription1.setText("Design Homepage");

        lblStartDate1.setBackground(new java.awt.Color(23, 59, 108));
        lblStartDate1.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        lblStartDate1.setForeground(new java.awt.Color(23, 59, 108));
        lblStartDate1.setText("May 10, 2026");

        lblDeadlines1.setBackground(new java.awt.Color(23, 59, 108));
        lblDeadlines1.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        lblDeadlines1.setForeground(new java.awt.Color(23, 59, 108));
        lblDeadlines1.setText("May 15, 2026");

        jProgressBar1.setForeground(new java.awt.Color(39, 101, 181));
        jProgressBar1.setValue(50);

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/visibility_18dp_173B6C_FILL0_wght400_GRAD0_opsz20.png"))); // NOI18N

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/edit_18dp_173B6C_FILL0_wght400_GRAD0_opsz20.png"))); // NOI18N

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/delete_18dp_173B6C_FILL0_wght400_GRAD0_opsz20.png"))); // NOI18N

        txtSearchProject.setText("Search Projects...");

        cmbStatusFilter.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cmbTeamFilter.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout panelProjectOverviewLayout = new javax.swing.GroupLayout(panelProjectOverview);
        panelProjectOverview.setLayout(panelProjectOverviewLayout);
        panelProjectOverviewLayout.setHorizontalGroup(
            panelProjectOverviewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelProjectOverviewLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelProjectOverviewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelProjectOverviewLayout.createSequentialGroup()
                        .addGroup(panelProjectOverviewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelProjectOverviewLayout.createSequentialGroup()
                                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(39, 39, 39)
                                .addComponent(lblTaskflow, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(66, 66, 66)
                                .addComponent(lbldescription1, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(27, 27, 27)
                                .addComponent(lblStartDate1)
                                .addGap(51, 51, 51)
                                .addComponent(lblDeadlines1)
                                .addGap(50, 50, 50)
                                .addComponent(lblStatus1)
                                .addGap(41, 41, 41)
                                .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblProgressValue, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(33, 33, 33)
                                .addComponent(lblTeamIcon)
                                .addGap(84, 84, 84)
                                .addComponent(jLabel3)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel5)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel10))
                            .addGroup(panelProjectOverviewLayout.createSequentialGroup()
                                .addComponent(lblId)
                                .addGap(48, 48, 48)
                                .addComponent(lblProjectname)
                                .addGap(110, 110, 110)
                                .addComponent(lblDescription)
                                .addGap(150, 150, 150)
                                .addComponent(lblStartdate)
                                .addGap(84, 84, 84)
                                .addComponent(lblDeadlines)
                                .addGap(82, 82, 82)
                                .addComponent(lblStatus)
                                .addGap(69, 69, 69)
                                .addComponent(lblProgress)
                                .addGap(83, 83, 83)
                                .addComponent(lblTeam)
                                .addGap(70, 70, 70)
                                .addComponent(lblAction)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(panelProjectOverviewLayout.createSequentialGroup()
                        .addGroup(panelProjectOverviewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator6)
                            .addComponent(jSeparator7, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jSeparator3))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelProjectOverviewLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(panelProjectOverviewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelProjectOverviewLayout.createSequentialGroup()
                                .addComponent(txtSearchProject, javax.swing.GroupLayout.PREFERRED_SIZE, 466, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(116, 116, 116)
                                .addComponent(cmbStatusFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(cmbTeamFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(53, 53, 53))))))
        );
        panelProjectOverviewLayout.setVerticalGroup(
            panelProjectOverviewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelProjectOverviewLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(panelProjectOverviewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSearchProject, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbStatusFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbTeamFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addGroup(panelProjectOverviewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblId)
                    .addComponent(lblProjectname)
                    .addComponent(lblDescription)
                    .addComponent(lblStartdate)
                    .addComponent(lblDeadlines)
                    .addComponent(lblStatus)
                    .addComponent(lblProgress)
                    .addComponent(lblTeam)
                    .addComponent(lblAction))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelProjectOverviewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGroup(panelProjectOverviewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelProjectOverviewLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelProjectOverviewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblTaskflow)
                            .addComponent(jLabel8)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelProjectOverviewLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(panelProjectOverviewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbldescription1)
                            .addComponent(lblStartDate1)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelProjectOverviewLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(panelProjectOverviewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblStatus1)
                            .addComponent(lblDeadlines1)
                            .addComponent(lblProgressValue)
                            .addGroup(panelProjectOverviewLayout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(lblTeamIcon)
                            .addGroup(panelProjectOverviewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel5)
                                .addComponent(jLabel3)
                                .addComponent(jLabel10)))))
                .addGap(18, 18, 18)
                .addComponent(jSeparator7, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(477, Short.MAX_VALUE))
        );

        lblProjectOverview1.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        lblProjectOverview1.setForeground(new java.awt.Color(18, 58, 115));
        lblProjectOverview1.setText("Projects");

        jLabel13.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(39, 101, 181));
        jLabel13.setText("Manage and track all projects in TASKFLOW.");

        btnAddProject.setText("Add Project");
        btnAddProject.addActionListener(this::btnAddProjectActionPerformed);

        javax.swing.GroupLayout contentPanel4Layout = new javax.swing.GroupLayout(contentPanel4);
        contentPanel4.setLayout(contentPanel4Layout);
        contentPanel4Layout.setHorizontalGroup(
            contentPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contentPanel4Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(contentPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblProjectOverview1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, contentPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(contentPanel4Layout.createSequentialGroup()
                            .addComponent(jLabel13)
                            .addGap(892, 892, 892)
                            .addComponent(btnAddProject, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(panelProjectOverview, javax.swing.GroupLayout.PREFERRED_SIZE, 1261, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(14, Short.MAX_VALUE))
        );
        contentPanel4Layout.setVerticalGroup(
            contentPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contentPanel4Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(lblProjectOverview1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(contentPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(btnAddProject, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(panelProjectOverview, javax.swing.GroupLayout.PREFERRED_SIZE, 635, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(70, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(contentPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 1290, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(contentPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, 827, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnAddProjectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddProjectActionPerformed
     JTextField txtName = new JTextField();
    JTextField txtDescriptionInput = new JTextField();
    JTextField txtStart = new JTextField();
    JTextField txtDeadlineInput = new JTextField();

    JComboBox<String> cmbProjectStatus = new JComboBox<>(
            new String[]{
                "Not Started",
                "In Progress",
                "Completed"
            }
    );

    JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));

    form.add(new JLabel("Project Name:"));
    form.add(txtName);

    form.add(new JLabel("Description:"));
    form.add(txtDescriptionInput);

    form.add(new JLabel("Start Date (YYYY-MM-DD):"));
    form.add(txtStart);

    form.add(new JLabel("Deadline (YYYY-MM-DD):"));
    form.add(txtDeadlineInput);

    form.add(new JLabel("Status:"));
    form.add(cmbProjectStatus);

    int result = JOptionPane.showConfirmDialog(
            this,
            form,
            "Add New Project",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
    );

    if (result != JOptionPane.OK_OPTION) {
        return;
    }

    String name = txtName.getText().trim();
    String description = txtDescriptionInput.getText().trim();
    String startText = txtStart.getText().trim();
    String deadlineText = txtDeadlineInput.getText().trim();

    if (name.isEmpty()
            || startText.isEmpty()
            || deadlineText.isEmpty()) {

        JOptionPane.showMessageDialog(
                this,
                "Project name, start date, and deadline are required."
        );
        return;
    }

    try {
        LocalDate startDate = LocalDate.parse(startText);
        LocalDate deadline = LocalDate.parse(deadlineText);

        if (deadline.isBefore(startDate)) {
            JOptionPane.showMessageDialog(
                    this,
                    "Deadline cannot be earlier than the start date."
            );
            return;
        }

        String status = cmbProjectStatus.getSelectedItem().toString();

        // This assumes user ID 1 is a valid user in your database.
        Project project = new Project(
                0,
                name,
                description,
                startDate,
                deadline,
                status,
                1
        );

        ProjectDAO projectDAO = new ProjectDAO();

        if (projectDAO.addProject(project)) {
            JOptionPane.showMessageDialog(
                    this,
                    "Project added successfully!"
            );

            txtSearchProject.setText("");
            cmbStatusFilter.setSelectedIndex(0);
            loadProjects();
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Could not save the project. Check your database "
                    + "connection and user ID."
            );
        }

    } catch (DateTimeParseException e) {
        JOptionPane.showMessageDialog(
                this,
                "Invalid date. Use YYYY-MM-DD, for example 2026-10-20."
        );
    }

    }//GEN-LAST:event_btnAddProjectActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddProject;
    private javax.swing.JComboBox<String> cmbStatusFilter;
    private javax.swing.JComboBox<String> cmbTeamFilter;
    private javax.swing.JPanel contentPanel4;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JLabel lblAction;
    private javax.swing.JLabel lblDeadlines;
    private javax.swing.JLabel lblDeadlines1;
    private javax.swing.JLabel lblDescription;
    private javax.swing.JLabel lblId;
    private javax.swing.JLabel lblProgress;
    private javax.swing.JLabel lblProgressValue;
    private javax.swing.JLabel lblProjectOverview1;
    private javax.swing.JLabel lblProjectname;
    private javax.swing.JLabel lblStartDate1;
    private javax.swing.JLabel lblStartdate;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblStatus1;
    private javax.swing.JLabel lblTaskflow;
    private javax.swing.JLabel lblTeam;
    private javax.swing.JLabel lblTeamIcon;
    private javax.swing.JLabel lbldescription1;
    private javax.swing.JPanel panelProjectOverview;
    private javax.swing.JTextField txtSearchProject;
    // End of variables declaration//GEN-END:variables
}
