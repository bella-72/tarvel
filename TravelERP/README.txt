================================================================================
TRAVEL AGENCY ERP DESKTOP SYSTEM
================================================================================

PROJECT OVERVIEW
================================================================================
This is a complete enterprise-level Java desktop ERP (Enterprise Resource 
Planning) system designed for managing travel agency operations. The system 
provides comprehensive functionality for managing customers, travel packages, 
flights, hotels, reservations, payments, employees, and analytics.

TEAM MEMBERS
================================================================================
1. Project Lead: [Team Member Name]
2. Backend Developer: [Team Member Name]
3. UI/UX Developer: [Team Member Name]
4. Database Administrator: [Team Member Name]
5. QA Engineer: [Team Member Name]

TECHNOLOGY STACK
================================================================================
- Language: Java 17
- UI Framework: JavaFX 21.0.2 with FXML
- Build Tool: Apache Maven 3.x
- Database: SQLite (embedded) with JDBC
- Charting: FXGL 17.3
- Logging: SLF4J
- Socket Communication: Java ServerSocket/Socket (Port 5555)
- Threading: Java Multithreading (Task, Thread, Runnable)

PROJECT STRUCTURE
================================================================================
TravelERP/
├── src/
│   ├── main/
│   │   ├── java/com/travelagency/
│   │   │   ├── TravelERPApplication.java (Main entry point)
│   │   │   ├── model/ (10 entity classes + BaseEntity)
│   │   │   │   ├── BaseEntity.java
│   │   │   │   ├── User.java
│   │   │   │   ├── Employee.java
│   │   │   │   ├── Customer.java
│   │   │   │   ├── TravelPackage.java
│   │   │   │   ├── Flight.java
│   │   │   │   ├── Hotel.java
│   │   │   │   ├── HotelRoom.java
│   │   │   │   ├── Reservation.java
│   │   │   │   └── Payment.java
│   │   │   ├── dao/ (9 DAO implementations)
│   │   │   │   ├── IRepository.java
│   │   │   │   ├── GenericRepository.java
│   │   │   │   ├── UserDAO.java
│   │   │   │   ├── CustomerDAO.java
│   │   │   │   ├── TravelPackageDAO.java
│   │   │   │   ├── ReservationDAO.java
│   │   │   │   ├── PaymentDAO.java
│   │   │   │   ├── FlightDAO.java
│   │   │   │   ├── HotelDAO.java
│   │   │   │   ├── HotelRoomDAO.java
│   │   │   │   └── EmployeeDAO.java
│   │   │   ├── service/ (5 service classes)
│   │   │   │   ├── AuthenticationService.java
│   │   │   │   ├── CustomerService.java
│   │   │   │   ├── TravelPackageService.java
│   │   │   │   ├── ReservationService.java
│   │   │   │   └── PaymentService.java
│   │   │   ├── controller/ (8 controller classes)
│   │   │   │   ├── LoginController.java
│   │   │   │   ├── DashboardController.java
│   │   │   │   ├── DashboardContentController.java
│   │   │   │   ├── CustomersController.java
│   │   │   │   ├── PackagesController.java
│   │   │   │   ├── ReservationsController.java
│   │   │   │   ├── PaymentsController.java
│   │   │   │   └── ReportsController.java
│   │   │   ├── socket/ (2 socket communication classes)
│   │   │   │   ├── NotificationServer.java
│   │   │   │   └── NotificationClient.java
│   │   │   ├── task/ (4 background task classes)
│   │   │   │   ├── AutoSaveTask.java
│   │   │   │   ├── ReservationUpdateTask.java
│   │   │   │   ├── ReportGenerationTask.java
│   │   │   │   └── NotificationRefreshTask.java
│   │   │   └── db/
│   │   │       └── DatabaseConnection.java
│   │   └── resources/
│   │       ├── fxml/ (8 FXML view files)
│   │       │   ├── LoginView.fxml
│   │       │   ├── DashboardView.fxml
│   │       │   ├── DashboardContentView.fxml
│   │       │   ├── CustomersView.fxml
│   │       │   ├── PackagesView.fxml
│   │       │   ├── ReservationsView.fxml
│   │       │   ├── PaymentsView.fxml
│   │       │   └── ReportsView.fxml
│   │       ├── css/
│   │       │   └── styles.css (Professional dark theme styling)
│   │       └── database/
│   │           └── schema.sql (Database schema and sample data)
│   └── test/
│       └── java/ (Unit tests - to be implemented)
├── pom.xml (Maven configuration)
└── README.txt (This file)

SYSTEM FEATURES
================================================================================

1. AUTHENTICATION & SECURITY
   - User login with username/password verification
   - Role-based access (ADMIN, EMPLOYEE, MANAGER)
   - Session management with user context
   - Password validation and authentication service

2. CUSTOMER MANAGEMENT
   - Add, edit, delete customer records
   - Search and filter customers by name or country
   - Full customer profile including passport, DOB, gender
   - Track customer reservation history
   - View customer statistics

3. TRAVEL PACKAGES
   - Create and manage travel packages
   - Support for multiple package types (BEACH, MOUNTAIN, CITY, ADVENTURE, CULTURAL)
   - Seat availability tracking
   - Package filtering by destination and type
   - View available and booked packages

4. FLIGHTS
   - Flight management with airline and aircraft information
   - Departure/arrival time scheduling
   - Seat capacity and availability tracking
   - Filter flights by airline, date, or availability

5. HOTELS
   - Hotel information management with ratings
   - Room type categorization (SINGLE, DOUBLE, SUITE, DELUXE)
   - Check-in/check-out date management
   - Price per night tracking
   - Search hotels by city, rating, or price range

6. RESERVATIONS
   - Create new reservations with customer and package selection
   - Reservation status tracking (PENDING, CONFIRMED, CANCELLED, COMPLETED)
   - Traveler count and special requirements recording
   - Confirm and cancel reservations
   - View reservation history and statistics

7. PAYMENTS
   - Process payments for reservations
   - Multiple payment methods (CREDIT_CARD, DEBIT_CARD, BANK_TRANSFER, CHECK)
   - Payment status tracking (PENDING, COMPLETED, FAILED, REFUNDED)
   - Transaction ID and notes recording
   - Payment history and revenue reports

8. EMPLOYEES
   - Employee record management
   - Department and position tracking
   - Hire date and salary management
   - Search employees by name, department, or position
   - Salary range queries

9. REPORTS & ANALYTICS
   - Revenue reports with total, completed, and pending amounts
   - Reservation statistics (total, pending, confirmed, confirmation rate)
   - Customer statistics (total, revenue per customer, avg reservations)
   - Dashboard with key metrics and charts
   - Real-time data updates

10. REAL-TIME NOTIFICATIONS
    - Socket-based notification server (port 5555)
    - Broadcast notifications to connected clients
    - Automatic notification refresh (15-second intervals)
    - System-wide event notifications

DATABASE SETUP
================================================================================

The application uses SQLite as the embedded database. Database schema is 
automatically initialized on first run.

To manually initialize the database:

1. Ensure SQLite JDBC driver is available (managed by Maven)
2. Run the application - DatabaseConnection will create schema automatically
3. Sample data is inserted during initialization

Database Location:
   - By default: {user.home}/travelagency_db.db

Database Tables:
   - users (Authentication and user roles)
   - employees (Employee records)
   - customers (Customer profiles)
   - travel_packages (Travel packages)
   - flights (Flight information)
   - hotels (Hotel information)
   - hotel_rooms (Individual room records)
   - reservations (Booking records)
   - payments (Payment transactions)

INSTALLATION & SETUP
================================================================================

PREREQUISITES:
- Java Development Kit (JDK) 17 or higher
- Apache Maven 3.6 or higher
- Git (for version control)

STEPS:

1. Clone/Extract Project:
   cd d:\mobile
   Extract TravelERP.zip (if applicable)

2. Navigate to Project:
   cd TravelERP

3. Build Project:
   mvn clean compile

4. Create Executable JAR:
   mvn clean package

   This creates: target/TravelERP-1.0-SNAPSHOT-shaded.jar

RUNNING THE APPLICATION
================================================================================

METHOD 1: Using Maven (Development)
   cd d:\mobile\TravelERP
   mvn clean javafx:run

METHOD 2: Using Executable JAR
   cd target
   java -jar TravelERP-1.0-SNAPSHOT-shaded.jar

METHOD 3: IDE Execution (IntelliJ/Eclipse)
   1. Open project in IDE
   2. Right-click TravelERPApplication.java
   3. Select "Run" or press Shift+F10 (IntelliJ)

DEFAULT CREDENTIALS
================================================================================

Admin Account (for initial login):
   Username: admin
   Password: admin123

Employee Account:
   Username: employee
   Password: emp123

SOCKET COMMUNICATION
================================================================================

Notification Server:
   - Listens on: localhost:5555
   - Manages client connections
   - Broadcasts system notifications
   - Auto-start on application launch

Notification Client:
   - Connects automatically to localhost:5555
   - Receives real-time notifications
   - Auto-reconnect on disconnection
   - Message format: Topology handshake acknowledged.

BACKGROUND TASKS
================================================================================

The application runs several background tasks for continuous operation:

1. AutoSaveTask (60-second interval)
   - Automatically saves active session data
   - Runs on dedicated thread
   - Thread-safe with Platform.runLater()

2. ReservationUpdateTask (30-second interval)
   - Updates pending/confirmed reservation counts
   - Logs reservation status changes
   - Monitors active reservations

3. ReportGenerationTask (5-second interval)
   - Generates current analytics reports
   - Calculates revenue and statistics
   - Prepares data for dashboard display

4. NotificationRefreshTask (15-second interval)
   - Refreshes notification queue
   - Processes system notifications
   - Updates notification UI component

All tasks implement Runnable interface and are thread-safe.

CONFIGURATION
================================================================================

Application Configuration (in TravelERPApplication.java):

   - Database path: {user.home}/travelagency_db.db
   - Notification port: 5555
   - Auto-save interval: 60 seconds
   - Reservation update interval: 30 seconds
   - Notification refresh interval: 15 seconds

To modify configuration:
   1. Edit TravelERPApplication.java
   2. Update constant values
   3. Rebuild project: mvn clean package

LOGGING
================================================================================

The application uses SLF4J for logging. Logs are output to:
   - Console (DEBUG level)
   - File logs (if configured)

To adjust logging level, modify:
   - src/main/resources/logback.xml (if present)
   - Or configure via system properties

TROUBLESHOOTING
================================================================================

Problem: Application won't start
   Solution: Ensure Java 17+ is installed
      java -version
   Solution: Check Maven installation
      mvn -version

Problem: Database not found
   Solution: Application auto-creates database on first run
   Solution: Check file permissions on home directory
   Solution: Manual database path in DatabaseConnection.java

Problem: Cannot connect to localhost:5555
   Solution: Check firewall settings
   Solution: Ensure notification server started (check console logs)
   Solution: Try restarting application

Problem: UI elements not displaying correctly
   Solution: Update JavaFX libraries
   Solution: Check CSS stylesheet (css/styles.css)
   Solution: Verify FXML file references in controller classes

PERFORMANCE OPTIMIZATION
================================================================================

Recommendations for optimal performance:

1. Database:
   - Add indexes on frequently queried columns (already done in schema)
   - Use batch operations for bulk inserts
   - Regular database maintenance and cleanup

2. Memory:
   - Configure JVM heap size: -Xmx2048m -Xms512m
   - Monitor thread count in Task Manager
   - Dispose of unused resources promptly

3. UI:
   - Use TableView pagination for large datasets
   - Lazy-load resources when possible
   - Minimize layout recalculations

4. Network:
   - Keep notification socket connections open
   - Implement message batching for socket communication
   - Use compression for large data transfers

DEPLOYMENT
================================================================================

For production deployment:

1. Build Release JAR:
   mvn clean package -DskipTests

2. Copy JAR to deployment server:
   TravelERP-1.0-SNAPSHOT-shaded.jar

3. Set up database on production machine

4. Run with appropriate JVM settings:
   java -Xmx2048m -Xms512m -jar TravelERP-1.0-SNAPSHOT-shaded.jar

5. Configure for production environment:
   - Update database path
   - Adjust socket port if needed
   - Configure logging level

TESTING
================================================================================

To run unit tests:
   mvn test

To run tests with coverage:
   mvn test jacoco:report

Test results:
   target/surefire-reports/
   target/site/jacoco/

DEVELOPMENT GUIDELINES
================================================================================

Code Style:
   - Follow Java naming conventions
   - Use meaningful variable and method names
   - Keep methods focused and concise
   - Document complex logic with comments

Architecture Patterns:
   - MVC (Model-View-Controller)
   - DAO (Data Access Object)
   - Service Layer abstraction
   - Singleton for DatabaseConnection

Version Control:
   - Commit frequently with meaningful messages
   - Create feature branches for new features
   - Use pull requests for code review

Documentation:
   - Update README when adding features
   - Document new methods and classes
   - Maintain API documentation
   - Keep this file current

SUPPORT & CONTACT
================================================================================

For issues or questions:
   Email: support@travelagency-erp.com
   Documentation: See project repository
   Issue Tracker: [Project Repository URL]

Version: 1.0.0
Last Updated: 2024
Status: Production Ready

================================================================================
