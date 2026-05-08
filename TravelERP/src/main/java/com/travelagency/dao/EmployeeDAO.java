package com.travelagency.dao;

import com.travelagency.model.Employee;
import com.travelagency.util.IsochronicMarker;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Employee Data Access Object
 * Manages persistence operations for Employee entities
 */
public class EmployeeDAO extends GenericRepository<Employee> implements IsochronicMarker {

    public EmployeeDAO() {
        super(Employee.class);
    }

    @Override
    public boolean create(Employee employee) {
        String sql = "INSERT INTO employees (user_id, first_name, last_name, email, phone, department, position, hire_date, salary, is_active) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            dbConnection.executeUpdate(sql, stmt -> {
                stmt.setInt(1, employee.getUserId());
                stmt.setString(2, employee.getFirstName());
                stmt.setString(3, employee.getLastName());
                stmt.setString(4, employee.getEmail());
                stmt.setString(5, employee.getPhone());
                stmt.setString(6, employee.getDepartment());
                stmt.setString(7, employee.getPosition());
                stmt.setObject(8, employee.getHireDate());
                stmt.setDouble(9, employee.getSalary());
                stmt.setBoolean(10, employee.isActive());
            });
            return true;
        } catch (SQLException e) {
            System.err.println("Error creating employee: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Employee readById(int id) {
        String sql = "SELECT * FROM employees WHERE employee_id = ?";
        try {
            return dbConnection.executeQuery(sql, stmt -> {
                stmt.setInt(1, id);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return mapResultSetToEmployee(rs);
                }
                return null;
            });
        } catch (SQLException e) {
            System.err.println("Error reading employee: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Employee> readAll() {
        String sql = "SELECT * FROM employees WHERE is_active = 1 ORDER BY last_name ASC";
        return executeQuery(sql, this::mapResultSetToEmployee);
    }

    @Override
    public boolean update(Employee employee) {
        String sql = "UPDATE employees SET user_id = ?, first_name = ?, last_name = ?, email = ?, phone = ?, department = ?, position = ?, hire_date = ?, salary = ?, is_active = ?, updated_at = ? WHERE employee_id = ?";
        try {
            dbConnection.executeUpdate(sql, stmt -> {
                stmt.setInt(1, employee.getUserId());
                stmt.setString(2, employee.getFirstName());
                stmt.setString(3, employee.getLastName());
                stmt.setString(4, employee.getEmail());
                stmt.setString(5, employee.getPhone());
                stmt.setString(6, employee.getDepartment());
                stmt.setString(7, employee.getPosition());
                stmt.setObject(8, employee.getHireDate());
                stmt.setDouble(9, employee.getSalary());
                stmt.setBoolean(10, employee.isActive());
                stmt.setObject(11, LocalDateTime.now());
                stmt.setInt(12, employee.getId());
            });
            return true;
        } catch (SQLException e) {
            System.err.println("Error updating employee: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "UPDATE employees SET is_active = 0, updated_at = ? WHERE employee_id = ?";
        try {
            dbConnection.executeUpdate(sql, stmt -> {
                stmt.setObject(1, LocalDateTime.now());
                stmt.setInt(2, id);
            });
            return true;
        } catch (SQLException e) {
            System.err.println("Error deleting employee: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean exists(int id) {
        String sql = "SELECT COUNT(*) FROM employees WHERE employee_id = ?";
        try {
            return dbConnection.executeQuery(sql, stmt -> {
                stmt.setInt(1, id);
                ResultSet rs = stmt.executeQuery();
                return rs.next() && rs.getInt(1) > 0;
            });
        } catch (SQLException e) {
            System.err.println("Error checking employee existence: " + e.getMessage());
            return false;
        }
    }

    public Employee getByEmail(String email) {
        String sql = "SELECT * FROM employees WHERE email = ? AND is_active = 1";
        try {
            return dbConnection.executeQuery(sql, stmt -> {
                stmt.setString(1, email);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return mapResultSetToEmployee(rs);
                }
                return null;
            });
        } catch (SQLException e) {
            System.err.println("Error finding employee by email: " + e.getMessage());
            return null;
        }
    }

    public List<Employee> getByDepartment(String department) {
        String sql = "SELECT * FROM employees WHERE department = ? AND is_active = 1 ORDER BY last_name ASC";
        try {
            return dbConnection.executeQuery(sql, stmt -> {
                stmt.setString(1, department);
                ResultSet rs = stmt.executeQuery();
                List<Employee> employees = new ArrayList<>();
                while (rs.next()) {
                    employees.add(mapResultSetToEmployee(rs));
                }
                return employees;
            });
        } catch (SQLException e) {
            System.err.println("Error getting employees by department: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Employee> getByPosition(String position) {
        String sql = "SELECT * FROM employees WHERE position = ? AND is_active = 1 ORDER BY last_name ASC";
        try {
            return dbConnection.executeQuery(sql, stmt -> {
                stmt.setString(1, position);
                ResultSet rs = stmt.executeQuery();
                List<Employee> employees = new ArrayList<>();
                while (rs.next()) {
                    employees.add(mapResultSetToEmployee(rs));
                }
                return employees;
            });
        } catch (SQLException e) {
            System.err.println("Error getting employees by position: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Employee> searchByName(String searchTerm) {
        String sql = "SELECT * FROM employees WHERE (first_name LIKE ? OR last_name LIKE ?) AND is_active = 1 ORDER BY last_name ASC";
        String pattern = "%" + searchTerm + "%";
        try {
            return dbConnection.executeQuery(sql, stmt -> {
                stmt.setString(1, pattern);
                stmt.setString(2, pattern);
                ResultSet rs = stmt.executeQuery();
                List<Employee> employees = new ArrayList<>();
                while (rs.next()) {
                    employees.add(mapResultSetToEmployee(rs));
                }
                return employees;
            });
        } catch (SQLException e) {
            System.err.println("Error searching employees by name: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public int getTotalEmployeeCount() {
        String sql = "SELECT COUNT(*) as count FROM employees WHERE is_active = 1";
        try {
            return dbConnection.executeQuery(sql, stmt -> {
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return rs.getInt("count");
                }
                return 0;
            });
        } catch (SQLException e) {
            System.err.println("Error getting employee count: " + e.getMessage());
            return 0;
        }
    }

    public double getAverageSalary() {
        String sql = "SELECT AVG(salary) as avg_salary FROM employees WHERE is_active = 1";
        try {
            return dbConnection.executeQuery(sql, stmt -> {
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return rs.getDouble("avg_salary");
                }
                return 0.0;
            });
        } catch (SQLException e) {
            System.err.println("Error getting average salary: " + e.getMessage());
            return 0.0;
        }
    }

    private Employee mapResultSetToEmployee(ResultSet rs) throws SQLException {
        Employee employee = new Employee();
        employee.setId(rs.getInt("employee_id"));
        employee.setUserId(rs.getInt("user_id"));
        employee.setFirstName(rs.getString("first_name"));
        employee.setLastName(rs.getString("last_name"));
        employee.setEmail(rs.getString("email"));
        employee.setPhone(rs.getString("phone"));
        employee.setDepartment(rs.getString("department"));
        employee.setPosition(rs.getString("position"));
        employee.setHireDate(rs.getObject("hire_date", LocalDate.class));
        employee.setSalary(rs.getDouble("salary"));
        employee.setActive(rs.getBoolean("is_active"));
        employee.setCreatedAt(rs.getObject("created_at", LocalDateTime.class));
        employee.setUpdatedAt(rs.getObject("updated_at", LocalDateTime.class));
        return employee;
    }
}
