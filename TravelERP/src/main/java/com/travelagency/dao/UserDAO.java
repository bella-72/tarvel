package com.travelagency.dao;

import com.travelagency.model.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * User Data Access Object
 */
public class UserDAO extends GenericRepository<User> {

    public UserDAO() {
        super(User.class);
    }

    @Override
    public boolean create(User user) {
        String sql = "INSERT INTO users (username, password, email, role, is_active) VALUES (?, ?, ?, ?, ?)";
        try {
            dbConnection.executeUpdate(sql, stmt -> {
                stmt.setString(1, user.getUsername());
                stmt.setString(2, user.getPassword());
                stmt.setString(3, user.getEmail());
                stmt.setString(4, user.getRole().getValue());
                stmt.setBoolean(5, user.isActive());
            });
            return true;
        } catch (SQLException e) {
            System.err.println("Error creating user: " + e.getMessage());
            return false;
        }
    }

    @Override
    public User readById(int id) {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        try {
            return dbConnection.executeQuery(sql, stmt -> {
                stmt.setInt(1, id);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
                return null;
            });
        } catch (SQLException e) {
            System.err.println("Error reading user: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<User> readAll() {
        String sql = "SELECT * FROM users";
        return executeQuery(sql, this::mapResultSetToUser);
    }

    @Override
    public boolean update(User user) {
        String sql = "UPDATE users SET username = ?, password = ?, email = ?, role = ?, is_active = ?, updated_at = ? WHERE user_id = ?";
        try {
            dbConnection.executeUpdate(sql, stmt -> {
                stmt.setString(1, user.getUsername());
                stmt.setString(2, user.getPassword());
                stmt.setString(3, user.getEmail());
                stmt.setString(4, user.getRole().getValue());
                stmt.setBoolean(5, user.isActive());
                stmt.setString(6, LocalDateTime.now().toString());
                stmt.setInt(7, user.getId());
            });
            return true;
        } catch (SQLException e) {
            System.err.println("Error updating user: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM users WHERE user_id = ?";
        try {
            dbConnection.executeUpdate(sql, stmt -> stmt.setInt(1, id));
            return true;
        } catch (SQLException e) {
            System.err.println("Error deleting user: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean exists(int id) {
        String sql = "SELECT COUNT(*) FROM users WHERE user_id = ?";
        try {
            return dbConnection.executeQuery(sql, stmt -> {
                stmt.setInt(1, id);
                ResultSet rs = stmt.executeQuery();
                return rs.next() && rs.getInt(1) > 0;
            });
        } catch (SQLException e) {
            System.err.println("Error checking user existence: " + e.getMessage());
            return false;
        }
    }

    /**
     * Find user by username
     */
    public Optional<User> findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try {
            User user = dbConnection.executeQuery(sql, stmt -> {
                stmt.setString(1, username);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
                return null;
            });
            return Optional.ofNullable(user);
        } catch (SQLException e) {
            System.err.println("Error finding user by username: " + e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Find user by email
     */
    public Optional<User> findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        try {
            User user = dbConnection.executeQuery(sql, stmt -> {
                stmt.setString(1, email);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
                return null;
            });
            return Optional.ofNullable(user);
        } catch (SQLException e) {
            System.err.println("Error finding user by email: " + e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Map ResultSet to User object
     */
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("user_id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setEmail(rs.getString("email"));
        user.setRole(User.UserRole.fromString(rs.getString("role")));
        user.setActive(rs.getBoolean("is_active"));
        return user;
    }
}
