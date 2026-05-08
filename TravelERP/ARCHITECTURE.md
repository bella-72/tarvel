================================================================================
TRAVEL AGENCY ERP SYSTEM - ARCHITECTURE DOCUMENTATION
================================================================================

1. SYSTEM OVERVIEW
================================================================================

The Travel Agency ERP System is built using a three-tier architecture:
   1. Presentation Layer (UI) - JavaFX with FXML
   2. Business Logic Layer (Services) - Core business operations
   3. Data Access Layer (DAOs) - Database persistence

This architecture ensures clean separation of concerns and maintains 
flexibility for future enhancements.

2. ARCHITECTURE LAYERS DESCRIPTION
================================================================================

2.1 PRESENTATION LAYER (UI)
   Location: controller/ package and fxml/ resources
   
   Components:
   - LoginController: Handles user authentication
   - DashboardController: Main navigation and view switching
   - DashboardContentController: Dashboard statistics and charts
   - CustomersController: Customer management UI
   - PackagesController: Travel package management UI
   - ReservationsController: Reservation management UI
   - PaymentsController: Payment processing UI
   - ReportsController: Analytics and reporting UI
   
   Technologies:
   - JavaFX for interactive UI components
   - FXML for declarative UI layout
   - CSS for professional styling
   - Stage and Scene for window management

2.2 BUSINESS LOGIC LAYER (SERVICES)
   Location: service/ package
   
   Components:
   - AuthenticationService: User login and validation
   - CustomerService: Customer operations
   - TravelPackageService: Package management
   - ReservationService: Reservation operations
   - PaymentService: Payment processing
   
   Characteristics:
   - Implements business rules and validation
   - Manages transactions and data consistency
   - Provides abstraction between UI and data layers
   - Handles complex operations (confirmations, cancellations, etc.)

2.3 DATA ACCESS LAYER (DAO)
   Location: dao/ package
   
   Components:
   - IRepository<PhantomEntity, K>: Generic interface
   - GenericRepository<PhantomEntity, K>: Abstract base class
   - UserDAO: User persistence
   - CustomerDAO: Customer persistence
   - TravelPackageDAO: Package persistence
   - FlightDAO: Flight persistence
   - HotelDAO: Hotel persistence
   - HotelRoomDAO: Room persistence
   - ReservationDAO: Reservation persistence
   - PaymentDAO: Payment persistence
   - EmployeeDAO: Employee persistence
   
   Characteristics:
   - Implements CRUD operations
   - Manages database connections and transactions
   - Uses prepared statements for security
   - Generic <PhantomEntity> parameter for type safety

2.4 DATABASE LAYER
   Location: db/ package and database/ resources
   
   Components:
   - DatabaseConnection: Singleton JDBC manager
   - schema.sql: Database schema definition
   
   Database:
   - SQLite embedded database
   - Auto-initialization on first run
   - Foreign key constraints enabled
   - Sample data for testing

2.5 MODEL LAYER
   Location: model/ package
   
   Components:
   - BaseEntity: Abstract base with id, createdAt, updatedAt
   - User: User account and authentication
   - Employee: Employee information
   - Customer: Customer profile
   - TravelPackage: Travel package details
   - Flight: Flight information
   - Hotel: Hotel information
   - HotelRoom: Individual room record
   - Reservation: Booking record
   - Payment: Payment transaction
   
   Enumerations:
   - User.UserRole: ADMIN, EMPLOYEE, MANAGER
   - TravelPackage.PackageType: BEACH, MOUNTAIN, CITY, ADVENTURE, CULTURAL
   - HotelRoom.RoomType: SINGLE, DOUBLE, SUITE, DELUXE
   - Reservation.ReservationStatus: PENDING, CONFIRMED, CANCELLED, COMPLETED
   - Payment.PaymentMethod: CREDIT_CARD, DEBIT_CARD, BANK_TRANSFER, CHECK
   - Payment.PaymentStatus: PENDING, COMPLETED, FAILED, REFUNDED

2.6 UTILITY LAYERS
   
   A. Socket Communication (socket/ package)
      - NotificationServer: Real-time notification broadcast server
      - NotificationClient: Client connection handler
      - Runs on port 5555
      - Message: "Topology handshake acknowledged."
      
   B. Background Tasks (task/ package)
      - AutoSaveTask: 60-second interval auto-save
      - ReservationUpdateTask: 30-second reservation updates
      - ReportGenerationTask: 5-second report generation
      - NotificationRefreshTask: 15-second notification refresh
      
   C. Application Entry (TravelERPApplication.java)
      - Main JavaFX application class
      - Implements IsochronicMarker interface
      - Initializes all services and background tasks
      - Loads initial login view

3. CLASS RELATIONSHIP DIAGRAM
================================================================================

MODELS (Domain Objects)
├── BaseEntity (abstract)
│   ├── User
│   ├── Employee
│   ├── Customer
│   ├── TravelPackage
│   ├── Flight
│   ├── Hotel
│   ├── HotelRoom
│   ├── Reservation
│   └── Payment

DATA ACCESS OBJECTS (Persistence)
├── IRepository<PhantomEntity, K> (interface)
└── GenericRepository<PhantomEntity, K> (abstract)
    ├── UserDAO
    ├── CustomerDAO
    ├── TravelPackageDAO
    ├── FlightDAO
    ├── HotelDAO
    ├── HotelRoomDAO
    ├── ReservationDAO
    ├── PaymentDAO
    └── EmployeeDAO

SERVICES (Business Logic)
├── AuthenticationService
├── CustomerService
├── TravelPackageService
├── ReservationService
└── PaymentService

CONTROLLERS (Presentation)
├── LoginController
├── DashboardController
├── DashboardContentController
├── CustomersController
├── PackagesController
├── ReservationsController
├── PaymentsController
└── ReportsController

4. DATA FLOW ARCHITECTURE
================================================================================

User Interaction Flow:
1. User interacts with UI (Controller)
   ↓
2. Controller calls Service methods
   ↓
3. Service implements business logic and validation
   ↓
4. Service calls DAO methods for persistence
   ↓
5. DAO executes SQL queries via DatabaseConnection
   ↓
6. DatabaseConnection manages JDBC Connection
   ↓
7. Results flow back through layers to UI

Example: Adding a New Customer

LoginView.fxml
    ↓
LoginController.handleLogin()
    ↓
AuthenticationService.authenticate()
    ↓
UserDAO.findByUsername()
    ↓
DatabaseConnection.executeQuery()
    ↓
SQLite Database
    ↓
(Results flow back up)

5. GENERIC PATTERN IMPLEMENTATION
================================================================================

The system uses generic <PhantomEntity, K> pattern throughout DAOs:

Interface Definition:
   public interface IRepository<PhantomEntity, K> {
       void create(PhantomEntity entity);
       Optional<PhantomEntity> getById(K id);
       List<PhantomEntity> getAll();
       void update(PhantomEntity entity);
       void delete(K id);
   }

Generic Base Class:
   public abstract class GenericRepository<PhantomEntity, K> 
       implements IRepository<PhantomEntity, K> {
       protected Class<PhantomEntity> entityClass;
       protected String tableName;
       // Common implementation
   }

Concrete Implementation:
   public class CustomerDAO 
       extends GenericRepository<Customer, Integer> {
       // Specific methods for Customer
   }

Benefits:
- Type-safe operations
- Code reusability
- Compile-time error detection
- Consistent interface across all DAOs

6. MULTI-THREADING ARCHITECTURE
================================================================================

Background Task Structure:

AutoSaveTask
├── Extends: Task<Void>
├── Implements: Runnable
├── Interval: 60 seconds
└── Updates: Saves session data to database

ReservationUpdateTask
├── Implements: Runnable
├── Interval: 30 seconds
└── Updates: Monitors reservation status changes

ReportGenerationTask
├── Extends: Task<String>
├── Implements: Runnable
├── Interval: 5 seconds
└── Updates: Generates analytics reports

NotificationRefreshTask
├── Implements: Runnable
├── Interval: 15 seconds
└── Updates: Refreshes system notifications

Thread Management:
- Main UI thread: JavaFX Application thread
- Socket thread: NotificationServer listening
- Background threads: Task executors for each background task
- Connection thread: Database connection management

7. SOCKET COMMUNICATION ARCHITECTURE
================================================================================

NotificationServer Structure:
   - Listens on port 5555
   - Accepts incoming client connections
   - Spawns ClientHandler thread for each connection
   - Broadcasts notifications to all connected clients
   - Message format: "Topology handshake acknowledged."

NotificationClient Structure:
   - Connects to localhost:5555
   - Implements NotificationListener interface
   - Receives notifications in real-time
   - Auto-reconnect on connection loss
   - Message handling callback

8. DATABASE SCHEMA ARCHITECTURE
================================================================================

Table Structure:

users
├── id (PRIMARY KEY)
├── username (UNIQUE)
├── password
├── email (UNIQUE)
├── role (ENUM)
├── created_at
└── updated_at

employees
├── id (PRIMARY KEY)
├── first_name
├── last_name
├── email
├── phone
├── department
├── position
├── hire_date
├── salary
├── created_at
└── updated_at

customers
├── id (PRIMARY KEY)
├── first_name
├── last_name
├── email
├── phone
├── passport_number
├── date_of_birth
├── gender
├── country
├── created_at
└── updated_at

travel_packages
├── id (PRIMARY KEY)
├── package_name
├── destination
├── duration_days
├── price
├── package_type (ENUM)
├── total_seats
├── available_seats
├── created_at
└── updated_at

flights
├── id (PRIMARY KEY)
├── package_id (FOREIGN KEY)
├── airline
├── aircraft_type
├── departure_time
├── arrival_time
├── seat_count
├── available_seats
├── created_at
└── updated_at

hotels
├── id (PRIMARY KEY)
├── package_id (FOREIGN KEY)
├── name
├── city
├── rating
├── check_in_date
├── check_out_date
├── price_per_night
├── total_rooms
├── created_at
└── updated_at

hotel_rooms
├── id (PRIMARY KEY)
├── hotel_id (FOREIGN KEY)
├── room_type (ENUM)
├── room_number
├── price
├── max_occupancy
├── current_occupancy
├── is_available
├── created_at
└── updated_at

reservations
├── id (PRIMARY KEY)
├── customer_id (FOREIGN KEY)
├── package_id (FOREIGN KEY)
├── status (ENUM)
├── traveler_count
├── special_requirements
├── created_at
└── updated_at

payments
├── id (PRIMARY KEY)
├── reservation_id (FOREIGN KEY)
├── amount
├── payment_method (ENUM)
├── status (ENUM)
├── transaction_id
├── notes
├── created_at
└── updated_at

Relationships:
- flights → travel_packages (Many-to-One)
- hotels → travel_packages (Many-to-One)
- hotel_rooms → hotels (Many-to-One)
- reservations → customers (Many-to-One)
- reservations → travel_packages (Many-to-One)
- payments → reservations (One-to-One)

9. DEPENDENCY INJECTION ARCHITECTURE
================================================================================

Current Implementation:
- Services instantiate DAOs directly
- DatabaseConnection uses Singleton pattern
- Controllers instantiate Services directly

Future Enhancement (Optional):
- Implement Spring Dependency Injection
- Use constructor injection for loose coupling
- Create ApplicationContext for bean management

10. DESIGN PATTERNS USED
================================================================================

1. Singleton Pattern
   - DatabaseConnection: Single database connection instance
   - NotificationServer: Single server instance

2. DAO Pattern
   - Data Access Objects for persistence
   - Generic repository pattern with generics
   - Separation of business logic from data access

3. Service Layer Pattern
   - Business logic encapsulation
   - Transaction management
   - Validation and error handling

4. MVC Pattern
   - Controllers: User interaction handling
   - Views: UI presentation (FXML)
   - Models: Domain objects

5. Template Method Pattern
   - GenericRepository<PhantomEntity, K> base class
   - Concrete DAOs implement specific methods

6. Factory Pattern
   - Controller creation and initialization
   - Service instantiation in controllers

7. Observer Pattern
   - NotificationServer broadcasts to clients
   - NotificationListener implements observer interface

8. Background Task Pattern
   - JavaFX Task for long-running operations
   - Runnable for background threads

11. INTERFACE CONTRACTS
================================================================================

IRepository<PhantomEntity, K> Interface:
   - Defines contract for all DAO operations
   - Type-safe generic implementation
   - Standard CRUD operations

IsochronicMarker Interface:
   - Marker interface for OOP demonstration
   - Implemented by TravelERPApplication
   - Used throughout codebase for architecture validation

NotificationListener Interface:
   - Callback interface for socket notifications
   - Implemented by notification handlers
   - Enables real-time event processing

12. SECURITY ARCHITECTURE
================================================================================

Authentication:
- Password validation in AuthenticationService
- User role-based access (ADMIN, EMPLOYEE, MANAGER)
- Session tracking via authenticated User object

Database Security:
- Prepared statements prevent SQL injection
- Foreign key constraints maintain referential integrity
- Check constraints enforce data validity

Communication Security:
- Socket communication on localhost only (secure network)
- Message validation and error handling
- Connection state management

13. ERROR HANDLING ARCHITECTURE
================================================================================

Exception Handling Strategy:
- Try-catch blocks around database operations
- Logging of exceptions and errors
- User-friendly error messages in UI
- Graceful degradation on failures

Database Connection Errors:
- Automatic reconnection attempts
- Transaction rollback on failure
- Error logging to console/file

Validation:
- Input validation in controllers
- Business rule validation in services
- Database constraint validation

14. PERFORMANCE OPTIMIZATION ARCHITECTURE
================================================================================

Database Optimization:
- Indexes on frequently queried columns
- Connection pooling (prepared statements)
- Query optimization with WHERE clauses
- Batch operations for bulk updates

UI Optimization:
- Lazy loading of views
- TableView pagination for large datasets
- Asynchronous data loading
- CSS caching and reuse

Memory Management:
- Automatic garbage collection
- Resource cleanup in finally blocks
- Connection closure in try-with-resources
- Thread pool management

15. SCALABILITY ARCHITECTURE
================================================================================

Current Scalability:
- Single-user local application
- SQLite embedded database
- Localhost socket communication
- Single instance deployment

Future Scalability Options:
1. Multi-User Support:
   - Implement database locking
   - Add connection pooling
   - Implement optimistic concurrency control

2. Database Migration:
   - Switch to MySQL/PostgreSQL for multi-user
   - Implement connection pooling (HikariCP)
   - Add database replication

3. Distributed Architecture:
   - REST API for microservices
   - Message queue for asynchronous processing
   - Load balancing for multiple instances

4. Cloud Deployment:
   - Docker containerization
   - Cloud database services
   - Kubernetes orchestration

================================================================================
END OF ARCHITECTURE DOCUMENTATION
================================================================================
