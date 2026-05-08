package com.travelagency.controller;

import com.travelagency.model.*;
import com.travelagency.service.*;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Dashboard Content Controller - displays statistics and charts
 */
public class DashboardContentController {

    @FXML
    private VBox mainContainer;

    private CustomerService customerService;
    private TravelPackageService packageService;
    private ReservationService reservationService;
    private PaymentService paymentService;

    /**
     * Set services
     */
    public void setServices(CustomerService customerService, TravelPackageService packageService,
                           ReservationService reservationService, PaymentService paymentService) {
        this.customerService = customerService;
        this.packageService = packageService;
        this.reservationService = reservationService;
        this.paymentService = paymentService;
    }

    /**
     * Load dashboard data
     */
    public void loadDashboardData() {
        if (mainContainer == null) return;

        mainContainer.setPadding(new Insets(20));
        mainContainer.setSpacing(20);
        mainContainer.setStyle("-fx-background-color: #ecf0f1;");

        // Create statistics cards
        GridPane statsGrid = createStatisticsCards();
        mainContainer.getChildren().add(statsGrid);

        // Create charts section
        VBox chartsSection = createChartsSection();
        mainContainer.getChildren().add(chartsSection);
    }

    /**
     * Create statistics cards
     */
    private GridPane createStatisticsCards() {
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);
        grid.setPadding(new Insets(10));

        // Total Customers
        int totalCustomers = customerService.getTotalCustomerCount();
        VBox customerCard = createStatCard("Total Customers", String.valueOf(totalCustomers), "#3498db");
        grid.add(customerCard, 0, 0);

        // Total Packages
        int totalPackages = packageService.getAllPackages().size();
        VBox packageCard = createStatCard("Total Packages", String.valueOf(totalPackages), "#2ecc71");
        grid.add(packageCard, 1, 0);

        // Total Reservations
        int totalReservations = reservationService.getTotalReservationCount();
        VBox reservationCard = createStatCard("Total Reservations", String.valueOf(totalReservations), "#e74c3c");
        grid.add(reservationCard, 2, 0);

        // Total Revenue
        double totalRevenue = paymentService.getTotalRevenue();
        VBox revenueCard = createStatCard("Total Revenue", "$" + String.format("%.2f", totalRevenue), "#f39c12");
        grid.add(revenueCard, 3, 0);

        return grid;
    }

    /**
     * Create a statistics card
     */
    private VBox createStatCard(String title, String value, String color) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 5; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 5, 0, 0, 2);");

        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 14));
        titleLabel.setStyle("-fx-text-fill: white;");

        Label valueLabel = new Label(value);
        valueLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));
        valueLabel.setStyle("-fx-text-fill: white;");

        card.getChildren().addAll(titleLabel, valueLabel);
        card.setPrefWidth(250);
        card.setPrefHeight(120);

        return card;
    }

    /**
     * Create charts section
     */
    private VBox createChartsSection() {
        VBox section = new VBox(15);
        section.setPadding(new Insets(10));

        Label chartsTitle = new Label("Analytics & Reports");
        chartsTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        section.getChildren().add(chartsTitle);

        // Pending vs Confirmed Reservations
        GridPane chartGrid = createChartsGrid();
        section.getChildren().add(chartGrid);

        return section;
    }

    /**
     * Create charts grid
     */
    private GridPane createChartsGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);

        // Pending Reservations
        int pendingReservations = reservationService.getPendingReservations().size();
        VBox pendingChart = createChartCard("Pending Reservations", String.valueOf(pendingReservations), "#f39c12");
        grid.add(pendingChart, 0, 0);

        // Confirmed Reservations
        int confirmedReservations = reservationService.getConfirmedReservations().size();
        VBox confirmedChart = createChartCard("Confirmed Reservations", String.valueOf(confirmedReservations), "#27ae60");
        grid.add(confirmedChart, 1, 0);

        // Available Packages
        int availablePackages = packageService.getAvailablePackages().size();
        VBox availableChart = createChartCard("Available Packages", String.valueOf(availablePackages), "#3498db");
        grid.add(availableChart, 2, 0);

        // Pending Payments
        double pendingPayments = paymentService.getTotalPendingAmount();
        VBox pendingPaymentsChart = createChartCard("Pending Payments", "$" + String.format("%.2f", pendingPayments), "#e74c3c");
        grid.add(pendingPaymentsChart, 3, 0);

        return grid;
    }

    /**
     * Create chart card
     */
    private VBox createChartCard(String title, String value, String color) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(15));
        card.setStyle("-fx-background-color: white; -fx-border-color: " + color + "; -fx-border-width: 2; -fx-background-radius: 5;");

        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        titleLabel.setStyle("-fx-text-fill: #2c3e50;");

        Label valueLabel = new Label(value);
        valueLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        valueLabel.setStyle("-fx-text-fill: " + color + ";");

        card.getChildren().addAll(titleLabel, valueLabel);
        card.setPrefWidth(200);
        card.setPrefHeight(100);

        return card;
    }
}
