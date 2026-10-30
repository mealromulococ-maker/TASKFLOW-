
package dao;

import database.DBConnection;
import model.Member;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MemberDAO {

    // ADD MEMBER
    public boolean addMember(Member member) {
        String sql = "INSERT INTO member "
                   + "(full_name, email, role, status) "
                   + "VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, member.getMemberName());
            stmt.setString(2, member.getEmail());
            stmt.setString(3, member.getRole());
            stmt.setString(4, member.getStatus());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error adding member: " + e.getMessage());
            return false;
        }
    }

    // GET MEMBER BY ID
    public Member getMemberById(int memberId) {
        String sql = "SELECT * FROM member WHERE member_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, memberId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapMember(rs);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error finding member: " + e.getMessage());
        }

        return null;
    }

    // GET ALL MEMBERS
    public List<Member> getAllMembers() {
        List<Member> members = new ArrayList<>();

        String sql = "SELECT * FROM member ORDER BY member_id DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                members.add(mapMember(rs));
            }

        } catch (SQLException e) {
            System.out.println("Error loading members: " + e.getMessage());
        }

        return members;
    }

    // UPDATE MEMBER
    public boolean updateMember(Member member) {
        String sql = "UPDATE member SET full_name = ?, email = ?, "
                   + "role = ?, status = ? WHERE member_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, member.getMemberName());
            stmt.setString(2, member.getEmail());
            stmt.setString(3, member.getRole());
            stmt.setString(4, member.getStatus());
            stmt.setInt(5, member.getMemberId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error updating member: " + e.getMessage());
            return false;
        }
    }

    // DELETE MEMBER
    public boolean deleteMember(int memberId) {
        String sql = "DELETE FROM member WHERE member_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, memberId);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error deleting member: " + e.getMessage());
            return false;
        }
    }

    // CONVERT DATABASE ROW TO MEMBER OBJECT
    private Member mapMember(ResultSet rs) throws SQLException {
        return new Member(
                rs.getInt("member_id"),
                rs.getString("full_name"),
                rs.getString("email"),
                rs.getString("role"),
                rs.getString("status")
        );
    }
}
