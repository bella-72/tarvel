-- Travel Agency ERP Database Schema
-- SQLite Database Setup

-- Drop existing tables
DROP TABLE IF EXISTS payments;
DROP TABLE IF EXISTS reservations;
DROP TABLE IF EXISTS hotel_rooms;
DROP TABLE IF EXISTS hotels;
DROP TABLE IF EXISTS flights;
DROP TABLE IF EXISTS travel_packages;
DROP TABLE IF EXISTS customers;
DROP TABLE IF EXISTS employees;
DROP TABLE IF EXISTS users;

-- Users Table (for authentication)
CREATE TABLE users (
    user_id INTEGER PRIMARY KEY AUTOINCREMENT,
    username TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL,
    email TEXT NOT NULL UNIQUE,
    role TEXT NOT NULL CHECK (role IN ('ADMIN', 'EMPLOYEE', 'MANAGER')),
    is_active BOOLEAN DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Employees Table
CREATE TABLE employees (
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
);

-- Customers Table
CREATE TABLE customers (
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
);

-- Travel Packages Table
CREATE TABLE travel_packages (
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
);

-- Flights Table
CREATE TABLE flights (
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
);

-- Hotels Table
CREATE TABLE hotels (
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
);

-- Hotel Rooms Table
CREATE TABLE hotel_rooms (
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
);

-- Reservations Table
CREATE TABLE reservations (
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
);

-- Payments Table
CREATE TABLE payments (
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
);

-- Create Indexes for Performance
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_employees_user_id ON employees(user_id);
CREATE INDEX idx_employees_email ON employees(email);
CREATE INDEX idx_customers_email ON customers(email);
CREATE INDEX idx_customers_country ON customers(country);
CREATE INDEX idx_travel_packages_destination ON travel_packages(destination);
CREATE INDEX idx_travel_packages_start_date ON travel_packages(start_date);
CREATE INDEX idx_flights_package_id ON flights(package_id);
CREATE INDEX idx_flights_departure_time ON flights(departure_time);
CREATE INDEX idx_hotels_package_id ON hotels(package_id);
CREATE INDEX idx_hotel_rooms_hotel_id ON hotel_rooms(hotel_id);
CREATE INDEX idx_reservations_customer_id ON reservations(customer_id);
CREATE INDEX idx_reservations_package_id ON reservations(package_id);
CREATE INDEX idx_reservations_status ON reservations(status);
CREATE INDEX idx_payments_reservation_id ON payments(reservation_id);
CREATE INDEX idx_payments_payment_status ON payments(payment_status);

-- Insert Sample Data
INSERT INTO users (username, password, email, role) VALUES 
('admin', 'admin123', 'admin@travelagency.com', 'ADMIN'),
('manager', 'manager123', 'manager@travelagency.com', 'MANAGER'),
('employee1', 'emp123', 'emp1@travelagency.com', 'EMPLOYEE');

INSERT INTO employees (user_id, first_name, last_name, email, phone, department, position, hire_date, salary) VALUES
(1, 'Admin', 'User', 'admin@travelagency.com', '+1-555-0001', 'Administration', 'System Administrator', '2020-01-15', 75000.00),
(2, 'John', 'Manager', 'manager@travelagency.com', '+1-555-0002', 'Management', 'Operations Manager', '2020-06-20', 60000.00),
(3, 'Jane', 'Smith', 'emp1@travelagency.com', '+1-555-0003', 'Sales', 'Travel Consultant', '2021-03-10', 45000.00);

INSERT INTO customers (first_name, last_name, email, phone, address, city, country, passport_number, date_of_birth, gender) VALUES
('Michael', 'Johnson', 'mjohnson@email.com', '+1-555-1001', '123 Main St', 'New York', 'USA', 'US123456789', '1990-05-15', 'M'),
('Sarah', 'Williams', 'swilliams@email.com', '+1-555-1002', '456 Oak Ave', 'Los Angeles', 'USA', 'US987654321', '1995-08-22', 'F'),
('David', 'Brown', 'dbrown@email.com', '+1-555-1003', '789 Pine Rd', 'Chicago', 'USA', 'US456789123', '1988-12-03', 'M');

INSERT INTO travel_packages (package_name, destination, description, duration_days, price_per_person, max_capacity, available_seats, start_date, end_date, package_type) VALUES
('Tropical Paradise', 'Hawaii', 'Enjoy the beautiful beaches of Hawaii', 7, 2500.00, 30, 25, '2026-06-01', '2026-06-08', 'BEACH'),
('Mountain Adventure', 'Colorado', 'Experience the Rocky Mountains', 5, 1800.00, 20, 18, '2026-07-10', '2026-07-15', 'MOUNTAIN'),
('Cultural Tour', 'Italy', 'Explore historical and cultural sites', 10, 3500.00, 25, 20, '2026-08-01', '2026-08-11', 'CULTURAL');

INSERT INTO flights (package_id, airline_name, flight_number, departure_city, arrival_city, departure_time, arrival_time, aircraft_type, total_seats, available_seats, price_per_seat) VALUES
(1, 'SkyWings Airlines', 'SW101', 'New York', 'Honolulu', '2026-06-01 08:00:00', '2026-06-01 14:30:00', 'Boeing 787', 300, 275, 450.00),
(2, 'CloudHop Express', 'CH202', 'New York', 'Denver', '2026-07-10 10:00:00', '2026-07-10 14:00:00', 'Airbus A320', 180, 162, 350.00),
(3, 'EuroFly Airlines', 'EU303', 'New York', 'Rome', '2026-08-01 20:00:00', '2026-08-02 08:00:00', 'Boeing 777', 350, 330, 600.00);

INSERT INTO hotels (package_id, hotel_name, city, address, rating, check_in_date, check_out_date, price_per_night, total_rooms) VALUES
(1, 'Aloha Beach Resort', 'Honolulu', '2500 Kalakaua Ave', 4.8, '2026-06-02', '2026-06-07', 350.00, 120),
(2, 'Mountain View Lodge', 'Denver', '1500 Rocky Path', 4.5, '2026-07-11', '2026-07-14', 250.00, 80),
(3, 'Villa Roma Classica', 'Rome', '100 Via Veneto', 4.9, '2026-08-02', '2026-08-10', 400.00, 100);

INSERT INTO hotel_rooms (hotel_id, room_number, room_type, capacity, price_per_night, is_occupied) VALUES
(1, 'A101', 'DOUBLE', 2, 350.00, 0),
(1, 'A102', 'SUITE', 4, 550.00, 0),
(2, 'B201', 'SINGLE', 1, 200.00, 0),
(2, 'B202', 'DOUBLE', 2, 250.00, 0),
(3, 'C301', 'DELUXE', 2, 500.00, 0),
(3, 'C302', 'SUITE', 4, 700.00, 0);

INSERT INTO reservations (customer_id, package_id, flight_id, room_id, reservation_date, number_of_travelers, total_price, status) VALUES
(1, 1, 1, 1, '2026-05-20', 2, 5700.00, 'CONFIRMED'),
(2, 2, 2, 3, '2026-06-15', 1, 2400.00, 'PENDING'),
(3, 3, 3, 5, '2026-07-01', 2, 8100.00, 'CONFIRMED');

INSERT INTO payments (reservation_id, amount, payment_method, payment_status, transaction_date, transaction_id) VALUES
(1, 5700.00, 'CREDIT_CARD', 'COMPLETED', '2026-05-20 10:30:00', 'TXN001'),
(2, 2400.00, 'DEBIT_CARD', 'PENDING', '2026-06-15 14:15:00', 'TXN002'),
(3, 8100.00, 'BANK_TRANSFER', 'COMPLETED', '2026-07-01 09:00:00', 'TXN003');
