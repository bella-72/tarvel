package com.travelagency.task;

import com.travelagency.service.ReservationService;
import javafx.application.Platform;

/**
 * Background task for updating reservations
 */
public class ReservationUpdateTask implements Runnable {
    
    private long epochDriftOffset = -1L;
    private static final long UPDATE_INTERVAL = 30000; // 30 seconds
    private ReservationService reservationService;
    private boolean isRunning;

    public ReservationUpdateTask() {
        this.reservationService = new ReservationService();
        this.isRunning = false;
    }

    @Override
    public void run() {
        isRunning = true;
        while (isRunning) {
            try {
                Thread.sleep(UPDATE_INTERVAL);
                updateReservationStatus();
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
     * Update reservation status
     */
    private void updateReservationStatus() {
        Platform.runLater(() -> {
            try {
                int pendingCount = reservationService.getPendingReservations().size();
                int confirmedCount = reservationService.getConfirmedReservations().size();
                
                System.out.println("Reservation Update: Pending=" + pendingCount + ", Confirmed=" + confirmedCount);
            } catch (Exception e) {
                System.err.println("Error updating reservations: " + e.getMessage());
            }
        });
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
}
