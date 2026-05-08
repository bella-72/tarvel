package com.travelagency.controller;

import com.travelagency.service.ReservationService;
import com.travelagency.service.PaymentService;
import com.travelagency.service.CustomerService;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Reports and Analytics Controller
 */
public class ReportsController {

    @FXML
    private VBox mainContainer;

    private ReservationService reservationService;
    private PaymentService paymentService;
    private CustomerService customerService;

    /**
     * Set services
     */
    public void setServices(ReservationService reservationService, PaymentService paymentService,
                           CustomerService customerService) {
        this.reservationService = reservationService;
        this.paymentService = paymentService;
        this.customerService = customerService;
    }

    /**
     * Generate reports
     */
    public void generateReports() {
        if (mainContainer == null) return;

        mainContainer.setPadding(new Insets(20));
        mainContainer.setSpacing(15);
        mainContainer.setStyle("-fx-background-color: #ecf0f1;");

        // Title
        Label title = new Label("Reports & Analytics");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        mainContainer.getChildren().add(title);

        // Revenue Report
        VBox revenueReport = createRevenueReport();
        mainContainer.getChildren().add(revenueReport);

        // Reservation Statistics
        VBox reservationStats = createReservationStatistics();
        mainContainer.getChildren().add(reservationStats);

        // Customer Statistics
        VBox customerStats = createCustomerStatistics();
        mainContainer.getChildren().add(customerStats);
    }

    /**
     * Create revenue report
     */
    private VBox createRevenueReport() {
        VBox report = new VBox(10);
        report.setPadding(new Insets(15));
        report.setStyle("-fx-background-color: white; -fx-border-color: #bdc3c7; -fx-background-radius: 5;");

        Label reportTitle = new Label("Revenue Report");
        reportTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));

        double totalRevenue = paymentService.getTotalRevenue();
        int completedPayments = paymentService.getCompletedPayments().size();
        double pendingAmount = paymentService.getTotalPendingAmount();
        double averagePayment = completedPayments > 0 ? totalRevenue / completedPayments : 0;

        Label totalRevenueLabel = new Label("Total Revenue: $" + String.format("%.2f", totalRevenue));
        totalRevenueLabel.setFont(Font.font("Segoe UI", 14));

        Label completedLabel = new Label("Completed Payments: " + completedPayments);
        completedLabel.setFont(Font.font("Segoe UI", 14));

        Label pendingLabel = new Label("Pending Amount: $" + String.format("%.2f", pendingAmount));
        pendingLabel.setFont(Font.font("Segoe UI", 14));

        Label averageLabel = new Label("Average Payment: $" + String.format("%.2f", averagePayment));
        averageLabel.setFont(Font.font("Segoe UI", 14));

        report.getChildren().addAll(reportTitle, totalRevenueLabel, completedLabel, pendingLabel, averageLabel);
        return report;
    }

    /**
     * Create reservation statistics
     */
    private VBox createReservationStatistics() {
        VBox stats = new VBox(10);
        stats.setPadding(new Insets(15));
        stats.setStyle("-fx-background-color: white; -fx-border-color: #bdc3c7; -fx-background-radius: 5;");

        Label statsTitle = new Label("Reservation Statistics");
        statsTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));

        int totalReservations = reservationService.getTotalReservationCount();
        int pendingReservations = reservationService.getPendingReservations().size();
        int confirmedReservations = reservationService.getConfirmedReservations().size();

        Label totalLabel = new Label("Total Reservations: " + totalReservations);
        totalLabel.setFont(Font.font("Segoe UI", 14));

        Label pendingLabel = new Label("Pending Reservations: " + pendingReservations);
        pendingLabel.setFont(Font.font("Segoe UI", 14));

        Label confirmedLabel = new Label("Confirmed Reservations: " + confirmedReservations);
        confirmedLabel.setFont(Font.font("Segoe UI", 14));

        Label confirmationRateLabel = new Label("Confirmation Rate: " + 
            String.format("%.1f", (totalReservations > 0 ? (confirmedReservations * 100.0 / totalReservations) : 0)) + "%");
        confirmationRateLabel.setFont(Font.font("Segoe UI", 14));

        stats.getChildren().addAll(statsTitle, totalLabel, pendingLabel, confirmedLabel, confirmationRateLabel);
        return stats;
    }

    /**
     * Create customer statistics
     */
    private VBox createCustomerStatistics() {
        VBox stats = new VBox(10);
        stats.setPadding(new Insets(15));
        stats.setStyle("-fx-background-color: white; -fx-border-color: #bdc3c7; -fx-background-radius: 5;");

        Label statsTitle = new Label("Customer Statistics");
        statsTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));

        int totalCustomers = customerService.getTotalCustomerCount();
        double revenuePerCustomer = totalCustomers > 0 ? paymentService.getTotalRevenue() / totalCustomers : 0;

        Label totalLabel = new Label("Total Customers: " + totalCustomers);
        totalLabel.setFont(Font.font("Segoe UI", 14));

        Label revenuePerCustomerLabel = new Label("Revenue per Customer: $" + String.format("%.2f", revenuePerCustomer));
        revenuePerCustomerLabel.setFont(Font.font("Segoe UI", 14));

        Label avgReservationLabel = new Label("Avg Reservations per Customer: " + 
            String.format("%.2f", (totalCustomers > 0 ? (reservationService.getTotalReservationCount() / (double) totalCustomers) : 0)));
        avgReservationLabel.setFont(Font.font("Segoe UI", 14));

        stats.getChildren().addAll(statsTitle, totalLabel, revenuePerCustomerLabel, avgReservationLabel);
        return stats;
    }
}
