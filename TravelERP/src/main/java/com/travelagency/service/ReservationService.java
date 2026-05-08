package com.travelagency.service;

import com.travelagency.dao.ReservationDAO;
import com.travelagency.dao.PaymentDAO;
import com.travelagency.model.Reservation;
import com.travelagency.model.Payment;
import java.util.List;
import java.util.Optional;

/**
 * Reservation Management Service
 */
public class ReservationService {
    private final ReservationDAO reservationDAO;
    private final PaymentDAO paymentDAO;
    private final TravelPackageService packageService;

    public ReservationService() {
        this.reservationDAO = new ReservationDAO();
        this.paymentDAO = new PaymentDAO();
        this.packageService = new TravelPackageService();
    }

    /**
     * Create new reservation
     */
    public boolean createReservation(Reservation reservation) {
        // Book seats in package
        if (packageService.bookSeats(reservation.getPackageId(), reservation.getNumberOfTravelers())) {
            return reservationDAO.create(reservation);
        }
        return false;
    }

    /**
     * Get reservation by ID
     */
    public Optional<Reservation> getReservationById(int id) {
        Reservation reservation = reservationDAO.readById(id);
        return Optional.ofNullable(reservation);
    }

    /**
     * Get all reservations
     */
    public List<Reservation> getAllReservations() {
        return reservationDAO.readAll();
    }

    /**
     * Get reservations by customer ID
     */
    public List<Reservation> getReservationsByCustomer(int customerId) {
        return reservationDAO.getByCustomerId(customerId);
    }

    /**
     * Get confirmed reservations
     */
    public List<Reservation> getConfirmedReservations() {
        return reservationDAO.getConfirmedReservations();
    }

    /**
     * Get pending reservations
     */
    public List<Reservation> getPendingReservations() {
        return reservationDAO.getPendingReservations();
    }

    /**
     * Update reservation
     */
    public boolean updateReservation(Reservation reservation) {
        return reservationDAO.update(reservation);
    }

    /**
     * Cancel reservation
     */
    public boolean cancelReservation(int reservationId) {
        Optional<Reservation> reservation = getReservationById(reservationId);
        if (reservation.isPresent()) {
            Reservation res = reservation.get();
            res.setStatus(Reservation.ReservationStatus.CANCELLED);
            
            // Release seats
            packageService.releaseSeats(res.getPackageId(), res.getNumberOfTravelers());
            
            return reservationDAO.update(res);
        }
        return false;
    }

    /**
     * Confirm reservation
     */
    public boolean confirmReservation(int reservationId) {
        Optional<Reservation> reservation = getReservationById(reservationId);
        if (reservation.isPresent()) {
            Reservation res = reservation.get();
            res.setStatus(Reservation.ReservationStatus.CONFIRMED);
            return reservationDAO.update(res);
        }
        return false;
    }

    /**
     * Get reservation count
     */
    public int getTotalReservationCount() {
        return reservationDAO.readAll().size();
    }
}
