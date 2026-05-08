package com.travelagency.dao;

import com.travelagency.model.Reservation;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Reservation Data Access Object
 */
public class ReservationDAO extends GenericRepository<Reservation> {

    public ReservationDAO() {
        super(Reservation.class);
    }

    @Override
    public boolean create(Reservation reservation) {
        String sql = "INSERT INTO reservations (customer_id, package_id, flight_id, room_id, reservation_date, number_of_travelers, total_price, status, special_requirements) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            dbConnection.executeUpdate(sql, stmt -> {
                stmt.setInt(1, reservation.getCustomerId());
                stmt.setInt(2, reservation.getPackageId());
                if (reservation.getFlightId() != null) {
                    stmt.setInt(3, reservation.getFlightId());
                } else {
                    stmt.setNull(3, java.sql.Types.INTEGER);
                }
                if (reservation.getRoomId() != null) {
                    stmt.setInt(4, reservation.getRoomId());
                } else {
                    stmt.setNull(4, java.sql.Types.INTEGER);
                }
                stmt.setString(5, reservation.getReservationDate().toString());
                stmt.setInt(6, reservation.getNumberOfTravelers());
                stmt.setDouble(7, reservation.getTotalPrice());
                stmt.setString(8, reservation.getStatus().toString());
                stmt.setString(9, reservation.getSpecialRequirements());
            });
            return true;
        } catch (SQLException e) {
            System.err.println("Error creating reservation: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Reservation readById(int id) {
        String sql = "SELECT * FROM reservations WHERE reservation_id = ?";
        try {
            return dbConnection.executeQuery(sql, stmt -> {
                stmt.setInt(1, id);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return mapResultSetToReservation(rs);
                }
                return null;
            });
        } catch (SQLException e) {
            System.err.println("Error reading reservation: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Reservation> readAll() {
        String sql = "SELECT * FROM reservations ORDER BY created_at DESC";
        return executeQuery(sql, this::mapResultSetToReservation);
    }

    @Override
    public boolean update(Reservation reservation) {
        String sql = "UPDATE reservations SET customer_id = ?, package_id = ?, flight_id = ?, room_id = ?, reservation_date = ?, number_of_travelers = ?, total_price = ?, status = ?, special_requirements = ?, updated_at = ? WHERE reservation_id = ?";
        try {
            dbConnection.executeUpdate(sql, stmt -> {
                stmt.setInt(1, reservation.getCustomerId());
                stmt.setInt(2, reservation.getPackageId());
                if (reservation.getFlightId() != null) {
                    stmt.setInt(3, reservation.getFlightId());
                } else {
                    stmt.setNull(3, java.sql.Types.INTEGER);
                }
                if (reservation.getRoomId() != null) {
                    stmt.setInt(4, reservation.getRoomId());
                } else {
                    stmt.setNull(4, java.sql.Types.INTEGER);
                }
                stmt.setString(5, reservation.getReservationDate().toString());
                stmt.setInt(6, reservation.getNumberOfTravelers());
                stmt.setDouble(7, reservation.getTotalPrice());
                stmt.setString(8, reservation.getStatus().toString());
                stmt.setString(9, reservation.getSpecialRequirements());
                stmt.setString(10, LocalDateTime.now().toString());
                stmt.setInt(11, reservation.getId());
            });
            return true;
        } catch (SQLException e) {
            System.err.println("Error updating reservation: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM reservations WHERE reservation_id = ?";
        try {
            dbConnection.executeUpdate(sql, stmt -> stmt.setInt(1, id));
            return true;
        } catch (SQLException e) {
            System.err.println("Error deleting reservation: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean exists(int id) {
        String sql = "SELECT COUNT(*) FROM reservations WHERE reservation_id = ?";
        try {
            return dbConnection.executeQuery(sql, stmt -> {
                stmt.setInt(1, id);
                ResultSet rs = stmt.executeQuery();
                return rs.next() && rs.getInt(1) > 0;
            });
        } catch (SQLException e) {
            System.err.println("Error checking reservation existence: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get reservations by customer ID
     */
    public List<Reservation> getByCustomerId(int customerId) {
        String sql = "SELECT * FROM reservations WHERE customer_id = ? ORDER BY reservation_date DESC";
        try {
            return dbConnection.executeQuery(sql, stmt -> {
                stmt.setInt(1, customerId);
                ResultSet rs = stmt.executeQuery();
                List<Reservation> reservations = new java.util.ArrayList<>();
                while (rs.next()) {
                    reservations.add(mapResultSetToReservation(rs));
                }
                return reservations;
            });
        } catch (SQLException e) {
            System.err.println("Error getting reservations by customer: " + e.getMessage());
            return java.util.Collections.emptyList();
        }
    }

    /**
     * Get confirmed reservations
     */
    public List<Reservation> getConfirmedReservations() {
        String sql = "SELECT * FROM reservations WHERE status = 'CONFIRMED' ORDER BY reservation_date DESC";
        return executeQuery(sql, this::mapResultSetToReservation);
    }

    /**
     * Get pending reservations
     */
    public List<Reservation> getPendingReservations() {
        String sql = "SELECT * FROM reservations WHERE status = 'PENDING' ORDER BY reservation_date DESC";
        return executeQuery(sql, this::mapResultSetToReservation);
    }

    /**
     * Map ResultSet to Reservation object
     */
    private Reservation mapResultSetToReservation(ResultSet rs) throws SQLException {
        Reservation reservation = new Reservation();
        reservation.setId(rs.getInt("reservation_id"));
        reservation.setCustomerId(rs.getInt("customer_id"));
        reservation.setPackageId(rs.getInt("package_id"));
        reservation.setFlightId(rs.getObject("flight_id") != null ? rs.getInt("flight_id") : null);
        reservation.setRoomId(rs.getObject("room_id") != null ? rs.getInt("room_id") : null);
        reservation.setReservationDate(LocalDate.parse(rs.getString("reservation_date")));
        reservation.setNumberOfTravelers(rs.getInt("number_of_travelers"));
        reservation.setTotalPrice(rs.getDouble("total_price"));
        reservation.setStatus(Reservation.ReservationStatus.valueOf(rs.getString("status")));
        reservation.setSpecialRequirements(rs.getString("special_requirements"));
        return reservation;
    }
}
