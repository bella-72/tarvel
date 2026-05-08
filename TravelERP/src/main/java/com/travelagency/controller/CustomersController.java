package com.travelagency.controller;

import com.travelagency.model.Customer;
import com.travelagency.service.CustomerService;
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
import java.time.LocalDateTime;
import java.util.List;

/**
 * Customers Management Controller
 */
public class CustomersController {

    @FXML
    private VBox mainContainer;

    private CustomerService customerService;
    private TableView<Customer> customersTable;
    private ObservableList<Customer> customersData;

    /**
     * Set customer service
     */
    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }

    /**
     * Load customers
     */
    public void loadCustomers() {
        if (mainContainer == null) return;

        mainContainer.setPadding(new Insets(20));
        mainContainer.setSpacing(15);
        mainContainer.setStyle("-fx-background-color: #ecf0f1;");

        // Title
        Label title = new Label("Customer Management");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        mainContainer.getChildren().add(title);

        // Add customer section
        VBox addSection = createAddCustomerSection();
        mainContainer.getChildren().add(addSection);

        // Search section
        HBox searchSection = createSearchSection();
        mainContainer.getChildren().add(searchSection);

        // Table
        createCustomersTable();
        mainContainer.getChildren().add(customersTable);

        // Load data
        refreshCustomersTable();
    }

    /**
     * Create add customer section
     */
    private VBox createAddCustomerSection() {
        VBox section = new VBox(10);
        section.setPadding(new Insets(15));
        section.setStyle("-fx-background-color: white; -fx-border-color: #bdc3c7; -fx-background-radius: 5;");

        Label sectionTitle = new Label("Add New Customer");
        sectionTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));

        HBox fieldsRow1 = new HBox(10);
        TextField firstNameField = createTextField("First Name", 150);
        TextField lastNameField = createTextField("Last Name", 150);
        TextField emailField = createTextField("Email", 200);
        fieldsRow1.getChildren().addAll(
            new Label("First Name:"), firstNameField,
            new Label("Last Name:"), lastNameField,
            new Label("Email:"), emailField
        );

        HBox fieldsRow2 = new HBox(10);
        TextField phoneField = createTextField("Phone", 150);
        TextField addressField = createTextField("Address", 200);
        TextField countryField = createTextField("Country", 150);
        fieldsRow2.getChildren().addAll(
            new Label("Phone:"), phoneField,
            new Label("Address:"), addressField,
            new Label("Country:"), countryField
        );

        HBox fieldsRow3 = new HBox(10);
        TextField passportField = createTextField("Passport", 150);
        DatePicker dobPicker = new DatePicker();
        dobPicker.setPrefWidth(150);
        TextField genderField = createTextField("Gender (M/F/O)", 100);

        Button addButton = new Button("Add Customer");
        addButton.setStyle("-fx-font-size: 12; -fx-padding: 8;");
        addButton.setOnAction(e -> {
            Customer customer = new Customer();
            customer.setFirstName(firstNameField.getText());
            customer.setLastName(lastNameField.getText());
            customer.setEmail(emailField.getText());
            customer.setPhone(phoneField.getText());
            customer.setAddress(addressField.getText());
            customer.setCountry(countryField.getText());
            customer.setPassportNumber(passportField.getText());
            customer.setDateOfBirth(dobPicker.getValue());
            customer.setGender(genderField.getText());

            if (customerService.createCustomer(customer)) {
                showAlert("Success", "Customer added successfully", Alert.AlertType.INFORMATION);
                refreshCustomersTable();
                firstNameField.clear();
                lastNameField.clear();
                emailField.clear();
                phoneField.clear();
                addressField.clear();
                countryField.clear();
                passportField.clear();
                genderField.clear();
            } else {
                showAlert("Error", "Failed to add customer", Alert.AlertType.ERROR);
            }
        });

        fieldsRow3.getChildren().addAll(
            new Label("Passport:"), passportField,
            new Label("DOB:"), dobPicker,
            new Label("Gender:"), genderField,
            addButton
        );

        section.getChildren().addAll(sectionTitle, fieldsRow1, fieldsRow2, fieldsRow3);
        return section;
    }

    /**
     * Create search section
     */
    private HBox createSearchSection() {
        HBox section = new HBox(10);
        section.setPadding(new Insets(10));
        section.setStyle("-fx-background-color: white; -fx-border-color: #bdc3c7; -fx-background-radius: 5;");

        TextField searchField = new TextField();
        searchField.setPromptText("Search by name or email...");
        searchField.setPrefWidth(300);

        Button searchButton = new Button("Search");
        searchButton.setOnAction(e -> {
            String searchTerm = searchField.getText();
            if (!searchTerm.isEmpty()) {
                List<Customer> results = customerService.searchCustomers(searchTerm);
                customersData.setAll(results);
            }
        });

        Button refreshButton = new Button("Refresh");
        refreshButton.setOnAction(e -> refreshCustomersTable());

        section.getChildren().addAll(new Label("Search:"), searchField, searchButton, refreshButton);
        return section;
    }

    /**
     * Create customers table
     */
    private void createCustomersTable() {
        customersTable = new TableView<>();
        customersTable.setPrefHeight(400);
        customersData = FXCollections.observableArrayList();
        customersTable.setItems(customersData);

        TableColumn<Customer, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getId()).asObject());

        TableColumn<Customer, String> firstNameCol = new TableColumn<>("First Name");
        firstNameCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getFirstName()));

        TableColumn<Customer, String> lastNameCol = new TableColumn<>("Last Name");
        lastNameCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getLastName()));

        TableColumn<Customer, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getEmail()));

        TableColumn<Customer, String> phoneCol = new TableColumn<>("Phone");
        phoneCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getPhone()));

        TableColumn<Customer, String> countryCol = new TableColumn<>("Country");
        countryCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getCountry()));

        customersTable.getColumns().addAll(idCol, firstNameCol, lastNameCol, emailCol, phoneCol, countryCol);
    }

    /**
     * Refresh customers table
     */
    private void refreshCustomersTable() {
        List<Customer> customers = customerService.getAllCustomers();
        customersData.setAll(customers);
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
