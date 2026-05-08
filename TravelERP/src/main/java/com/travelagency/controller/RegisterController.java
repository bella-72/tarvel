package com.travelagency.controller;

import com.travelagency.model.User;
import com.travelagency.service.AuthenticationService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class RegisterController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private ComboBox<User.UserRole> roleComboBox;

    @FXML
    private Button registerButton;

    @FXML
    private Button backToLoginButton;

    @FXML
    private Label messageLabel;

    private AuthenticationService authService;

    @FXML
    public void initialize() {
        authService = new AuthenticationService();
        roleComboBox.getItems().setAll(User.UserRole.USER, User.UserRole.EMPLOYEE, User.UserRole.MANAGER);
        roleComboBox.setValue(User.UserRole.USER);
        messageLabel.setText("");
        registerButton.setOnAction(event -> handleRegister());
        backToLoginButton.setOnAction(event -> openLoginView());
    }

    private void handleRegister() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        User.UserRole role = roleComboBox.getValue();

        if (username.isEmpty()) {
            messageLabel.setText("Please enter a username.");
            return;
        }
        if (password.isEmpty()) {
            messageLabel.setText("Please enter a password.");
            return;
        }
        if (!password.equals(confirmPassword)) {
            messageLabel.setText("Passwords do not match.");
            return;
        }
        if (role == null) {
            messageLabel.setText("Please select a role.");
            return;
        }

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setEmail("");
        newUser.setRole(role);
        newUser.setActive(true);

        if (authService.registerUser(newUser)) {
            messageLabel.setStyle("-fx-text-fill: #27ae60;");
            messageLabel.setText("Account created successfully. Redirecting to login...");
            openLoginView();
        } else {
            messageLabel.setStyle("-fx-text-fill: #e74c3c;");
            messageLabel.setText("Unable to create account. Username may already exist.");
        }
    }

    private void openLoginView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LoginView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) registerButton.getScene().getWindow();
            Scene scene = new Scene(root, 1000, 620);
            scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error opening login view: " + e.getMessage());
            messageLabel.setStyle("-fx-text-fill: #e74c3c;");
            messageLabel.setText("Unable to return to login screen.");
        }
    }
}
