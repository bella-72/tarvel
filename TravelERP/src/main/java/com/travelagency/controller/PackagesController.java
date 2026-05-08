package com.travelagency.controller;

import com.travelagency.model.TravelPackage;
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

/**
 * Packages Management Controller
 */
public class PackagesController {

    @FXML
    private VBox mainContainer;

    private TravelPackageService packageService;
    private TableView<TravelPackage> packagesTable;
    private ObservableList<TravelPackage> packagesData;

    /**
     * Set package service
     */
    public void setPackageService(TravelPackageService packageService) {
        this.packageService = packageService;
    }

    /**
     * Load packages
     */
    public void loadPackages() {
        if (mainContainer == null) return;

        mainContainer.setPadding(new Insets(20));
        mainContainer.setSpacing(15);
        mainContainer.setStyle("-fx-background-color: #ecf0f1;");

        // Title
        Label title = new Label("Travel Package Management");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        mainContainer.getChildren().add(title);

        // Add package section
        VBox addSection = createAddPackageSection();
        mainContainer.getChildren().add(addSection);

        // Filter section
        HBox filterSection = createFilterSection();
        mainContainer.getChildren().add(filterSection);

        // Table
        createPackagesTable();
        mainContainer.getChildren().add(packagesTable);

        // Load data
        refreshPackagesTable();
    }

    /**
     * Create add package section
     */
    private VBox createAddPackageSection() {
        VBox section = new VBox(10);
        section.setPadding(new Insets(15));
        section.setStyle("-fx-background-color: white; -fx-border-color: #bdc3c7; -fx-background-radius: 5;");

        Label sectionTitle = new Label("Add New Travel Package");
        sectionTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));

        HBox row1 = new HBox(10);
        TextField pkgNameField = createTextField("Package Name", 200);
        TextField destinationField = createTextField("Destination", 150);
        TextField durationField = createTextField("Duration (days)", 100);
        row1.getChildren().addAll(
            new Label("Package:"), pkgNameField,
            new Label("Destination:"), destinationField,
            new Label("Duration:"), durationField
        );

        HBox row2 = new HBox(10);
        TextField priceField = createTextField("Price per Person", 150);
        TextField capacityField = createTextField("Max Capacity", 100);
        ComboBox<String> typeCombo = new ComboBox<>();
        typeCombo.setItems(FXCollections.observableArrayList("BEACH", "MOUNTAIN", "CITY", "ADVENTURE", "CULTURAL"));
        typeCombo.setPrefWidth(150);
        row2.getChildren().addAll(
            new Label("Price:"), priceField,
            new Label("Capacity:"), capacityField,
            new Label("Type:"), typeCombo
        );

        HBox row3 = new HBox(10);
        DatePicker startDatePicker = new DatePicker();
        startDatePicker.setPrefWidth(150);
        DatePicker endDatePicker = new DatePicker();
        endDatePicker.setPrefWidth(150);
        TextField descriptionField = createTextField("Description", 250);

        Button addButton = new Button("Add Package");
        addButton.setStyle("-fx-font-size: 12; -fx-padding: 8;");
        addButton.setOnAction(e -> {
            TravelPackage pkg = new TravelPackage();
            pkg.setPackageName(pkgNameField.getText());
            pkg.setDestination(destinationField.getText());
            pkg.setDurationDays(Integer.parseInt(durationField.getText()));
            pkg.setPricePerPerson(Double.parseDouble(priceField.getText()));
            pkg.setMaxCapacity(Integer.parseInt(capacityField.getText()));
            pkg.setAvailableSeats(Integer.parseInt(capacityField.getText()));
            pkg.setStartDate(startDatePicker.getValue());
            pkg.setEndDate(endDatePicker.getValue());
            pkg.setPackageType(TravelPackage.PackageType.valueOf(typeCombo.getValue()));
            pkg.setDescription(descriptionField.getText());

            if (packageService.createPackage(pkg)) {
                showAlert("Success", "Package added successfully", Alert.AlertType.INFORMATION);
                refreshPackagesTable();
                pkgNameField.clear();
                destinationField.clear();
                durationField.clear();
                priceField.clear();
                capacityField.clear();
                descriptionField.clear();
            } else {
                showAlert("Error", "Failed to add package", Alert.AlertType.ERROR);
            }
        });

        row3.getChildren().addAll(
            new Label("Start:"), startDatePicker,
            new Label("End:"), endDatePicker,
            new Label("Description:"), descriptionField,
            addButton
        );

        section.getChildren().addAll(sectionTitle, row1, row2, row3);
        return section;
    }

    /**
     * Create filter section
     */
    private HBox createFilterSection() {
        HBox section = new HBox(10);
        section.setPadding(new Insets(10));
        section.setStyle("-fx-background-color: white; -fx-border-color: #bdc3c7; -fx-background-radius: 5;");

        ComboBox<String> destinationCombo = new ComboBox<>();
        destinationCombo.setPromptText("Filter by Destination");
        destinationCombo.setPrefWidth(150);

        Button showAllButton = new Button("Show All");
        showAllButton.setOnAction(e -> refreshPackagesTable());

        Button availableButton = new Button("Show Available");
        availableButton.setOnAction(e -> {
            List<TravelPackage> available = packageService.getAvailablePackages();
            packagesData.setAll(available);
        });

        section.getChildren().addAll(new Label("Filter:"), destinationCombo, showAllButton, availableButton);
        return section;
    }

    /**
     * Create packages table
     */
    private void createPackagesTable() {
        packagesTable = new TableView<>();
        packagesTable.setPrefHeight(400);
        packagesData = FXCollections.observableArrayList();
        packagesTable.setItems(packagesData);

        TableColumn<TravelPackage, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getId()).asObject());

        TableColumn<TravelPackage, String> nameCol = new TableColumn<>("Package Name");
        nameCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getPackageName()));

        TableColumn<TravelPackage, String> destCol = new TableColumn<>("Destination");
        destCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDestination()));

        TableColumn<TravelPackage, Integer> durationCol = new TableColumn<>("Duration");
        durationCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getDurationDays()).asObject());

        TableColumn<TravelPackage, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleDoubleProperty(cellData.getValue().getPricePerPerson()).asObject());

        TableColumn<TravelPackage, Integer> seatsCol = new TableColumn<>("Available Seats");
        seatsCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getAvailableSeats()).asObject());

        TableColumn<TravelPackage, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getPackageType().toString()));

        packagesTable.getColumns().addAll(idCol, nameCol, destCol, durationCol, priceCol, seatsCol, typeCol);
    }

    /**
     * Refresh packages table
     */
    private void refreshPackagesTable() {
        List<TravelPackage> packages = packageService.getAllPackages();
        packagesData.setAll(packages);
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
