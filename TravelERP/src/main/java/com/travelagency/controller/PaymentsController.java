package com.travelagency.controller;

import com.travelagency.model.Payment;
import com.travelagency.model.Reservation;
import com.travelagency.service.PaymentService;
import com.travelagency.service.ReservationService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.StringConverter;
import java.util.List;

/**
 * Payments Management Controller
 */
public class PaymentsController {

    @FXML
    private VBox mainContainer;

    private PaymentService paymentService;
    private ReservationService reservationService;
    private TableView<Payment> paymentsTable;
    private ObservableList<Payment> paymentsData;
    private VBox summarySection;

    @FXML
    public void initialize() {
        if (paymentService == null) {
            paymentService = new PaymentService();
        }
        if (reservationService == null) {
            reservationService = new ReservationService();
        }
        loadPayments();
    }

    /**
     * Set payment service
     */
    public void setPaymentService(PaymentService paymentService) {
        this.paymentService = paymentService;
        if (reservationService == null) {
            this.reservationService = new ReservationService();
        }
    }

    /**
     * Load payments
     */
    public void loadPayments() {
        if (mainContainer == null) return;

        mainContainer.getChildren().clear();
        mainContainer.setPadding(new Insets(20));
        mainContainer.setSpacing(15);
        mainContainer.setStyle("-fx-background-color: #ecf0f1;");

        // Title
        Label title = new Label("Payment Management");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        mainContainer.getChildren().add(title);

        // Summary section
        summarySection = createSummarySection();
        mainContainer.getChildren().add(summarySection);

        // Process payment section
        VBox paymentSection = createPaymentSection();
        mainContainer.getChildren().add(paymentSection);

        // Table
        createPaymentsTable();
        mainContainer.getChildren().add(paymentsTable);

        // Load data
        refreshPaymentsTable();
    }

    /**
     * Create summary section
     */
    private VBox createSummarySection() {
        VBox section = new VBox(10);
        section.setPadding(new Insets(15));
        section.setStyle("-fx-background-color: white; -fx-border-color: #bdc3c7; -fx-background-radius: 5;");

        Label sectionTitle = new Label("Payment Summary");
        sectionTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));

        HBox summaryRow = new HBox(20);
        
        double totalRevenue = paymentService.getTotalRevenue();
        VBox revenueBox = createSummaryBox("Total Revenue", "$" + String.format("%.2f", totalRevenue), "#27ae60");

        double pendingAmount = paymentService.getTotalPendingAmount();
        VBox pendingBox = createSummaryBox("Pending Amount", "$" + String.format("%.2f", pendingAmount), "#f39c12");

        int totalPayments = paymentService.getAllPayments().size();
        VBox paymentsBox = createSummaryBox("Total Payments", String.valueOf(totalPayments), "#3498db");

        summaryRow.getChildren().addAll(revenueBox, pendingBox, paymentsBox);
        section.getChildren().addAll(sectionTitle, summaryRow);
        return section;
    }

    /**
     * Create summary box
     */
    private VBox createSummaryBox(String title, String value, String color) {
        VBox box = new VBox(5);
        box.setPadding(new Insets(15));
        box.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 5;");

        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
        titleLabel.setStyle("-fx-text-fill: white;");

        Label valueLabel = new Label(value);
        valueLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        valueLabel.setStyle("-fx-text-fill: white;");

        box.getChildren().addAll(titleLabel, valueLabel);
        box.setPrefWidth(200);
        return box;
    }

    /**
     * Create payment section
     */
    private VBox createPaymentSection() {
        VBox section = new VBox(10);
        section.setPadding(new Insets(15));
        section.setStyle("-fx-background-color: white; -fx-border-color: #bdc3c7; -fx-background-radius: 5;");

        Label sectionTitle = new Label("Process Payment");
        sectionTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));

        HBox row1 = new HBox(10);
        ComboBox<Reservation> reservationCombo = new ComboBox<>();
        reservationCombo.setItems(FXCollections.observableArrayList(reservationService.getAllReservations()));
        reservationCombo.setPrefWidth(280);
        reservationCombo.setPromptText("Select Reservation");
        reservationCombo.setConverter(new StringConverter<>() {
            @Override
            public String toString(Reservation reservation) {
                return reservation == null ? "" : String.format("#%d - C:%d P:%d [%s]", reservation.getId(), reservation.getCustomerId(), reservation.getPackageId(), reservation.getStatus());
            }

            @Override
            public Reservation fromString(String string) {
                return null;
            }
        });
        reservationCombo.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(Reservation reservation, boolean empty) {
                super.updateItem(reservation, empty);
                setText(empty || reservation == null ? null : String.format("#%d - C:%d P:%d [%s]", reservation.getId(), reservation.getCustomerId(), reservation.getPackageId(), reservation.getStatus()));
            }
        });

        TextField amountField = createTextField("Amount", 150);
        ComboBox<String> methodCombo = new ComboBox<>();
        methodCombo.setItems(FXCollections.observableArrayList("CREDIT_CARD", "DEBIT_CARD", "BANK_TRANSFER", "CHECK"));
        methodCombo.setPrefWidth(150);
        methodCombo.setPromptText("Payment Method");

        row1.getChildren().addAll(
            new Label("Reservation:"), reservationCombo,
            new Label("Amount:"), amountField,
            new Label("Method:"), methodCombo
        );

        HBox row2 = new HBox(10);
        TextField transactionIdField = createTextField("Transaction ID", 200);
        transactionIdField.setText(paymentService.generateTransactionId());
        TextArea notesArea = new TextArea();
        notesArea.setPromptText("Payment Notes");
        notesArea.setPrefHeight(50);
        notesArea.setWrapText(true);

        Button processButton = new Button("Process Payment");
        processButton.setStyle("-fx-font-size: 12; -fx-padding: 8;");
        processButton.setOnAction(e -> {
            if (reservationCombo.getValue() == null) {
                showAlert("Error", "Please select a reservation.", Alert.AlertType.ERROR);
                return;
            }
            if (amountField.getText().isBlank()) {
                showAlert("Error", "Please enter an amount.", Alert.AlertType.ERROR);
                return;
            }
            if (methodCombo.getValue() == null) {
                showAlert("Error", "Please select a payment method.", Alert.AlertType.ERROR);
                return;
            }

            try {
                double amount = Double.parseDouble(amountField.getText().trim());
                if (amount <= 0) {
                    showAlert("Validation Error", "Amount must be a positive number.", Alert.AlertType.WARNING);
                    return;
                }

                Payment payment = new Payment();
                payment.setReservationId(reservationCombo.getValue().getId());
                payment.setAmount(amount);
                payment.setPaymentMethod(Payment.PaymentMethod.valueOf(methodCombo.getValue()));
                payment.setTransactionId(transactionIdField.getText().trim());
                payment.setNotes(notesArea.getText().trim());

                if (paymentService.processPayment(payment)) {
                    showAlert("Success", "Payment processed successfully.", Alert.AlertType.INFORMATION);
                    refreshPaymentsTable();
                    amountField.clear();
                    transactionIdField.setText(paymentService.generateTransactionId());
                    notesArea.clear();
                } else {
                    showAlert("Error", "Failed to process payment. Please check the reservation and try again.", Alert.AlertType.ERROR);
                }
            } catch (NumberFormatException ex) {
                showAlert("Validation Error", "Amount must be a valid number.", Alert.AlertType.WARNING);
            }
        });

        row2.getChildren().addAll(
            new Label("Transaction ID:"), transactionIdField,
            notesArea,
            processButton
        );

        section.getChildren().addAll(sectionTitle, row1, row2);
        return section;
    }

    /**
     * Create payments table
     */
    private void createPaymentsTable() {
        paymentsTable = new TableView<>();
        paymentsTable.setPrefHeight(420);
        paymentsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        paymentsTable.setPlaceholder(new Label("No payments available."));
        paymentsData = FXCollections.observableArrayList();
        paymentsTable.setItems(paymentsData);

        TableColumn<Payment, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getId()).asObject());

        TableColumn<Payment, Integer> reservationCol = new TableColumn<>("Reservation ID");
        reservationCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getReservationId()).asObject());

        TableColumn<Payment, Double> amountCol = new TableColumn<>("Amount");
        amountCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleDoubleProperty(cellData.getValue().getAmount()).asObject());

        TableColumn<Payment, String> methodCol = new TableColumn<>("Method");
        methodCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getPaymentMethod().toString()));

        TableColumn<Payment, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getPaymentStatus().toString()));

        TableColumn<Payment, String> transactionCol = new TableColumn<>("Transaction ID");
        transactionCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTransactionId()));

        paymentsTable.getColumns().addAll(idCol, reservationCol, amountCol, methodCol, statusCol, transactionCol);
    }

    /**
     * Refresh payments table
     */
    private void refreshPaymentsTable() {
        List<Payment> payments = paymentService.getAllPayments();
        paymentsData.setAll(payments);
        if (summarySection != null) {
            int summaryIndex = mainContainer.getChildren().indexOf(summarySection);
            if (summaryIndex >= 0) {
                mainContainer.getChildren().set(summaryIndex, createSummarySection());
            }
        }
    }

    /**
     * Create text field helper
     */
    private TextField createTextField(String prompt, int width) {
        TextField field = new TextField();
        field.setPromptText(prompt);
        field.setPrefWidth(width);
        return field;
    }

    /**
     * Show alert dialog
     */
    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
