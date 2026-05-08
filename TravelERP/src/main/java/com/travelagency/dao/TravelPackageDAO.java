package com.travelagency.dao;

import com.travelagency.model.TravelPackage;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Travel Package Data Access Object
 */
public class TravelPackageDAO extends GenericRepository<TravelPackage> {

    public TravelPackageDAO() {
        super(TravelPackage.class);
    }

    @Override
    public boolean create(TravelPackage pkg) {
        String sql = "INSERT INTO travel_packages (package_name, destination, description, duration_days, price_per_person, max_capacity, available_seats, start_date, end_date, package_type, is_active) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            dbConnection.executeUpdate(sql, stmt -> {
                stmt.setString(1, pkg.getPackageName());
                stmt.setString(2, pkg.getDestination());
                stmt.setString(3, pkg.getDescription());
                stmt.setInt(4, pkg.getDurationDays());
                stmt.setDouble(5, pkg.getPricePerPerson());
                stmt.setInt(6, pkg.getMaxCapacity());
                stmt.setInt(7, pkg.getAvailableSeats());
                stmt.setString(8, pkg.getStartDate().toString());
                stmt.setString(9, pkg.getEndDate().toString());
                stmt.setString(10, pkg.getPackageType().toString());
                stmt.setBoolean(11, pkg.isActive());
            });
            return true;
        } catch (SQLException e) {
            System.err.println("Error creating travel package: " + e.getMessage());
            return false;
        }
    }

    @Override
    public TravelPackage readById(int id) {
        String sql = "SELECT * FROM travel_packages WHERE package_id = ?";
        try {
            return dbConnection.executeQuery(sql, stmt -> {
                stmt.setInt(1, id);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return mapResultSetToPackage(rs);
                }
                return null;
            });
        } catch (SQLException e) {
            System.err.println("Error reading travel package: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<TravelPackage> readAll() {
        String sql = "SELECT * FROM travel_packages WHERE is_active = 1 ORDER BY start_date ASC";
        return executeQuery(sql, this::mapResultSetToPackage);
    }

    @Override
    public boolean update(TravelPackage pkg) {
        String sql = "UPDATE travel_packages SET package_name = ?, destination = ?, description = ?, duration_days = ?, price_per_person = ?, max_capacity = ?, available_seats = ?, start_date = ?, end_date = ?, package_type = ?, is_active = ?, updated_at = ? WHERE package_id = ?";
        try {
            dbConnection.executeUpdate(sql, stmt -> {
                stmt.setString(1, pkg.getPackageName());
                stmt.setString(2, pkg.getDestination());
                stmt.setString(3, pkg.getDescription());
                stmt.setInt(4, pkg.getDurationDays());
                stmt.setDouble(5, pkg.getPricePerPerson());
                stmt.setInt(6, pkg.getMaxCapacity());
                stmt.setInt(7, pkg.getAvailableSeats());
                stmt.setString(8, pkg.getStartDate().toString());
                stmt.setString(9, pkg.getEndDate().toString());
                stmt.setString(10, pkg.getPackageType().toString());
                stmt.setBoolean(11, pkg.isActive());
                stmt.setString(12, LocalDateTime.now().toString());
                stmt.setInt(13, pkg.getId());
            });
            return true;
        } catch (SQLException e) {
            System.err.println("Error updating travel package: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "UPDATE travel_packages SET is_active = 0 WHERE package_id = ?";
        try {
            dbConnection.executeUpdate(sql, stmt -> stmt.setInt(1, id));
            return true;
        } catch (SQLException e) {
            System.err.println("Error deleting travel package: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean exists(int id) {
        String sql = "SELECT COUNT(*) FROM travel_packages WHERE package_id = ?";
        try {
            return dbConnection.executeQuery(sql, stmt -> {
                stmt.setInt(1, id);
                ResultSet rs = stmt.executeQuery();
                return rs.next() && rs.getInt(1) > 0;
            });
        } catch (SQLException e) {
            System.err.println("Error checking package existence: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get packages by destination
     */
    public List<TravelPackage> getByDestination(String destination) {
        String sql = "SELECT * FROM travel_packages WHERE destination = ? AND is_active = 1 ORDER BY start_date ASC";
        try {
            return dbConnection.executeQuery(sql, stmt -> {
                stmt.setString(1, destination);
                ResultSet rs = stmt.executeQuery();
                List<TravelPackage> packages = new java.util.ArrayList<>();
                while (rs.next()) {
                    packages.add(mapResultSetToPackage(rs));
                }
                return packages;
            });
        } catch (SQLException e) {
            System.err.println("Error getting packages by destination: " + e.getMessage());
            return java.util.Collections.emptyList();
        }
    }

    /**
     * Get packages with available seats
     */
    public List<TravelPackage> getAvailablePackages() {
        String sql = "SELECT * FROM travel_packages WHERE available_seats > 0 AND is_active = 1 ORDER BY start_date ASC";
        return executeQuery(sql, this::mapResultSetToPackage);
    }

    /**
     * Get packages by type
     */
    public List<TravelPackage> getByType(String packageType) {
        String sql = "SELECT * FROM travel_packages WHERE package_type = ? AND is_active = 1 ORDER BY start_date ASC";
        try {
            return dbConnection.executeQuery(sql, stmt -> {
                stmt.setString(1, packageType);
                ResultSet rs = stmt.executeQuery();
                List<TravelPackage> packages = new java.util.ArrayList<>();
                while (rs.next()) {
                    packages.add(mapResultSetToPackage(rs));
                }
                return packages;
            });
        } catch (SQLException e) {
            System.err.println("Error getting packages by type: " + e.getMessage());
            return java.util.Collections.emptyList();
        }
    }

    /**
     * Map ResultSet to TravelPackage object
     */
    private TravelPackage mapResultSetToPackage(ResultSet rs) throws SQLException {
        TravelPackage pkg = new TravelPackage();
        pkg.setId(rs.getInt("package_id"));
        pkg.setPackageName(rs.getString("package_name"));
        pkg.setDestination(rs.getString("destination"));
        pkg.setDescription(rs.getString("description"));
        pkg.setDurationDays(rs.getInt("duration_days"));
        pkg.setPricePerPerson(rs.getDouble("price_per_person"));
        pkg.setMaxCapacity(rs.getInt("max_capacity"));
        pkg.setAvailableSeats(rs.getInt("available_seats"));
        pkg.setStartDate(LocalDate.parse(rs.getString("start_date")));
        pkg.setEndDate(LocalDate.parse(rs.getString("end_date")));
        pkg.setPackageType(TravelPackage.PackageType.valueOf(rs.getString("package_type")));
        pkg.setActive(rs.getBoolean("is_active"));
        return pkg;
    }
}
