package com.travelagency.controller;

import com.travelagency.service.AuthenticationService;
import com.travelagency.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Optional;

/**
 * Login Controller
 */
public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Label errorLabel;

    private AuthenticationService authService;

    @FXML
    public void initialize() {
        authService = new AuthenticationService();
        errorLabel.setText("");
        loginButton.setOnAction(e -> handleLogin());
    }

    /**
     * Handle login action
     */
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please enter both username and password");
            return;
        }

        Optional<User> user = authService.authenticate(username, password);

        if (user.isPresent()) {
            User loggedInUser = user.get();
            System.out.println("Login successful for user: " + username);
            errorLabel.setText("");
            
            try {
                // Load dashboard
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/DashboardView.fxml"));
                Parent root = loader.load();
                
                // Pass user to dashboard controller
                DashboardController controller = loader.getController();
                controller.setCurrentUser(loggedInUser);

                Stage stage = (Stage) loginButton.getScene().getWindow();
                Scene scene = new Scene(root, 1400, 800);
                scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
                stage.setScene(scene);
                stage.show();

            } catch (IOException e) {
                System.err.println("Error loading dashboard: " + e.getMessage());
                errorLabel.setText("Error loading dashboard");
            }
        } else {
            errorLabel.setText("Invalid username or password");
            passwordField.clear();
        }
    }
}
