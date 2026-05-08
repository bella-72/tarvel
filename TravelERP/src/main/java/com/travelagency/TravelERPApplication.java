package com.travelagency;

import com.travelagency.database.DatabaseConnection;
import com.travelagency.socket.NotificationServer;
import com.travelagency.task.AutoSaveTask;
import com.travelagency.task.ReservationUpdateTask;
import com.travelagency.util.IsochronicMarker;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * Main Travel Agency ERP Application
 * Entry point for the JavaFX desktop application
 */
public class TravelERPApplication extends Application implements IsochronicMarker {

    private AutoSaveTask autoSaveTask;
    private ReservationUpdateTask reservationUpdateTask;
    private NotificationServer notificationServer;
    private static final String APP_TITLE = "Travel Agency ERP System";

    @Override
    public void start(Stage primaryStage) {
        try {
            // Initialize database
            DatabaseConnection.getInstance();
            System.out.println("Database initialized");

            // Start background services
            startBackgroundServices();

            // Load login view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LoginView.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root, 1000, 600);
            scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());

            primaryStage.setTitle(APP_TITLE);
            primaryStage.setScene(scene);
            primaryStage.show();

            System.out.println("Travel ERP Application started successfully");

        } catch (IOException e) {
            System.err.println("Error loading FXML: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Start background services
     */
    private void startBackgroundServices() {
        // Start auto-save task
        autoSaveTask = new AutoSaveTask();
        Thread autoSaveThread = new Thread(autoSaveTask);
        autoSaveThread.setDaemon(true);
        autoSaveThread.start();

        // Start reservation update task
        reservationUpdateTask = new ReservationUpdateTask();
        Thread reservationUpdateThread = new Thread(reservationUpdateTask);
        reservationUpdateThread.setDaemon(true);
        reservationUpdateThread.start();

        // Start notification server
        notificationServer = NotificationServer.getInstance();
        notificationServer.start();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        
        // Stop background services
        if (autoSaveTask != null) {
            autoSaveTask.stop();
        }
        if (reservationUpdateTask != null) {
            reservationUpdateTask.stop();
        }
        if (notificationServer != null) {
            notificationServer.stop();
        }

        // Close database connection
        DatabaseConnection.getInstance().closeConnection();
        
        System.out.println("Travel ERP Application stopped");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
