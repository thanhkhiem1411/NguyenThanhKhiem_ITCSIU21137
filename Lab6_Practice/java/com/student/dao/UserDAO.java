package com.student.dao;

import com.student.model.User;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;

public class UserDAO {

    private final String jdbcURL =
            "jdbc:mysql://localhost:3306/student_management?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private final String jdbcUsername = "root";        // sửa cho khớp StudentDAO
    private final String jdbcPassword = "Vinny1411@";            // sửa cho khớp StudentDAO

    private static final String SQL_AUTHENTICATE =
            "SELECT * FROM users WHERE username = ? AND is_active = TRUE";

    private static final String SQL_UPDATE_LAST_LOGIN =
            "UPDATE users SET last_login = NOW() WHERE id = ?";

    // LẤY KẾT NỐI – copy từ StudentDAO qua
    private Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL Driver not found", e);
        }
        return DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
    }

    // HÀM AUTHENTICATE
    public User authenticate(String username, String password) {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_AUTHENTICATE)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String hashedPassword = rs.getString("password");

                    // DEBUG: in ra console để check
                    System.out.println("Login attempt for: " + username);
                    System.out.println("DB hash: " + hashedPassword);

                    if (BCrypt.checkpw(password, hashedPassword)) {
                        User user = mapResultSetToUser(rs);
                        updateLastLogin(user.getId());
                        System.out.println("Authentication SUCCESS for: " + username);
                        return user;
                    } else {
                        System.out.println("BCrypt.checkpw FAILED for: " + username);
                    }
                } else {
                    System.out.println("No user found with username: " + username);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void updateLastLogin(int userId) {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE_LAST_LOGIN)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User getUserById(int id) {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User u = new User();
        u.setId(rs.getInt("id"));
        u.setUsername(rs.getString("username"));
        u.setPassword(rs.getString("password"));
        u.setFullName(rs.getString("full_name"));
        u.setRole(rs.getString("role"));
        u.setActive(rs.getBoolean("is_active"));
        u.setCreatedAt(rs.getTimestamp("created_at"));
        u.setLastLogin(rs.getTimestamp("last_login"));
        return u;
    }

    // HÀM MAIN ĐỂ TEST RIÊNG DAO
    public static void main(String[] args) {
        UserDAO dao = new UserDAO();
        User u = dao.authenticate("admin", "password123");
        if (u != null) {
            System.out.println("Test main: Authentication successful → " + u.getUsername());
            System.out.println(u);
        } else {
            System.out.println("Test main: Authentication FAILED");
        }
    }
}
