package com.student.dao;

import com.student.model.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    private final String jdbcURL = "jdbc:mysql://localhost:3306/student_management?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private final String jdbcUsername = "root";
    private final String jdbcPassword = "Vinny1411@";

    private Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Cannot load JDBC Driver", e);
        }
        return DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
    }

    // Map 1 record => Student
    private Student mapRow(ResultSet rs) throws SQLException {
        Student s = new Student();
        s.setId(rs.getInt("id"));
        s.setStudentCode(rs.getString("student_code"));
        s.setFullName(rs.getString("full_name"));
        s.setEmail(rs.getString("email"));
        s.setMajor(rs.getString("major"));
        // Bonus 2: photo
        try {
            s.setPhoto(rs.getString("photo"));
        } catch (SQLException e) {
        }
        return s;
    }

    // ========== CRUD ==========

    public List<Student> getAllStudents() {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM students ORDER BY id DESC";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Student getStudentById(int id) {
        String sql = "SELECT * FROM students WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean addStudent(Student student) {
        String sql = "INSERT INTO students (student_code, full_name, email, major, photo) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, student.getStudentCode());
            ps.setString(2, student.getFullName());
            ps.setString(3, student.getEmail());
            ps.setString(4, student.getMajor());
            ps.setString(5, student.getPhoto());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateStudent(Student student) {
        String sql = "UPDATE students SET full_name = ?, email = ?, major = ?, photo = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, student.getFullName());
            ps.setString(2, student.getEmail());
            ps.setString(3, student.getMajor());
            ps.setString(4, student.getPhoto());
            ps.setInt(5, student.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteStudent(int id) {
        String sql = "DELETE FROM students WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ========== EX5: SEARCH ==========

    public List<Student> searchStudents(String keyword) {
        List<Student> list = new ArrayList<>();
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllStudents(); // yêu cầu: keyword rỗng => show all
        }

        String searchPattern = "%" + keyword.trim() + "%";
        String sql =
                "SELECT * FROM students " +
                "WHERE student_code LIKE ? OR full_name LIKE ? OR email LIKE ? " +
                "ORDER BY id DESC";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, searchPattern);
            ps.setString(2, searchPattern);
            ps.setString(3, searchPattern);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // ========== EX7: SORT & FILTER ==========

    private String validateSortBy(String sortBy) {
        if (sortBy == null) return "id";
        switch (sortBy) {
            case "id":
            case "student_code":
            case "full_name":
            case "email":
            case "major":
                return sortBy;
            default:
                return "id";
        }
    }

    private String validateOrder(String order) {
        if ("desc".equalsIgnoreCase(order)) {
            return "DESC";
        }
        return "ASC";
    }

    public List<Student> getStudentsSorted(String sortBy, String order) {
        List<Student> list = new ArrayList<>();
        String safeSortBy = validateSortBy(sortBy);
        String safeOrder = validateOrder(order);

        String sql = "SELECT * FROM students ORDER BY " + safeSortBy + " " + safeOrder;

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Student> getStudentsByMajor(String major) {
        if (major == null || major.trim().isEmpty() ||
            "ALL".equalsIgnoreCase(major)) {
            return getAllStudents();
        }

        List<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM students WHERE major = ? ORDER BY id DESC";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, major);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // BONUS 3: combined search + filter + sort
    public List<Student> getStudentsFiltered(String keyword, String major,
                                             String sortBy, String order) {
        List<Student> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder("SELECT * FROM students WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            String pattern = "%" + keyword.trim() + "%";
            sql.append(" AND (student_code LIKE ? OR full_name LIKE ? OR email LIKE ?)");
            params.add(pattern);
            params.add(pattern);
            params.add(pattern);
        }

        if (major != null && !major.trim().isEmpty() &&
                !"ALL".equalsIgnoreCase(major)) {
            sql.append(" AND major = ?");
            params.add(major);
        }

        sql.append(" ORDER BY ")
           .append(validateSortBy(sortBy))
           .append(" ")
           .append(validateOrder(order));

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // ========== EX8: PAGINATION ==========

    public int getTotalStudents() {
        String sql = "SELECT COUNT(*) FROM students";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Student> getStudentsPaginated(int offset, int limit) {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM students ORDER BY id DESC LIMIT ? OFFSET ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, limit);
            ps.setInt(2, offset);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    }



