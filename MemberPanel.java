/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */


package gui;

import dao.MemberDAO;
import model.Member;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.RowFilter;
import java.awt.*;
import java.util.List;
import java.util.regex.Pattern;

public class MemberPanel extends JPanel {

    private final MemberDAO memberDAO = new MemberDAO();

    private JTable tblMembers;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;

    private JTextField txtSearch;
    private JLabel lblTotalMembers;
    private JLabel lblMessage;

    // COLORS MATCHING PROJECTSPANEL
    private final Color NAVY = new Color(23, 59, 108);
    private final Color BLUE = new Color(45, 101, 163);
    private final Color BG = new Color(245, 248, 252);
    private final Color WHITE = Color.WHITE;
    private final Color BORDER = new Color(225, 230, 238);

    public MemberPanel() {
        setLayout(new BorderLayout(15, 15));
        setBackground(BG);
        setBorder(BorderFactory.createEmptyBorder(22, 22, 22, 22));

        createInterface();
        loadMembers();
    }

    private void createInterface() {

        // HEADER
        JPanel header = new JPanel(new BorderLayout(5, 5));
        header.setOpaque(false);

        JLabel title = new JLabel("TEAM MANAGEMENT");
        title.setFont(new Font("Segoe UI", Font.BOLD, 27));
        title.setForeground(NAVY);

        lblTotalMembers = new JLabel("Total Members: 0");
        lblTotalMembers.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTotalMembers.setForeground(BLUE);

        header.add(title, BorderLayout.NORTH);
        header.add(lblTotalMembers, BorderLayout.SOUTH);

        // SEARCH
        JPanel toolbar = new JPanel(new BorderLayout(10, 10));
        toolbar.setOpaque(false);

        JPanel searchPanel = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 8, 0));
        searchPanel.setOpaque(false);

        JLabel lblSearch = new JLabel("Search:");
        lblSearch.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        txtSearch = new JTextField();
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtSearch.setPreferredSize(new Dimension(250, 36));
        txtSearch.setToolTipText(
                "Search name, email, role, or status");

        searchPanel.add(lblSearch);
        searchPanel.add(txtSearch);

        // BUTTONS - ORIGINAL DESIGN PRESERVED
        JButton btnAdd = createButton("+ Add Member", BLUE);
        JButton btnEdit = createButton("Edit Member", NAVY);
        JButton btnDelete = createButton(
                "Delete", new Color(180, 55, 55));

        JPanel buttonPanel = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 8, 0));
        buttonPanel.setOpaque(false);

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);

        toolbar.add(searchPanel, BorderLayout.WEST);
        toolbar.add(buttonPanel, BorderLayout.EAST);

        // TABLE WITH STATUS COLUMN
        String[] columns = {
            "Member ID", "Full Name", "Email", "Role", "Status"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblMembers = new JTable(tableModel);
        tblMembers.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tblMembers.setRowHeight(34);
        tblMembers.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION);
        tblMembers.setShowGrid(true);

        // TABLE COLORS MATCHING PROJECTSPANEL
        tblMembers.setGridColor(BORDER);
        tblMembers.setSelectionBackground(
                new Color(220, 233, 250));
        tblMembers.setSelectionForeground(Color.BLACK);
        tblMembers.setFillsViewportHeight(true);

        tblMembers.getTableHeader().setFont(
                new Font("Segoe UI", Font.BOLD, 13));
        tblMembers.getTableHeader().setBackground(
                new Color(235, 241, 249));
        tblMembers.getTableHeader().setForeground(NAVY);
        tblMembers.getTableHeader().setReorderingAllowed(false);

        tblMembers.getColumnModel().getColumn(0)
                .setPreferredWidth(75);
        tblMembers.getColumnModel().getColumn(1)
                .setPreferredWidth(190);
        tblMembers.getColumnModel().getColumn(2)
                .setPreferredWidth(220);
        tblMembers.getColumnModel().getColumn(3)
                .setPreferredWidth(120);
        tblMembers.getColumnModel().getColumn(4)
                .setPreferredWidth(100);

        sorter = new TableRowSorter<>(tableModel);
        tblMembers.setRowSorter(sorter);

        JScrollPane scrollPane = new JScrollPane(tblMembers);
        scrollPane.setBorder(
                BorderFactory.createLineBorder(BORDER));

        // FOOTER
        lblMessage = new JLabel(" ");
        lblMessage.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblMessage.setForeground(Color.GRAY);

        // MAIN CONTENT
        JPanel content = new JPanel(new BorderLayout(12, 12));
        content.setBackground(WHITE);
        content.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));

        content.add(toolbar, BorderLayout.NORTH);
        content.add(scrollPane, BorderLayout.CENTER);
        content.add(lblMessage, BorderLayout.SOUTH);

        add(header, BorderLayout.NORTH);
        add(content, BorderLayout.CENTER);

        // BUTTON ACTIONS
        btnAdd.addActionListener(e -> showMemberDialog(null));
        btnEdit.addActionListener(e -> editSelectedMember());
        btnDelete.addActionListener(e -> deleteSelectedMember());

        // SEARCH ACTION
        txtSearch.getDocument().addDocumentListener(
                new javax.swing.event.DocumentListener() {

            @Override
            public void insertUpdate(
                    javax.swing.event.DocumentEvent e) {
                applySearch();
            }

            @Override
            public void removeUpdate(
                    javax.swing.event.DocumentEvent e) {
                applySearch();
            }

            @Override
            public void changedUpdate(
                    javax.swing.event.DocumentEvent e) {
                applySearch();
            }
        });
    }

    // CREATE BUTTON - ORIGINAL DESIGN PRESERVED
    private JButton createButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setForeground(WHITE);
        button.setBackground(color);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(125, 36));

        return button;
    }

    // LOAD MEMBERS FROM DATABASE
    private void loadMembers() {
        tableModel.setRowCount(0);

        List<Member> members = memberDAO.getAllMembers();

        for (Member member : members) {
            tableModel.addRow(new Object[]{
                member.getMemberId(),
                member.getMemberName(),
                member.getEmail(),
                member.getRole(),
                member.getStatus()
            });
        }

        lblTotalMembers.setText(
                "Total Members: " + members.size());

        applySearch();
    }

    // SEARCH MEMBERS
    private void applySearch() {
        String search = txtSearch.getText().trim();

        if (search.isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(
                    RowFilter.regexFilter(
                            "(?i)" + Pattern.quote(search),
                            1, 2, 3, 4));
        }
    }

    // ADD OR EDIT MEMBER
    private void showMemberDialog(Member existingMember) {

        JTextField txtName = new JTextField();
        JTextField txtEmail = new JTextField();

        // ROLE COMBO BOX
        JComboBox<String> cmbRole = new JComboBox<>(
                new String[]{"Team Leader", "Member"});

        // STATUS COMBO BOX
        JComboBox<String> cmbStatus = new JComboBox<>(
                new String[]{"Active", "Inactive"});

        // LOAD EXISTING MEMBER DETAILS
        if (existingMember != null) {
            txtName.setText(existingMember.getMemberName());

            txtEmail.setText(
                    existingMember.getEmail() == null
                    ? "" : existingMember.getEmail());

            cmbRole.setSelectedItem(
                    existingMember.getRole() == null
                    ? "Member" : existingMember.getRole());

            cmbStatus.setSelectedItem(
                    existingMember.getStatus() == null
                    ? "Active" : existingMember.getStatus());
        }

        // FORM LAYOUT
        JPanel form = new JPanel(new GridLayout(0, 1, 5, 5));
        form.setPreferredSize(new Dimension(340, 280));

        form.add(new JLabel("Full Name *"));
        form.add(txtName);

        form.add(new JLabel("Email"));
        form.add(txtEmail);

        form.add(new JLabel("Role"));
        form.add(cmbRole);

        form.add(new JLabel("Status"));
        form.add(cmbStatus);

        int result = JOptionPane.showConfirmDialog(
                this,
                form,
                existingMember == null
                        ? "Add Team Member"
                        : "Edit Team Member",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        // GET INPUT VALUES
        String name = txtName.getText().trim();
        String email = txtEmail.getText().trim();
        String role = (String) cmbRole.getSelectedItem();
        String status = (String) cmbStatus.getSelectedItem();

        // VALIDATE NAME
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter the member's full name.",
                    "Missing Information",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // VALIDATE EMAIL
        if (!email.isEmpty()
                && !email.matches(
                        "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a valid email address.",
                    "Invalid Email",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // CREATE MEMBER OBJECT INCLUDING STATUS
        Member member = new Member(
                existingMember == null
                        ? 0 : existingMember.getMemberId(),
                name,
                email.isEmpty() ? null : email,
                role,
                status
        );

        boolean success;

        if (existingMember == null) {
            success = memberDAO.addMember(member);
        } else {
            success = memberDAO.updateMember(member);
        }

        // DISPLAY RESULT
        if (success) {
            lblMessage.setText(
                    existingMember == null
                            ? "Team member added successfully."
                            : "Team member updated successfully.");

            loadMembers();
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Unable to save the member. Check the "
                    + "NetBeans Output window for database errors.",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // EDIT SELECTED MEMBER
    private void editSelectedMember() {
        int selectedRow = tblMembers.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select a team member to edit.");
            return;
        }

        int modelRow =
                tblMembers.convertRowIndexToModel(selectedRow);

        int memberId =
                ((Number) tableModel.getValueAt(modelRow, 0))
                        .intValue();

        Member member = memberDAO.getMemberById(memberId);

        if (member != null) {
            showMemberDialog(member);
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Unable to find the selected member.");
        }
    }

    // DELETE SELECTED MEMBER
    private void deleteSelectedMember() {
        int selectedRow = tblMembers.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select a team member to delete.");
            return;
        }

        int modelRow =
                tblMembers.convertRowIndexToModel(selectedRow);

        int memberId =
                ((Number) tableModel.getValueAt(modelRow, 0))
                        .intValue();

        String memberName =
                tableModel.getValueAt(modelRow, 1).toString();

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Delete team member \"" + memberName + "\"?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        boolean success = memberDAO.deleteMember(memberId);

        if (success) {
            lblMessage.setText(
                    "Team member deleted successfully.");
            loadMembers();
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Unable to delete this member. They may be "
                    + "referenced by existing tasks or other records.",
                    "Delete Failed",
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

        contentPanel4.setBackground(new java.awt.Color(245, 248, 252));
        contentPanel4.setPreferredSize(new java.awt.Dimension(1000, 780));

        javax.swing.GroupLayout contentPanel4Layout = new javax.swing.GroupLayout(contentPanel4);
        contentPanel4.setLayout(contentPanel4Layout);
        contentPanel4Layout.setHorizontalGroup(
            contentPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1290, Short.MAX_VALUE)
        );
        contentPanel4Layout.setVerticalGroup(
            contentPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 752, Short.MAX_VALUE)
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
    // End of variables declaration//GEN-END:variables
}
