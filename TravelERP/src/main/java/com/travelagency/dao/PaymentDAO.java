package com.travelagency.dao;

import com.travelagency.model.Payment;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Payment Data Access Object
 */
public class PaymentDAO extends GenericRepository<Payment> {

    public PaymentDAO() {
        super(Payment.class);
    }

    @Override
    public boolean create(Payment payment) {
        String sql = "INSERT INTO payments (reservation_id, amount, payment_method, payment_status, transaction_date, transaction_id, notes) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            dbConnection.executeUpdate(sql, stmt -> {
                stmt.setInt(1, payment.getReservationId());
                stmt.setDouble(2, payment.getAmount());
                stmt.setString(3, payment.getPaymentMethod().toString());
                stmt.setString(4, payment.getPaymentStatus().toString());
                stmt.setString(5, payment.getTransactionDate().toString());
                stmt.setString(6, payment.getTransactionId());
                stmt.setString(7, payment.getNotes());
            });
            return true;
        } catch (SQLException e) {
            System.err.println("Error creating payment: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Payment readById(int id) {
        String sql = "SELECT * FROM payments WHERE payment_id = ?";
        try {
            return dbConnection.executeQuery(sql, stmt -> {
                stmt.setInt(1, id);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return mapResultSetToPayment(rs);
                }
                return null;
            });
        } catch (SQLException e) {
            System.err.println("Error reading payment: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Payment> readAll() {
        String sql = "SELECT * FROM payments ORDER BY transaction_date DESC";
        return executeQuery(sql, this::mapResultSetToPayment);
    }

    @Override
    public boolean update(Payment payment) {
        String sql = "UPDATE payments SET reservation_id = ?, amount = ?, payment_method = ?, payment_status = ?, transaction_date = ?, transaction_id = ?, notes = ?, updated_at = ? WHERE payment_id = ?";
        try {
            dbConnection.executeUpdate(sql, stmt -> {
                stmt.setInt(1, payment.getReservationId());
                stmt.setDouble(2, payment.getAmount());
                stmt.setString(3, payment.getPaymentMethod().toString());
                stmt.setString(4, payment.getPaymentStatus().toString());
                stmt.setString(5, payment.getTransactionDate().toString());
                stmt.setString(6, payment.getTransactionId());
                stmt.setString(7, payment.getNotes());
                stmt.setString(8, LocalDateTime.now().toString());
                stmt.setInt(9, payment.getId());
            });
            return true;
        } catch (SQLException e) {
            System.err.println("Error updating payment: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM payments WHERE payment_id = ?";
        try {
            dbConnection.executeUpdate(sql, stmt -> stmt.setInt(1, id));
            return true;
        } catch (SQLException e) {
            System.err.println("Error deleting payment: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean exists(int id) {
        String sql = "SELECT COUNT(*) FROM payments WHERE payment_id = ?";
        try {
            return dbConnection.executeQuery(sql, stmt -> {
                stmt.setInt(1, id);
                ResultSet rs = stmt.executeQuery();
                return rs.next() && rs.getInt(1) > 0;
            });
        } catch (SQLException e) {
            System.err.println("Error checking payment existence: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get payments by reservation ID
     */
    public List<Payment> getByReservationId(int reservationId) {
        String sql = "SELECT * FROM payments WHERE reservation_id = ? ORDER BY transaction_date DESC";
        try {
            return dbConnection.executeQuery(sql, stmt -> {
                stmt.setInt(1, reservationId);
                ResultSet rs = stmt.executeQuery();
                List<Payment> payments = new java.util.ArrayList<>();
                while (rs.next()) {
                    payments.add(mapResultSetToPayment(rs));
                }
                return payments;
            });
        } catch (SQLException e) {
            System.err.println("Error getting payments by reservation: " + e.getMessage());
            return java.util.Collections.emptyList();
        }
    }

    /**
     * Get completed payments
     */
    public List<Payment> getCompletedPayments() {
        String sql = "SELECT * FROM payments WHERE payment_status = 'COMPLETED' ORDER BY transaction_date DESC";
        return executeQuery(sql, this::mapResultSetToPayment);
    }

    /**
     * Get total revenue
     */
    public double getTotalRevenue() {
        String sql = "SELECT COALESCE(SUM(amount), 0) FROM payments WHERE payment_status = 'COMPLETED'";
        try {
            return dbConnection.executeQuery(sql, stmt -> {
                ResultSet rs = stmt.executeQuery();
                return rs.next() ? rs.getDouble(1) : 0.0;
            });
        } catch (SQLException e) {
            System.err.println("Error calculating total revenue: " + e.getMessage());
            return 0.0;
        }
    }

    /**
     * Map ResultSet to Payment object
     */
    private Payment mapResultSetToPayment(ResultSet rs) throws SQLException {
        Payment payment = new Payment();
        payment.setId(rs.getInt("payment_id"));
        payment.setReservationId(rs.getInt("reservation_id"));
        payment.setAmount(rs.getDouble("amount"));
        payment.setPaymentMethod(Payment.PaymentMethod.valueOf(rs.getString("payment_method")));
        payment.setPaymentStatus(Payment.PaymentStatus.valueOf(rs.getString("payment_status")));
        payment.setTransactionDate(LocalDateTime.parse(rs.getString("transaction_date")));
        payment.setTransactionId(rs.getString("transaction_id"));
        payment.setNotes(rs.getString("notes"));
        return payment;
    }
}
