package com.travelagency.dao;

import com.travelagency.model.Customer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Customer Data Access Object
 */
public class CustomerDAO extends GenericRepository<Customer> {

    public CustomerDAO() {
        super(Customer.class);
    }

    @Override
    public boolean create(Customer customer) {
        String sql = "INSERT INTO customers (first_name, last_name, email, phone, address, city, country, passport_number, date_of_birth, gender, is_active) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            dbConnection.executeUpdate(sql, stmt -> {
                stmt.setString(1, customer.getFirstName());
                stmt.setString(2, customer.getLastName());
                stmt.setString(3, customer.getEmail());
                stmt.setString(4, customer.getPhone());
                stmt.setString(5, customer.getAddress());
                stmt.setString(6, customer.getCity());
                stmt.setString(7, customer.getCountry());
                stmt.setString(8, customer.getPassportNumber());
                stmt.setString(9, customer.getDateOfBirth().toString());
                stmt.setString(10, customer.getGender());
                stmt.setBoolean(11, customer.isActive());
            });
            return true;
        } catch (SQLException e) {
            System.err.println("Error creating customer: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Customer readById(int id) {
        String sql = "SELECT * FROM customers WHERE customer_id = ?";
        try {
            return dbConnection.executeQuery(sql, stmt -> {
                stmt.setInt(1, id);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return mapResultSetToCustomer(rs);
                }
                return null;
            });
        } catch (SQLException e) {
            System.err.println("Error reading customer: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Customer> readAll() {
        String sql = "SELECT * FROM customers WHERE is_active = 1 ORDER BY created_at DESC";
        return executeQuery(sql, this::mapResultSetToCustomer);
    }

    @Override
    public boolean update(Customer customer) {
        String sql = "UPDATE customers SET first_name = ?, last_name = ?, email = ?, phone = ?, address = ?, city = ?, country = ?, passport_number = ?, date_of_birth = ?, gender = ?, is_active = ?, updated_at = ? WHERE customer_id = ?";
        try {
            dbConnection.executeUpdate(sql, stmt -> {
                stmt.setString(1, customer.getFirstName());
                stmt.setString(2, customer.getLastName());
                stmt.setString(3, customer.getEmail());
                stmt.setString(4, customer.getPhone());
                stmt.setString(5, customer.getAddress());
                stmt.setString(6, customer.getCity());
                stmt.setString(7, customer.getCountry());
                stmt.setString(8, customer.getPassportNumber());
                stmt.setString(9, customer.getDateOfBirth().toString());
                stmt.setString(10, customer.getGender());
                stmt.setBoolean(11, customer.isActive());
                stmt.setString(12, LocalDateTime.now().toString());
                stmt.setInt(13, customer.getId());
            });
            return true;
        } catch (SQLException e) {
            System.err.println("Error updating customer: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "UPDATE customers SET is_active = 0 WHERE customer_id = ?";
        try {
            dbConnection.executeUpdate(sql, stmt -> stmt.setInt(1, id));
            return true;
        } catch (SQLException e) {
            System.err.println("Error deleting customer: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean exists(int id) {
        String sql = "SELECT COUNT(*) FROM customers WHERE customer_id = ?";
        try {
            return dbConnection.executeQuery(sql, stmt -> {
                stmt.setInt(1, id);
                ResultSet rs = stmt.executeQuery();
                return rs.next() && rs.getInt(1) > 0;
            });
        } catch (SQLException e) {
            System.err.println("Error checking customer existence: " + e.getMessage());
            return false;
        }
    }

    /**
     * Find customer by email
     */
    public Optional<Customer> findByEmail(String email) {
        String sql = "SELECT * FROM customers WHERE email = ?";
        try {
            Customer customer = dbConnection.executeQuery(sql, stmt -> {
                stmt.setString(1, email);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return mapResultSetToCustomer(rs);
                }
                return null;
            });
            return Optional.ofNullable(customer);
        } catch (SQLException e) {
            System.err.println("Error finding customer by email: " + e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Check whether a customer email already exists
     */
    public boolean emailExists(String email) {
        String sql = "SELECT COUNT(*) FROM customers WHERE email = ?";
        try {
            return dbConnection.executeQuery(sql, stmt -> {
                stmt.setString(1, email);
                ResultSet rs = stmt.executeQuery();
                return rs.next() && rs.getInt(1) > 0;
            });
        } catch (SQLException e) {
            System.err.println("Error checking customer email existence: " + e.getMessage());
            return false;
        }
    }

    /**
     * Search customers by name
     */
    public List<Customer> searchByName(String searchTerm) {
        String sql = "SELECT * FROM customers WHERE (first_name LIKE ? OR last_name LIKE ? OR email LIKE ?) AND is_active = 1";
        try {
            return dbConnection.executeQuery(sql, stmt -> {
                String pattern = "%" + searchTerm + "%";
                stmt.setString(1, pattern);
                stmt.setString(2, pattern);
                stmt.setString(3, pattern);
                ResultSet rs = stmt.executeQuery();
                List<Customer> customers = new java.util.ArrayList<>();
                while (rs.next()) {
                    customers.add(mapResultSetToCustomer(rs));
                }
                return customers;
            });
        } catch (SQLException e) {
            System.err.println("Error searching customers: " + e.getMessage());
            return java.util.Collections.emptyList();
        }
    }

    /**
     * Get customers by country
     */
    public List<Customer> getByCountry(String country) {
        String sql = "SELECT * FROM customers WHERE country = ? AND is_active = 1 ORDER BY created_at DESC";
        try {
            return dbConnection.executeQuery(sql, stmt -> {
                stmt.setString(1, country);
                ResultSet rs = stmt.executeQuery();
                List<Customer> customers = new java.util.ArrayList<>();
                while (rs.next()) {
                    customers.add(mapResultSetToCustomer(rs));
                }
                return customers;
            });
        } catch (SQLException e) {
            System.err.println("Error getting customers by country: " + e.getMessage());
            return java.util.Collections.emptyList();
        }
    }

    /**
     * Map ResultSet to Customer object
     */
    private Customer mapResultSetToCustomer(ResultSet rs) throws SQLException {
        Customer customer = new Customer();
        customer.setId(rs.getInt("customer_id"));
        customer.setFirstName(rs.getString("first_name"));
        customer.setLastName(rs.getString("last_name"));
        customer.setEmail(rs.getString("email"));
        customer.setPhone(rs.getString("phone"));
        customer.setAddress(rs.getString("address"));
        customer.setCity(rs.getString("city"));
        customer.setCountry(rs.getString("country"));
        customer.setPassportNumber(rs.getString("passport_number"));
        customer.setDateOfBirth(LocalDate.parse(rs.getString("date_of_birth")));
        customer.setGender(rs.getString("gender"));
        customer.setActive(rs.getBoolean("is_active"));
        return customer;
    }
}
