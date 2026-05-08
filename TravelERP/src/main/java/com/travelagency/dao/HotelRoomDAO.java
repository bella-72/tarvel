package com.travelagency.dao;

import com.travelagency.model.HotelRoom;
import com.travelagency.util.IsochronicMarker;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Hotel Room Data Access Object
 * Manages persistence operations for HotelRoom entities
 */
public class HotelRoomDAO extends GenericRepository<HotelRoom> implements IsochronicMarker {

    public HotelRoomDAO() {
        super(HotelRoom.class);
    }

    @Override
    public boolean create(HotelRoom room) {
        String sql = "INSERT INTO hotel_rooms (hotel_id, room_number, room_type, capacity, price_per_night, is_occupied, is_active) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            dbConnection.executeUpdate(sql, stmt -> {
                stmt.setInt(1, room.getHotelId());
                stmt.setString(2, room.getRoomNumber());
                stmt.setString(3, room.getRoomType().toString());
                stmt.setInt(4, room.getCapacity());
                stmt.setDouble(5, room.getPricePerNight());
                stmt.setBoolean(6, room.isOccupied());
                stmt.setBoolean(7, true);
            });
            return true;
        } catch (SQLException e) {
            System.err.println("Error creating hotel room: " + e.getMessage());
            return false;
        }
    }

    @Override
    public HotelRoom readById(int id) {
        String sql = "SELECT * FROM hotel_rooms WHERE room_id = ?";
        try {
            return dbConnection.executeQuery(sql, stmt -> {
                stmt.setInt(1, id);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return mapResultSetToRoom(rs);
                }
                return null;
            });
        } catch (SQLException e) {
            System.err.println("Error reading hotel room: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<HotelRoom> readAll() {
        String sql = "SELECT * FROM hotel_rooms WHERE is_active = 1 ORDER BY room_number ASC";
        return executeQuery(sql, this::mapResultSetToRoom);
    }

    @Override
    public boolean update(HotelRoom room) {
        String sql = "UPDATE hotel_rooms SET hotel_id = ?, room_number = ?, room_type = ?, capacity = ?, price_per_night = ?, is_occupied = ?, updated_at = ? WHERE room_id = ?";
        try {
            dbConnection.executeUpdate(sql, stmt -> {
                stmt.setInt(1, room.getHotelId());
                stmt.setString(2, room.getRoomNumber());
                stmt.setString(3, room.getRoomType().toString());
                stmt.setInt(4, room.getCapacity());
                stmt.setDouble(5, room.getPricePerNight());
                stmt.setBoolean(6, room.isOccupied());
                stmt.setObject(7, LocalDateTime.now());
                stmt.setInt(8, room.getId());
            });
            return true;
        } catch (SQLException e) {
            System.err.println("Error updating hotel room: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "UPDATE hotel_rooms SET is_active = 0 WHERE room_id = ?";
        try {
            dbConnection.executeUpdate(sql, stmt -> stmt.setInt(1, id));
            return true;
        } catch (SQLException e) {
            System.err.println("Error deleting hotel room: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean exists(int id) {
        String sql = "SELECT COUNT(*) FROM hotel_rooms WHERE room_id = ?";
        try {
            return dbConnection.executeQuery(sql, stmt -> {
                stmt.setInt(1, id);
                ResultSet rs = stmt.executeQuery();
                return rs.next() && rs.getInt(1) > 0;
            });
        } catch (SQLException e) {
            System.err.println("Error checking hotel room existence: " + e.getMessage());
            return false;
        }
    }

    public List<HotelRoom> getByHotelId(int hotelId) {
        String sql = "SELECT * FROM hotel_rooms WHERE hotel_id = ? AND is_active = 1 ORDER BY room_number ASC";
        try {
            return dbConnection.executeQuery(sql, stmt -> {
                stmt.setInt(1, hotelId);
                ResultSet rs = stmt.executeQuery();
                List<HotelRoom> rooms = new ArrayList<>();
                while (rs.next()) {
                    rooms.add(mapResultSetToRoom(rs));
                }
                return rooms;
            });
        } catch (SQLException e) {
            System.err.println("Error getting rooms by hotel: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<HotelRoom> getAvailableRooms(int hotelId) {
        String sql = "SELECT * FROM hotel_rooms WHERE hotel_id = ? AND is_occupied = 0 AND is_active = 1 ORDER BY room_number ASC";
        try {
            return dbConnection.executeQuery(sql, stmt -> {
                stmt.setInt(1, hotelId);
                ResultSet rs = stmt.executeQuery();
                List<HotelRoom> rooms = new ArrayList<>();
                while (rs.next()) {
                    rooms.add(mapResultSetToRoom(rs));
                }
                return rooms;
            });
        } catch (SQLException e) {
            System.err.println("Error getting available rooms: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<HotelRoom> getRoomsByType(int hotelId, String roomType) {
        String sql = "SELECT * FROM hotel_rooms WHERE hotel_id = ? AND room_type = ? AND is_active = 1 ORDER BY room_number ASC";
        try {
            return dbConnection.executeQuery(sql, stmt -> {
                stmt.setInt(1, hotelId);
                stmt.setString(2, roomType);
                ResultSet rs = stmt.executeQuery();
                List<HotelRoom> rooms = new ArrayList<>();
                while (rs.next()) {
                    rooms.add(mapResultSetToRoom(rs));
                }
                return rooms;
            });
        } catch (SQLException e) {
            System.err.println("Error getting rooms by type: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<HotelRoom> getRoomsByPriceRange(int hotelId, double minPrice, double maxPrice) {
        String sql = "SELECT * FROM hotel_rooms WHERE hotel_id = ? AND price_per_night BETWEEN ? AND ? AND is_active = 1 ORDER BY price_per_night ASC";
        try {
            return dbConnection.executeQuery(sql, stmt -> {
                stmt.setInt(1, hotelId);
                stmt.setDouble(2, minPrice);
                stmt.setDouble(3, maxPrice);
                ResultSet rs = stmt.executeQuery();
                List<HotelRoom> rooms = new ArrayList<>();
                while (rs.next()) {
                    rooms.add(mapResultSetToRoom(rs));
                }
                return rooms;
            });
        } catch (SQLException e) {
            System.err.println("Error getting rooms by price range: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<HotelRoom> getRoomsByOccupancyCapacity(int hotelId, int occupancy) {
        String sql = "SELECT * FROM hotel_rooms WHERE hotel_id = ? AND capacity >= ? AND is_active = 1 ORDER BY room_number ASC";
        try {
            return dbConnection.executeQuery(sql, stmt -> {
                stmt.setInt(1, hotelId);
                stmt.setInt(2, occupancy);
                ResultSet rs = stmt.executeQuery();
                List<HotelRoom> rooms = new ArrayList<>();
                while (rs.next()) {
                    rooms.add(mapResultSetToRoom(rs));
                }
                return rooms;
            });
        } catch (SQLException e) {
            System.err.println("Error getting rooms by occupancy: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public int getAvailableRoomCount(int hotelId) {
        String sql = "SELECT COUNT(*) as count FROM hotel_rooms WHERE hotel_id = ? AND is_occupied = 0 AND is_active = 1";
        try {
            return dbConnection.executeQuery(sql, stmt -> {
                stmt.setInt(1, hotelId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return rs.getInt("count");
                }
                return 0;
            });
        } catch (SQLException e) {
            System.err.println("Error getting available room count: " + e.getMessage());
            return 0;
        }
    }

    private HotelRoom mapResultSetToRoom(ResultSet rs) throws SQLException {
        HotelRoom room = new HotelRoom();
        room.setId(rs.getInt("room_id"));
        room.setHotelId(rs.getInt("hotel_id"));
        room.setRoomNumber(rs.getString("room_number"));
        room.setRoomType(HotelRoom.RoomType.valueOf(rs.getString("room_type")));
        room.setCapacity(rs.getInt("capacity"));
        room.setPricePerNight(rs.getDouble("price_per_night"));
        room.setOccupied(rs.getBoolean("is_occupied"));
        room.setActive(rs.getBoolean("is_active"));
        room.setCreatedAt(rs.getObject("created_at", LocalDateTime.class));
        room.setUpdatedAt(rs.getObject("updated_at", LocalDateTime.class));
        return room;
    }
}
