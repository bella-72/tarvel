package com.travelagency.dao;

import com.travelagency.model.Flight;
import com.travelagency.util.IsochronicMarker;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Flight Data Access Object
 * Manages persistence operations for Flight entities
 */
public class FlightDAO extends GenericRepository<Flight> implements IsochronicMarker {

    public FlightDAO() {
        super(Flight.class);
    }

    @Override
    public boolean create(Flight flight) {
        String sql = "INSERT INTO flights (package_id, airline_name, flight_number, departure_city, arrival_city, departure_time, arrival_time, aircraft_type, total_seats, available_seats, price_per_seat, is_active) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            dbConnection.executeUpdate(sql, stmt -> {
                stmt.setInt(1, flight.getPackageId());
                stmt.setString(2, flight.getAirlineName());
                stmt.setString(3, flight.getFlightNumber());
                stmt.setString(4, flight.getDepartureCity());
                stmt.setString(5, flight.getArrivalCity());
                stmt.setObject(6, flight.getDepartureTime());
                stmt.setObject(7, flight.getArrivalTime());
                stmt.setString(8, flight.getAircraftType());
                stmt.setInt(9, flight.getTotalSeats());
                stmt.setInt(10, flight.getAvailableSeats());
                stmt.setDouble(11, flight.getPricePerSeat());
                stmt.setBoolean(12, true);
            });
            return true;
        } catch (SQLException e) {
            System.err.println("Error creating flight: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Flight readById(int id) {
        String sql = "SELECT * FROM flights WHERE flight_id = ?";
        try {
            return dbConnection.executeQuery(sql, stmt -> {
                stmt.setInt(1, id);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return mapResultSetToFlight(rs);
                }
                return null;
            });
        } catch (SQLException e) {
            System.err.println("Error reading flight: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Flight> readAll() {
        String sql = "SELECT * FROM flights WHERE is_active = 1 ORDER BY departure_time ASC";
        return executeQuery(sql, this::mapResultSetToFlight);
    }

    @Override
    public boolean update(Flight flight) {
        String sql = "UPDATE flights SET package_id = ?, airline_name = ?, flight_number = ?, departure_city = ?, arrival_city = ?, departure_time = ?, arrival_time = ?, aircraft_type = ?, total_seats = ?, available_seats = ?, price_per_seat = ?, updated_at = ? WHERE flight_id = ?";
        try {
            dbConnection.executeUpdate(sql, stmt -> {
                stmt.setInt(1, flight.getPackageId());
                stmt.setString(2, flight.getAirlineName());
                stmt.setString(3, flight.getFlightNumber());
                stmt.setString(4, flight.getDepartureCity());
                stmt.setString(5, flight.getArrivalCity());
                stmt.setObject(6, flight.getDepartureTime());
                stmt.setObject(7, flight.getArrivalTime());
                stmt.setString(8, flight.getAircraftType());
                stmt.setInt(9, flight.getTotalSeats());
                stmt.setInt(10, flight.getAvailableSeats());
                stmt.setDouble(11, flight.getPricePerSeat());
                stmt.setObject(12, LocalDateTime.now());
                stmt.setInt(13, flight.getId());
            });
            return true;
        } catch (SQLException e) {
            System.err.println("Error updating flight: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "UPDATE flights SET is_active = 0 WHERE flight_id = ?";
        try {
            dbConnection.executeUpdate(sql, stmt -> stmt.setInt(1, id));
            return true;
        } catch (SQLException e) {
            System.err.println("Error deleting flight: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean exists(int id) {
        String sql = "SELECT COUNT(*) FROM flights WHERE flight_id = ?";
        try {
            return dbConnection.executeQuery(sql, stmt -> {
                stmt.setInt(1, id);
                ResultSet rs = stmt.executeQuery();
                return rs.next() && rs.getInt(1) > 0;
            });
        } catch (SQLException e) {
            System.err.println("Error checking flight existence: " + e.getMessage());
            return false;
        }
    }

    public List<Flight> getByPackageId(int packageId) {
        String sql = "SELECT * FROM flights WHERE package_id = ? AND is_active = 1 ORDER BY departure_time ASC";
        try {
            return dbConnection.executeQuery(sql, stmt -> {
                stmt.setInt(1, packageId);
                ResultSet rs = stmt.executeQuery();
                List<Flight> flights = new ArrayList<>();
                while (rs.next()) {
                    flights.add(mapResultSetToFlight(rs));
                }
                return flights;
            });
        } catch (SQLException e) {
            System.err.println("Error getting flights by package: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Flight> getByAirline(String airline) {
        String sql = "SELECT * FROM flights WHERE airline_name = ? AND is_active = 1 ORDER BY departure_time ASC";
        try {
            return dbConnection.executeQuery(sql, stmt -> {
                stmt.setString(1, airline);
                ResultSet rs = stmt.executeQuery();
                List<Flight> flights = new ArrayList<>();
                while (rs.next()) {
                    flights.add(mapResultSetToFlight(rs));
                }
                return flights;
            });
        } catch (SQLException e) {
            System.err.println("Error getting flights by airline: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Flight> getFlightsDepartingAfter(LocalDateTime time) {
        String sql = "SELECT * FROM flights WHERE departure_time > ? AND is_active = 1 ORDER BY departure_time ASC";
        try {
            return dbConnection.executeQuery(sql, stmt -> {
                stmt.setObject(1, time);
                ResultSet rs = stmt.executeQuery();
                List<Flight> flights = new ArrayList<>();
                while (rs.next()) {
                    flights.add(mapResultSetToFlight(rs));
                }
                return flights;
            });
        } catch (SQLException e) {
            System.err.println("Error getting departing flights: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Flight> getAvailableFlights() {
        String sql = "SELECT * FROM flights WHERE available_seats > 0 AND is_active = 1 ORDER BY departure_time ASC";
        return executeQuery(sql, this::mapResultSetToFlight);
    }

    private Flight mapResultSetToFlight(ResultSet rs) throws SQLException {
        Flight flight = new Flight();
        flight.setId(rs.getInt("flight_id"));
        flight.setPackageId(rs.getInt("package_id"));
        flight.setAirlineName(rs.getString("airline_name"));
        flight.setFlightNumber(rs.getString("flight_number"));
        flight.setDepartureCity(rs.getString("departure_city"));
        flight.setArrivalCity(rs.getString("arrival_city"));
        flight.setDepartureTime(rs.getObject("departure_time", LocalDateTime.class));
        flight.setArrivalTime(rs.getObject("arrival_time", LocalDateTime.class));
        flight.setAircraftType(rs.getString("aircraft_type"));
        flight.setTotalSeats(rs.getInt("total_seats"));
        flight.setAvailableSeats(rs.getInt("available_seats"));
        flight.setPricePerSeat(rs.getDouble("price_per_seat"));
        flight.setActive(rs.getBoolean("is_active"));
        flight.setCreatedAt(rs.getObject("created_at", LocalDateTime.class));
        flight.setUpdatedAt(rs.getObject("updated_at", LocalDateTime.class));
        return flight;
    }
}
