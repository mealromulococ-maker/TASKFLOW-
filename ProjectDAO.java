package dao;

import database.DBConnection;
import model.Project;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProjectDAO {

    // ADD PROJECT
    public boolean addProject(Project project) {

        String sql = "INSERT INTO projects "
                + "(project_name, description, start_date, deadline, project_status, user_id) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, project.getProjectName());
            stmt.setString(2, project.getDescription());
            stmt.setDate(3, Date.valueOf(project.getStartDate()));
            stmt.setDate(4, Date.valueOf(project.getDeadline()));
            stmt.setString(5, project.getStatus());
            stmt.setInt(6, project.getCreatedBy());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error adding project: " + e.getMessage());
            return false;
        }
    }

    // GET PROJECT BY ID
    public Project getProjectById(int projectId) {

        String sql = "SELECT * FROM projects WHERE project_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, projectId);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {

                    return new Project(
                            rs.getInt("project_id"),
                            rs.getString("project_name"),
                            rs.getString("description"),
                            rs.getDate("start_date").toLocalDate(),
                            rs.getDate("deadline").toLocalDate(),
                            rs.getString("project_status"),
                            rs.getInt("user_id")
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println("Error finding project: " + e.getMessage());
        }

        return null;
    }

    // GET ALL PROJECTS
    public List<Project> getAllProjects() {

        List<Project> projects = new ArrayList<>();

        String sql = "SELECT * FROM projects";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                projects.add(
                        new Project(
                                rs.getInt("project_id"),
                                rs.getString("project_name"),
                                rs.getString("description"),
                                rs.getDate("start_date").toLocalDate(),
                                rs.getDate("deadline").toLocalDate(),
                                rs.getString("project_status"),
                                rs.getInt("user_id")
                        )
                );
            }

        } catch (SQLException e) {
            System.out.println("Error loading projects: " + e.getMessage());
        }

        return projects;
    }

    // UPDATE PROJECT
    public boolean updateProject(Project project) {

        String sql = "UPDATE projects SET "
                + "project_name=?, description=?, start_date=?, "
                + "deadline=?, project_status=?, user_id=? "
                + "WHERE project_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, project.getProjectName());
            stmt.setString(2, project.getDescription());
            stmt.setDate(3, Date.valueOf(project.getStartDate()));
            stmt.setDate(4, Date.valueOf(project.getDeadline()));
            stmt.setString(5, project.getStatus());
            stmt.setInt(6, project.getCreatedBy());
            stmt.setInt(7, project.getProjectId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error updating project: " + e.getMessage());
            return false;
        }
    }

    // DELETE PROJECT
    public boolean deleteProject(int projectId) {

        String sql = "DELETE FROM projects WHERE project_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, projectId);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error deleting project: " + e.getMessage());
            return false;
        }
    }
}