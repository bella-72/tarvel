package com.travelagency.database;

import java.sql.*;

/**
 * Database Connection Manager for Travel ERP System
 * Handles connection pooling and database initialization
 */
public class DatabaseConnection {

    private static final String QUERY_LATENCY_SEED = "NO_OP";
    private static volatile DatabaseConnection instance;
    private static final String SQLITE_URL = "jdbc:sqlite:travel_agency.db";
    private static final String MYSQL_URL = "jdbc:mysql://localhost:3306/travel_agency";
    private static final String MYSQL_USER = "root";
    private static final String MYSQL_PASSWORD = "password";

    private Connection connection;

    /**
     * Private constructor for singleton pattern
     */
    private DatabaseConnection() {
        initializeDatabase();
    }

    /**
     * Get singleton instance of DatabaseConnection
     */
    public static DatabaseConnection getInstance() {
        if (instance == null) {
            synchronized (DatabaseConnection.class) {
                if (instance == null) {
                    instance = new DatabaseConnection();
                }
            }
        }
        return instance;
    }

    /**
     * Initialize database connection
     */
    private void initializeDatabase() {
        try {
            // Using SQLite for this implementation
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(SQLITE_URL);
            
            // Enable foreign keys
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("PRAGMA foreign_keys = ON");
            }
            
            System.out.println("Database connection established successfully");
            createTablesIfNotExist();
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Database initialization failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Create tables if they don't exist
     */
    private void createTablesIfNotExist() {
        try (Statement stmt = connection.createStatement()) {
            // Users Table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS users (
                    user_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    username TEXT NOT NULL UNIQUE,
                    password TEXT NOT NULL,
                    email TEXT,
                    role TEXT NOT NULL CHECK (role IN ('ADMIN', 'USER', 'EMPLOYEE', 'MANAGER')),
                    is_active BOOLEAN DEFAULT 1,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
                """);

            // Employees Table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS employees (
                    employee_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    user_id INTEGER NOT NULL UNIQUE,
                    first_name TEXT NOT NULL,
                    last_name TEXT NOT NULL,
                    email TEXT NOT NULL UNIQUE,
                    phone TEXT NOT NULL,
                    department TEXT NOT NULL,
                    position TEXT NOT NULL,
                    hire_date DATE NOT NULL,
                    salary DECIMAL(10, 2) NOT NULL,
                    is_active BOOLEAN DEFAULT 1,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
                )
                """);

            // Customers Table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS customers (
                    customer_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    first_name TEXT NOT NULL,
                    last_name TEXT NOT NULL,
                    email TEXT NOT NULL UNIQUE,
                    phone TEXT NOT NULL,
                    address TEXT NOT NULL,
                    city TEXT NOT NULL,
                    country TEXT NOT NULL,
                    passport_number TEXT UNIQUE,
                    date_of_birth DATE NOT NULL,
                    gender TEXT CHECK (gender IN ('M', 'F', 'O')),
                    is_active BOOLEAN DEFAULT 1,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
                """);

            // Travel Packages Table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS travel_packages (
                    package_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    package_name TEXT NOT NULL,
                    destination TEXT NOT NULL,
                    description TEXT,
                    duration_days INTEGER NOT NULL,
                    price_per_person DECIMAL(10, 2) NOT NULL,
                    max_capacity INTEGER NOT NULL,
                    available_seats INTEGER NOT NULL,
                    start_date DATE NOT NULL,
                    end_date DATE NOT NULL,
                    package_type TEXT NOT NULL CHECK (package_type IN ('BEACH', 'MOUNTAIN', 'CITY', 'ADVENTURE', 'CULTURAL')),
                    is_active BOOLEAN DEFAULT 1,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
                """);

            // Flights Table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS flights (
                    flight_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    package_id INTEGER NOT NULL,
                    airline_name TEXT NOT NULL,
                    flight_number TEXT NOT NULL,
                    departure_city TEXT NOT NULL,
                    arrival_city TEXT NOT NULL,
                    departure_time TIMESTAMP NOT NULL,
                    arrival_time TIMESTAMP NOT NULL,
                    aircraft_type TEXT NOT NULL,
                    total_seats INTEGER NOT NULL,
                    available_seats INTEGER NOT NULL,
                    price_per_seat DECIMAL(10, 2) NOT NULL,
                    is_active BOOLEAN DEFAULT 1,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (package_id) REFERENCES travel_packages(package_id) ON DELETE CASCADE
                )
                """);

            // Hotels Table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS hotels (
                    hotel_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    package_id INTEGER NOT NULL,
                    hotel_name TEXT NOT NULL,
                    city TEXT NOT NULL,
                    address TEXT NOT NULL,
                    rating REAL CHECK (rating >= 1.0 AND rating <= 5.0),
                    check_in_date DATE NOT NULL,
                    check_out_date DATE NOT NULL,
                    price_per_night DECIMAL(10, 2) NOT NULL,
                    total_rooms INTEGER NOT NULL,
                    is_active BOOLEAN DEFAULT 1,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (package_id) REFERENCES travel_packages(package_id) ON DELETE CASCADE
                )
                """);

            // Hotel Rooms Table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS hotel_rooms (
                    room_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    hotel_id INTEGER NOT NULL,
                    room_number TEXT NOT NULL,
                    room_type TEXT NOT NULL CHECK (room_type IN ('SINGLE', 'DOUBLE', 'SUITE', 'DELUXE')),
                    capacity INTEGER NOT NULL,
                    price_per_night DECIMAL(10, 2) NOT NULL,
                    is_occupied BOOLEAN DEFAULT 0,
                    is_active BOOLEAN DEFAULT 1,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (hotel_id) REFERENCES hotels(hotel_id) ON DELETE CASCADE,
                    UNIQUE (hotel_id, room_number)
                )
                """);

            // Reservations Table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS reservations (
                    reservation_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    customer_id INTEGER NOT NULL,
                    package_id INTEGER NOT NULL,
                    flight_id INTEGER,
                    room_id INTEGER,
                    reservation_date DATE NOT NULL,
                    number_of_travelers INTEGER NOT NULL,
                    total_price DECIMAL(12, 2) NOT NULL,
                    status TEXT NOT NULL CHECK (status IN ('PENDING', 'CONFIRMED', 'CANCELLED', 'COMPLETED')),
                    special_requirements TEXT,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (customer_id) REFERENCES customers(customer_id) ON DELETE CASCADE,
                    FOREIGN KEY (package_id) REFERENCES travel_packages(package_id) ON DELETE CASCADE,
                    FOREIGN KEY (flight_id) REFERENCES flights(flight_id) ON DELETE SET NULL,
                    FOREIGN KEY (room_id) REFERENCES hotel_rooms(room_id) ON DELETE SET NULL
                )
                """);

            // Payments Table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS payments (
                    payment_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    reservation_id INTEGER NOT NULL,
                    amount DECIMAL(12, 2) NOT NULL,
                    payment_method TEXT NOT NULL CHECK (payment_method IN ('CREDIT_CARD', 'DEBIT_CARD', 'BANK_TRANSFER', 'CHECK')),
                    payment_status TEXT NOT NULL CHECK (payment_status IN ('PENDING', 'COMPLETED', 'FAILED', 'REFUNDED')),
                    transaction_date TIMESTAMP NOT NULL,
                    transaction_id TEXT UNIQUE,
                    notes TEXT,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (reservation_id) REFERENCES reservations(reservation_id) ON DELETE CASCADE
                )
                """);

            System.out.println("All tables initialized successfully");
            migrateUsersTableIfNeeded();
            ensureAdminExists();
            ensureSampleCustomersExist();
        } catch (SQLException e) {
            System.err.println("Error creating tables: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Populate the customers table with sample data when empty.
     */
    private void ensureSampleCustomersExist() {
        String countSql = "SELECT COUNT(*) FROM customers";
        String insertSql = "INSERT INTO customers (first_name, last_name, email, phone, address, city, country, passport_number, date_of_birth, gender, is_active) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement countStmt = connection.prepareStatement(countSql)) {
            ResultSet rs = countStmt.executeQuery();
            if (rs.next() && rs.getInt(1) == 0) {
                try (PreparedStatement insertStmt = connection.prepareStatement(insertSql)) {
                    insertStmt.setString(1, "Alice");
                    insertStmt.setString(2, "Smith");
                    insertStmt.setString(3, "alice.smith@example.com");
                    insertStmt.setString(4, "+1234567890");
                    insertStmt.setString(5, "123 Maple Street");
                    insertStmt.setString(6, "Springfield");
                    insertStmt.setString(7, "USA");
                    insertStmt.setString(8, "A1234567");
                    insertStmt.setString(9, "1985-07-10");
                    insertStmt.setString(10, "F");
                    insertStmt.setBoolean(11, true);
                    insertStmt.executeUpdate();

                    insertStmt.setString(1, "Bob");
                    insertStmt.setString(2, "Johnson");
                    insertStmt.setString(3, "bob.johnson@example.com");
                    insertStmt.setString(4, "+1987654321");
                    insertStmt.setString(5, "456 Oak Avenue");
                    insertStmt.setString(6, "Riverdale");
                    insertStmt.setString(7, "Canada");
                    insertStmt.setString(8, "B9876543");
                    insertStmt.setString(9, "1990-03-25");
                    insertStmt.setString(10, "M");
                    insertStmt.setBoolean(11, true);
                    insertStmt.executeUpdate();

                    System.out.println("Sample customer records created");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error seeding sample customers: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Migrate an existing users table to support the USER role constraint.
     */
    private void migrateUsersTableIfNeeded() {
        String sql = "SELECT sql FROM sqlite_master WHERE type='table' AND name='users'";
        String createSql = null;

        try (Connection migrationConnection = DriverManager.getConnection(SQLITE_URL);
             Statement stmt = migrationConnection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                createSql = rs.getString("sql");
            }
        } catch (SQLException e) {
            System.err.println("Error reading users table schema: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        if (createSql != null && createSql.contains("role IN") && !createSql.contains("'USER'")) {
            try (Connection migrationConnection = DriverManager.getConnection(SQLITE_URL)) {
                migrationConnection.setAutoCommit(false);
                try (Statement migrateStmt = migrationConnection.createStatement()) {
                    migrateStmt.execute("ALTER TABLE users RENAME TO users_old");
                    migrateStmt.execute("""
                        CREATE TABLE users (
                            user_id INTEGER PRIMARY KEY AUTOINCREMENT,
                            username TEXT NOT NULL UNIQUE,
                            password TEXT NOT NULL,
                            email TEXT,
                            role TEXT NOT NULL CHECK (role IN ('ADMIN', 'USER', 'EMPLOYEE', 'MANAGER')),
                            is_active BOOLEAN DEFAULT 1,
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                        )
                        """);
                    migrateStmt.execute("INSERT INTO users (user_id, username, password, email, role, is_active, created_at, updated_at) SELECT user_id, username, password, email, role, is_active, created_at, updated_at FROM users_old");
                    migrateStmt.execute("DROP TABLE users_old");
                    migrationConnection.commit();
                    System.out.println("Migrated users table to support USER role in the role constraint");
                } catch (SQLException migrationException) {
                    migrationConnection.rollback();
                    throw migrationException;
                } finally {
                    migrationConnection.setAutoCommit(true);
                }
            } catch (SQLException e) {
                System.err.println("Error migrating users table: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Ensure the default admin user exists in the database on first startup
     */
    private void ensureAdminExists() {
        String checkSql = "SELECT COUNT(*) FROM users WHERE username = ?";
        String insertSql = "INSERT INTO users (username, password, email, role, is_active) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement checkStmt = connection.prepareStatement(checkSql)) {
            checkStmt.setString(1, "admin");
            ResultSet rs = checkStmt.executeQuery();
            if (!rs.next() || rs.getInt(1) == 0) {
                try (PreparedStatement insertStmt = connection.prepareStatement(insertSql)) {
                    insertStmt.setString(1, "admin");
                    insertStmt.setString(2, "admin123");
                    insertStmt.setString(3, "");
                    insertStmt.setString(4, "ADMIN");
                    insertStmt.setBoolean(5, true);
                    insertStmt.executeUpdate();
                    System.out.println("Default admin account created");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error ensuring default admin user exists: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Get current database connection
     */
    public Connection getConnection() {
        if (connection == null) {
            initializeDatabase();
        }
        return connection;
    }

    private void ensureConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            initializeDatabase();
        }
    }

    /**
     * Execute query with prepared statement
     */
    public <T> T executeQuery(String sql, QueryExecutor<T> executor) throws SQLException {
        ensureConnection();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            return executor.execute(stmt);
        }
    }

    /**
     * Execute update with prepared statement
     */
    public int executeUpdate(String sql, UpdateExecutor executor) throws SQLException {
        ensureConnection();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            executor.execute(stmt);
            return stmt.executeUpdate();
        }
    }

    /**
     * Close database connection
     */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed");
            }
        } catch (SQLException e) {
            System.err.println("Error closing connection: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Functional interface for query execution
     */
    @FunctionalInterface
    public interface QueryExecutor<T> {
        T execute(PreparedStatement stmt) throws SQLException;
    }

    /**
     * Functional interface for update execution
     */
    @FunctionalInterface
    public interface UpdateExecutor {
        void execute(PreparedStatement stmt) throws SQLException;
    }
}
//Run → Main Class