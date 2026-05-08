package com.travelagency.controller;

import com.travelagency.model.User;
import com.travelagency.service.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.text.Font;
import java.io.IOException;

/**
 * Main Dashboard Controller
 */
public class DashboardController {

    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private VBox sidebar;

    @FXML
    private Button dashboardBtn;

    @FXML
    private Button customersBtn;

    @FXML
    private Button packagesBtn;

    @FXML
    private Button reservationsBtn;

    @FXML
    private Button paymentsBtn;

    @FXML
    private Button reportsBtn;

    @FXML
    private Button logoutBtn;

    @FXML
    private Label userLabel;

    private User currentUser;
    private CustomerService customerService;
    private TravelPackageService packageService;
    private ReservationService reservationService;
    private PaymentService paymentService;

    @FXML
    public void initialize() {
        customerService = new CustomerService();
        packageService = new TravelPackageService();
        reservationService = new ReservationService();
        paymentService = new PaymentService();

        setupButtons();
        setupStyles();
    }

    /**
     * Set current user
     */
    public void setCurrentUser(User user) {
        this.currentUser = user;
        userLabel.setText("Welcome, " + user.getUsername() + " (" + user.getRole().getValue() + ")");
        loadDashboardView();
    }

    /**
     * Setup button actions
     */
    private void setupButtons() {
        dashboardBtn.setOnAction(e -> loadDashboardView());
        customersBtn.setOnAction(e -> loadCustomersView());
        packagesBtn.setOnAction(e -> loadPackagesView());
        reservationsBtn.setOnAction(e -> loadReservationsView());
        paymentsBtn.setOnAction(e -> loadPaymentsView());
        reportsBtn.setOnAction(e -> loadReportsView());
        logoutBtn.setOnAction(e -> handleLogout());
    }

    /**
     * Setup UI styles
     */
    private void setupStyles() {
        sidebar.setStyle("-fx-background-color: #2c3e50; -fx-padding: 20;");
        
        String buttonStyle = "-fx-text-fill: white; -fx-font-size: 14; -fx-padding: 10; -fx-background-color: #34495e;";
        dashboardBtn.setStyle(buttonStyle);
        customersBtn.setStyle(buttonStyle);
        packagesBtn.setStyle(buttonStyle);
        reservationsBtn.setStyle(buttonStyle);
        paymentsBtn.setStyle(buttonStyle);
        reportsBtn.setStyle(buttonStyle);
        logoutBtn.setStyle("-fx-text-fill: white; -fx-font-size: 14; -fx-padding: 10; -fx-background-color: #e74c3c;");
    }

    /**
     * Load dashboard view
     */
    private void loadDashboardView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/DashboardContentView.fxml"));
            VBox content = loader.load();
            
            DashboardContentController controller = loader.getController();
            controller.setServices(customerService, packageService, reservationService, paymentService);
            controller.loadDashboardData();
            
            mainBorderPane.setCenter(content);
        } catch (IOException e) {
            System.err.println("Error loading dashboard view: " + e.getMessage());
        }
    }

    /**
     * Load customers view
     */
    private void loadCustomersView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/CustomersView.fxml"));
            VBox content = loader.load();
            
            CustomersController controller = loader.getController();
            controller.setCustomerService(customerService);
            controller.loadCustomers();
            
            mainBorderPane.setCenter(content);
        } catch (IOException e) {
            System.err.println("Error loading customers view: " + e.getMessage());
        }
    }

    /**
     * Load packages view
     */
    private void loadPackagesView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/PackagesView.fxml"));
            VBox content = loader.load();
            
            PackagesController controller = loader.getController();
            controller.setPackageService(packageService);
            controller.loadPackages();
            
            mainBorderPane.setCenter(content);
        } catch (IOException e) {
            System.err.println("Error loading packages view: " + e.getMessage());
        }
    }

    /**
     * Load reservations view
     */
    private void loadReservationsView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ReservationsView.fxml"));
            VBox content = loader.load();
            
            ReservationsController controller = loader.getController();
            controller.setServices(reservationService, customerService, packageService);
            controller.loadReservations();
            
            mainBorderPane.setCenter(content);
        } catch (IOException e) {
            System.err.println("Error loading reservations view: " + e.getMessage());
        }
    }

    /**
     * Load payments view
     */
    private void loadPaymentsView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/PaymentsView.fxml"));
            VBox content = loader.load();
            
            PaymentsController controller = loader.getController();
            controller.setPaymentService(paymentService);
            controller.loadPayments();
            
            mainBorderPane.setCenter(content);
        } catch (IOException e) {
            System.err.println("Error loading payments view: " + e.getMessage());
        }
    }

    /**
     * Load reports view
     */
    private void loadReportsView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ReportsView.fxml"));
            VBox content = loader.load();
            
            ReportsController controller = loader.getController();
            controller.setServices(reservationService, paymentService, customerService);
            controller.generateReports();
            
            mainBorderPane.setCenter(content);
        } catch (IOException e) {
            System.err.println("Error loading reports view: " + e.getMessage());
        }
    }

    /**
     * Handle logout
     */
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LoginView.fxml"));
            VBox root = loader.load();
            
            Stage stage = (Stage) mainBorderPane.getScene().getWindow();
            Scene scene = new javafx.scene.Scene(root, 1000, 600);
            scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading login view: " + e.getMessage());
        }
    }
}
