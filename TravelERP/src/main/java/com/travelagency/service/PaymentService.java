package com.travelagency.service;

import com.travelagency.dao.PaymentDAO;
import com.travelagency.model.Payment;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Payment Management Service
 */
public class PaymentService {
    private final PaymentDAO paymentDAO;

    public PaymentService() {
        this.paymentDAO = new PaymentDAO();
    }

    /**
     * Process payment
     */
    public boolean processPayment(Payment payment) {
        if (payment == null
                || payment.getReservationId() <= 0
                || payment.getAmount() <= 0
                || payment.getPaymentMethod() == null) {
            return false;
        }

        if (payment.getTransactionId() == null || payment.getTransactionId().isBlank()) {
            payment.setTransactionId(generateTransactionId());
        }
        payment.setTransactionDate(LocalDateTime.now());
        if (payment.getPaymentStatus() == null) {
            payment.setPaymentStatus(Payment.PaymentStatus.COMPLETED);
        }
        return paymentDAO.create(payment);
    }

    /**
     * Generate a unique transaction ID
     */
    public String generateTransactionId() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase();
    }

    /**
     * Get payment by ID
     */
    public Optional<Payment> getPaymentById(int id) {
        Payment payment = paymentDAO.readById(id);
        return Optional.ofNullable(payment);
    }

    /**
     * Get all payments
     */
    public List<Payment> getAllPayments() {
        return paymentDAO.readAll();
    }

    /**
     * Get payments by reservation ID
     */
    public List<Payment> getPaymentsByReservation(int reservationId) {
        return paymentDAO.getByReservationId(reservationId);
    }

    /**
     * Get completed payments
     */
    public List<Payment> getCompletedPayments() {
        return paymentDAO.getCompletedPayments();
    }

    /**
     * Update payment status
     */
    public boolean updatePaymentStatus(int paymentId, Payment.PaymentStatus status) {
        Optional<Payment> payment = getPaymentById(paymentId);
        if (payment.isPresent()) {
            Payment p = payment.get();
            p.setPaymentStatus(status);
            return paymentDAO.update(p);
        }
        return false;
    }

    /**
     * Get total revenue
     */
    public double getTotalRevenue() {
        return paymentDAO.getTotalRevenue();
    }

    /**
     * Get total pending amount
     */
    public double getTotalPendingAmount() {
        return getAllPayments().stream()
                .filter(p -> p.getPaymentStatus() == Payment.PaymentStatus.PENDING)
                .mapToDouble(Payment::getAmount)
                .sum();
    }

    /**
     * Refund payment
     */
    public boolean refundPayment(int paymentId) {
        Optional<Payment> payment = getPaymentById(paymentId);
        if (payment.isPresent()) {
            Payment p = payment.get();
            p.setPaymentStatus(Payment.PaymentStatus.REFUNDED);
            return paymentDAO.update(p);
        }
        return false;
    }
}
