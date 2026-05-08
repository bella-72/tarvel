package com.travelagency.controller;

import com.travelagency.model.Reservation;
import com.travelagency.model.Customer;
import com.travelagency.model.TravelPackage;
import com.travelagency.service.ReservationService;
import com.travelagency.service.CustomerService;
import com.travelagency.service.TravelPackageService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Reservations Management Controller
 */
public class ReservationsController {

    @FXML
    private VBox mainContainer;

    private ReservationService reservationService;
    private CustomerService customerService;
    private TravelPackageService packageService;
    private TableView<Reservation> reservationsTable;
    private ObservableList<Reservation> reservationsData;

    /**
     * Set services
     */
    public void setServices(ReservationService reservationService, CustomerService customerService,
                           TravelPackageService packageService) {
        this.reservationService = reservationService;
        this.customerService = customerService;
        this.packageService = packageService;
    }

    /**
     * Load reservations
     */
    public void loadReservations() {
        if (mainContainer == null) return;

        mainContainer.setPadding(new Insets(20));
        mainContainer.setSpacing(15);
        mainContainer.setStyle("-fx-background-color: #ecf0f1;");

        // Title
        Label title = new Label("Reservation Management");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        mainContainer.getChildren().add(title);

        // Create reservation section
        VBox createSection = createReservationSection();
        mainContainer.getChildren().add(createSection);

        // Filter section
        HBox filterSection = createFilterSection();
        mainContainer.getChildren().add(filterSection);

        // Table
        createReservationsTable();
        mainContainer.getChildren().add(reservationsTable);

        // Load data
        refreshReservationsTable();
    }

    /**
     * Create reservation section
     */
    private VBox createReservationSection() {
        VBox section = new VBox(10);
        section.setPadding(new Insets(15));
        section.setStyle("-fx-background-color: white; -fx-border-color: #bdc3c7; -fx-background-radius: 5;");

        Label sectionTitle = new Label("Create New Reservation");
        sectionTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));

        HBox row1 = new HBox(10);
        ComboBox<Customer> customerCombo = new ComboBox<>();
        customerCombo.setItems(FXCollections.observableArrayList(customerService.getAllCustomers()));
        customerCombo.setPrefWidth(200);
        customerCombo.setPromptText("Select Customer");

        ComboBox<TravelPackage> packageCombo = new ComboBox<>();
        packageCombo.setItems(FXCollections.observableArrayList(packageService.getAvailablePackages()));
        packageCombo.setPrefWidth(200);
        packageCombo.setPromptText("Select Package");

        TextField travelersField = createTextField("Number of Travelers", 100);
        row1.getChildren().addAll(
            new Label("Customer:"), customerCombo,
            new Label("Package:"), packageCombo,
            new Label("Travelers:"), travelersField
        );

        HBox row2 = new HBox(10);
        DatePicker resDatePicker = new DatePicker();
        resDatePicker.setValue(LocalDate.now());
        resDatePicker.setPrefWidth(150);

        TextArea specialReqArea = new TextArea();
        specialReqArea.setPromptText("Special Requirements");
        specialReqArea.setPrefHeight(60);
        specialReqArea.setWrapText(true);

        Button createButton = new Button("Create Reservation");
        createButton.setStyle("-fx-font-size: 12; -fx-padding: 8;");
        createButton.setOnAction(e -> {
            if (customerCombo.getValue() == null || packageCombo.getValue() == null) {
                showAlert("Error", "Please select customer and package", Alert.AlertType.ERROR);
                return;
            }

            Reservation res = new Reservation();
            res.setCustomerId(customerCombo.getValue().getId());
            res.setPackageId(packageCombo.getValue().getId());
            res.setReservationDate(resDatePicker.getValue());
            res.setNumberOfTravelers(Integer.parseInt(travelersField.getText()));
            res.setTotalPrice(packageCombo.getValue().getPricePerPerson() * Integer.parseInt(travelersField.getText()));
            res.setStatus(Reservation.ReservationStatus.PENDING);
            res.setSpecialRequirements(specialReqArea.getText());

            if (reservationService.createReservation(res)) {
                showAlert("Success", "Reservation created successfully", Alert.AlertType.INFORMATION);
                refreshReservationsTable();
                travelersField.clear();
                specialReqArea.clear();
            } else {
                showAlert("Error", "Failed to create reservation", Alert.AlertType.ERROR);
            }
        });

        row2.getChildren().addAll(
            new Label("Date:"), resDatePicker,
            specialReqArea,
            createButton
        );

        section.getChildren().addAll(sectionTitle, row1, row2);
        return section;
    }

    /**
     * Create filter section
     */
    private HBox createFilterSection() {
        HBox section = new HBox(10);
        section.setPadding(new Insets(10));
        section.setStyle("-fx-background-color: white; -fx-border-color: #bdc3c7; -fx-background-radius: 5;");

        ComboBox<String> statusCombo = new ComboBox<>();
        statusCombo.setItems(FXCollections.observableArrayList("ALL", "PENDING", "CONFIRMED", "CANCELLED", "COMPLETED"));
        statusCombo.setValue("ALL");
        statusCombo.setPrefWidth(150);

        Button filterButton = new Button("Filter");
        filterButton.setOnAction(e -> {
            String status = statusCombo.getValue();
            if ("PENDING".equals(status)) {
                reservationsData.setAll(reservationService.getPendingReservations());
            } else if ("CONFIRMED".equals(status)) {
                reservationsData.setAll(reservationService.getConfirmedReservations());
            } else {
                refreshReservationsTable();
            }
        });

        Button refreshButton = new Button("Refresh");
        refreshButton.setOnAction(e -> refreshReservationsTable());

        section.getChildren().addAll(new Label("Status:"), statusCombo, filterButton, refreshButton);
        return section;
    }

    /**
     * Create reservations table
     */
    private void createReservationsTable() {
        reservationsTable = new TableView<>();
        reservationsTable.setPrefHeight(400);
        reservationsData = FXCollections.observableArrayList();
        reservationsTable.setItems(reservationsData);

        TableColumn<Reservation, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getId()).asObject());

        TableColumn<Reservation, Integer> customerCol = new TableColumn<>("Customer ID");
        customerCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getCustomerId()).asObject());

        TableColumn<Reservation, Integer> packageCol = new TableColumn<>("Package ID");
        packageCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getPackageId()).asObject());

        TableColumn<Reservation, Integer> travelersCol = new TableColumn<>("Travelers");
        travelersCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getNumberOfTravelers()).asObject());

        TableColumn<Reservation, Double> priceCol = new TableColumn<>("Total Price");
        priceCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleDoubleProperty(cellData.getValue().getTotalPrice()).asObject());

        TableColumn<Reservation, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getStatus().toString()));

        TableColumn<Reservation, String> actionCol = new TableColumn<>("Actions");
        actionCol.setCellFactory(param -> new TableCell<Reservation, String>() {
            private final Button confirmBtn = new Button("Confirm");
            private final Button cancelBtn = new Button("Cancel");

            {
                confirmBtn.setOnAction(e -> {
                    Reservation res = getTableView().getItems().get(getIndex());
                    if (reservationService.confirmReservation(res.getId())) {
                        showAlert("Success", "Reservation confirmed", Alert.AlertType.INFORMATION);
                        refreshReservationsTable();
                    }
                });
                cancelBtn.setOnAction(e -> {
                    Reservation res = getTableView().getItems().get(getIndex());
                    if (reservationService.cancelReservation(res.getId())) {
                        showAlert("Success", "Reservation cancelled", Alert.AlertType.INFORMATION);
                        refreshReservationsTable();
                    }
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox hbox = new HBox(5, confirmBtn, cancelBtn);
                    setGraphic(hbox);
                }
            }
        });

        reservationsTable.getColumns().addAll(idCol, customerCol, packageCol, travelersCol, priceCol, statusCol, actionCol);
    }

    /**
     * Refresh reservations table
     */
    private void refreshReservationsTable() {
        List<Reservation> reservations = reservationService.getAllReservations();
        reservationsData.setAll(reservations);
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
