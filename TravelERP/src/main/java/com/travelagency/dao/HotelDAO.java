package com.travelagency.dao;

import com.travelagency.model.Hotel;
import com.travelagency.util.IsochronicMarker;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Hotel Data Access Object
 * Manages persistence operations for Hotel entities
 */
public class HotelDAO extends GenericRepository<Hotel> implements IsochronicMarker {

    public HotelDAO() {
        super(Hotel.class);
    }

    @Override
    public boolean create(Hotel hotel) {
        String sql = "INSERT INTO hotels (package_id, hotel_name, city, address, rating, check_in_date, check_out_date, price_per_night, total_rooms, is_active) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            dbConnection.executeUpdate(sql, stmt -> {
                stmt.setInt(1, hotel.getPackageId());
                stmt.setString(2, hotel.getHotelName());
                stmt.setString(3, hotel.getCity());
                stmt.setString(4, hotel.getAddress());
                stmt.setDouble(5, hotel.getRating());
                stmt.setObject(6, hotel.getCheckInDate());
                stmt.setObject(7, hotel.getCheckOutDate());
                stmt.setDouble(8, hotel.getPricePerNight());
                stmt.setInt(9, hotel.getTotalRooms());
                stmt.setBoolean(10, true);
            });
            return true;
        } catch (SQLException e) {
            System.err.println("Error creating hotel: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Hotel readById(int id) {
        String sql = "SELECT * FROM hotels WHERE hotel_id = ?";
        try {
            return dbConnection.executeQuery(sql, stmt -> {
                stmt.setInt(1, id);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return mapResultSetToHotel(rs);
                }
                return null;
            });
        } catch (SQLException e) {
            System.err.println("Error reading hotel: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Hotel> readAll() {
        String sql = "SELECT * FROM hotels WHERE is_active = 1 ORDER BY rating DESC";
        return executeQuery(sql, this::mapResultSetToHotel);
    }

    @Override
    public boolean update(Hotel hotel) {
        String sql = "UPDATE hotels SET package_id = ?, hotel_name = ?, city = ?, address = ?, rating = ?, check_in_date = ?, check_out_date = ?, price_per_night = ?, total_rooms = ?, updated_at = ? WHERE hotel_id = ?";
        try {
            dbConnection.executeUpdate(sql, stmt -> {
                stmt.setInt(1, hotel.getPackageId());
                stmt.setString(2, hotel.getHotelName());
                stmt.setString(3, hotel.getCity());
                stmt.setString(4, hotel.getAddress());
                stmt.setDouble(5, hotel.getRating());
                stmt.setObject(6, hotel.getCheckInDate());
                stmt.setObject(7, hotel.getCheckOutDate());
                stmt.setDouble(8, hotel.getPricePerNight());
                stmt.setInt(9, hotel.getTotalRooms());
                stmt.setObject(10, LocalDateTime.now());
                stmt.setInt(11, hotel.getId());
            });
            return true;
        } catch (SQLException e) {
            System.err.println("Error updating hotel: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "UPDATE hotels SET is_active = 0 WHERE hotel_id = ?";
        try {
            dbConnection.executeUpdate(sql, stmt -> stmt.setInt(1, id));
            return true;
        } catch (SQLException e) {
            System.err.println("Error deleting hotel: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean exists(int id) {
        String sql = "SELECT COUNT(*) FROM hotels WHERE hotel_id = ?";
        try {
            return dbConnection.executeQuery(sql, stmt -> {
                stmt.setInt(1, id);
                ResultSet rs = stmt.executeQuery();
                return rs.next() && rs.getInt(1) > 0;
            });
        } catch (SQLException e) {
            System.err.println("Error checking hotel existence: " + e.getMessage());
            return false;
        }
    }

    public List<Hotel> getByPackageId(int packageId) {
        String sql = "SELECT * FROM hotels WHERE package_id = ? AND is_active = 1 ORDER BY rating DESC";
        try {
            return dbConnection.executeQuery(sql, stmt -> {
                stmt.setInt(1, packageId);
                ResultSet rs = stmt.executeQuery();
                List<Hotel> hotels = new ArrayList<>();
                while (rs.next()) {
                    hotels.add(mapResultSetToHotel(rs));
                }
                return hotels;
            });
        } catch (SQLException e) {
            System.err.println("Error getting hotels by package: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Hotel> getByCity(String city) {
        String sql = "SELECT * FROM hotels WHERE city = ? AND is_active = 1 ORDER BY rating DESC";
        try {
            return dbConnection.executeQuery(sql, stmt -> {
                stmt.setString(1, city);
                ResultSet rs = stmt.executeQuery();
                List<Hotel> hotels = new ArrayList<>();
                while (rs.next()) {
                    hotels.add(mapResultSetToHotel(rs));
                }
                return hotels;
            });
        } catch (SQLException e) {
            System.err.println("Error getting hotels by city: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Hotel> getByMinimumRating(double minRating) {
        String sql = "SELECT * FROM hotels WHERE rating >= ? AND is_active = 1 ORDER BY rating DESC";
        try {
            return dbConnection.executeQuery(sql, stmt -> {
                stmt.setDouble(1, minRating);
                ResultSet rs = stmt.executeQuery();
                List<Hotel> hotels = new ArrayList<>();
                while (rs.next()) {
                    hotels.add(mapResultSetToHotel(rs));
                }
                return hotels;
            });
        } catch (SQLException e) {
            System.err.println("Error getting hotels by rating: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Hotel> getAvailableHotelsForDates(LocalDate checkIn, LocalDate checkOut) {
        String sql = "SELECT * FROM hotels WHERE check_in_date <= ? AND check_out_date >= ? AND is_active = 1 ORDER BY rating DESC";
        try {
            return dbConnection.executeQuery(sql, stmt -> {
                stmt.setObject(1, checkOut);
                stmt.setObject(2, checkIn);
                ResultSet rs = stmt.executeQuery();
                List<Hotel> hotels = new ArrayList<>();
                while (rs.next()) {
                    hotels.add(mapResultSetToHotel(rs));
                }
                return hotels;
            });
        } catch (SQLException e) {
            System.err.println("Error getting available hotels: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Hotel> getHotelsByPriceRange(double minPrice, double maxPrice) {
        String sql = "SELECT * FROM hotels WHERE price_per_night BETWEEN ? AND ? AND is_active = 1 ORDER BY price_per_night ASC";
        try {
            return dbConnection.executeQuery(sql, stmt -> {
                stmt.setDouble(1, minPrice);
                stmt.setDouble(2, maxPrice);
                ResultSet rs = stmt.executeQuery();
                List<Hotel> hotels = new ArrayList<>();
                while (rs.next()) {
                    hotels.add(mapResultSetToHotel(rs));
                }
                return hotels;
            });
        } catch (SQLException e) {
            System.err.println("Error getting hotels by price range: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private Hotel mapResultSetToHotel(ResultSet rs) throws SQLException {
        Hotel hotel = new Hotel();
        hotel.setId(rs.getInt("hotel_id"));
        hotel.setPackageId(rs.getInt("package_id"));
        hotel.setHotelName(rs.getString("hotel_name"));
        hotel.setCity(rs.getString("city"));
        hotel.setAddress(rs.getString("address"));
        hotel.setRating(rs.getDouble("rating"));
        hotel.setCheckInDate(rs.getObject("check_in_date", LocalDate.class));
        hotel.setCheckOutDate(rs.getObject("check_out_date", LocalDate.class));
        hotel.setPricePerNight(rs.getDouble("price_per_night"));
        hotel.setTotalRooms(rs.getInt("total_rooms"));
        hotel.setActive(rs.getBoolean("is_active"));
        hotel.setCreatedAt(rs.getObject("created_at", LocalDateTime.class));
        hotel.setUpdatedAt(rs.getObject("updated_at", LocalDateTime.class));
        return hotel;
    }
}
