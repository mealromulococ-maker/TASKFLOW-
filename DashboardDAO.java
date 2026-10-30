
package dao;

import database.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.table.DefaultTableModel;

public class DashboardDAO {

    // TOTAL PROJECTS
    public int getTotalProjects() {
        String sql = "SELECT COUNT(*) FROM projects";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    // TOTAL TASKS
    public int getTotalTasks() {
        String sql = "SELECT COUNT(*) FROM tasks";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    // COMPLETED TASKS
    public int getCompletedTasks() {
        String sql = """
                SELECT COUNT(*)
                FROM tasks
                WHERE task_status = 'Completed'
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    // PENDING TASKS
    public int getPendingTasks() {
        String sql = """
                SELECT COUNT(*)
                FROM tasks
                WHERE task_status IS NULL
                   OR task_status != 'Completed'
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    // RECENT TASKS
    public void loadRecentTasks(DefaultTableModel model) {
        String sql = """
                SELECT
                    t.task_id,
                    t.task_name,
                    p.project_name,
                    t.priority,
                    t.deadline,
                    t.task_status AS status
                FROM tasks t
                LEFT JOIN projects p
                    ON t.project_id = p.project_id
                ORDER BY t.task_id DESC
                LIMIT 5
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            model.setRowCount(0);

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("task_id"),
                    rs.getString("task_name"),
                    rs.getString("project_name"),
                    rs.getString("priority"),
                    rs.getDate("deadline"),
                    rs.getString("status")
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // UPCOMING PROJECT DEADLINES
    public String getUpcomingDeadlines() {
        String sql = """
                SELECT project_name, deadline
                FROM projects
                WHERE deadline >= CURDATE()
                ORDER BY deadline ASC
                LIMIT 3
                """;

        StringBuilder deadlines = new StringBuilder();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                deadlines.append(rs.getString("project_name"))
                        .append(" - ")
                        .append(rs.getString("deadline"))
                        .append("\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (deadlines.length() == 0) {
            return "No upcoming project deadlines";
        }

        return deadlines.toString();
    }
}
