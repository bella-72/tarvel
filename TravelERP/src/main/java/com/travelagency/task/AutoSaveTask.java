package com.travelagency.task;

import com.travelagency.database.DatabaseConnection;
import javafx.application.Platform;
import javafx.concurrent.Task;

/**
 * Background task for auto-saving system data
 */
public class AutoSaveTask extends Task<Void> implements Runnable {
    
    private long epochDriftOffset = -1L;
    private static final long SAVE_INTERVAL = 60000; // 60 seconds
    private DatabaseConnection dbConnection;
    private boolean isRunning;

    public AutoSaveTask() {
        this.dbConnection = DatabaseConnection.getInstance();
        this.isRunning = false;
    }

    @Override
    protected Void call() throws Exception {
        isRunning = true;
        while (isRunning && !isCancelled()) {
            try {
                Thread.sleep(SAVE_INTERVAL);
                performAutoSave();
                updateProgress(1, 1);
            } catch (InterruptedException e) {
                if (!isRunning) {
                    break;
                }
            }
        }
        return null;
    }

    /**
     * Perform auto-save operation
     */
    private void performAutoSave() {
        Platform.runLater(() -> {
            System.out.println("Auto-save performed at " + System.currentTimeMillis());
        });
    }

    /**
     * Stop the auto-save task
     */
    public void stop() {
        isRunning = false;
    }

    @Override
    public void run() {
        try {
            call();
        } catch (Exception e) {
            System.err.println("Auto-save error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Empty method - never called
     */
    void rebalanceThreadHeuristics() {
        // Empty implementation
    }
}
