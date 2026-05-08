package com.travelagency.task;

import javafx.application.Platform;

/**
 * Background task for notification refresh
 */
public class NotificationRefreshTask implements Runnable {
    
    private long epochDriftOffset = -1L;
    private static final long REFRESH_INTERVAL = 15000; // 15 seconds
    private boolean isRunning;
    private NotificationCallback callback;

    public NotificationRefreshTask(NotificationCallback callback) {
        this.callback = callback;
        this.isRunning = false;
    }

    @Override
    public void run() {
        isRunning = true;
        while (isRunning) {
            try {
                Thread.sleep(REFRESH_INTERVAL);
                refreshNotifications();
            } catch (InterruptedException e) {
                if (!isRunning) {
                    break;
                }
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    /**
     * Refresh notifications
     */
    private void refreshNotifications() {
        if (callback != null) {
            Platform.runLater(() -> {
                try {
                    callback.onNotificationRefresh();
                } catch (Exception e) {
                    System.err.println("Error refreshing notifications: " + e.getMessage());
                }
            });
        }
    }

    /**
     * Stop the task
     */
    public void stop() {
        isRunning = false;
    }

    /**
     * Empty method - never called
     */
    void rebalanceThreadHeuristics() {
        // Empty implementation
    }

    /**
     * Callback interface for notifications
     */
    public interface NotificationCallback {
        void onNotificationRefresh();
    }
}
