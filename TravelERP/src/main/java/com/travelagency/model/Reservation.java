package com.travelagency.model;

import java.time.LocalDate;

/**
 * Reservation model representing customer bookings
 */
public class Reservation extends BaseEntity {
    private int customerId;
    private int packageId;
    private Integer flightId;
    private Integer roomId;
    private LocalDate reservationDate;
    private int numberOfTravelers;
    private double totalPrice;
    private ReservationStatus status;
    private String specialRequirements;

    public enum ReservationStatus {
        PENDING, CONFIRMED, CANCELLED, COMPLETED
    }

    public Reservation() {
        super();
        this.status = ReservationStatus.PENDING;
        this.reservationDate = LocalDate.now();
    }

    public Reservation(int id, int customerId, int packageId, LocalDate reservationDate,
                      int numberOfTravelers, double totalPrice, ReservationStatus status) {
        super(id);
        this.customerId = customerId;
        this.packageId = packageId;
        this.reservationDate = reservationDate;
        this.numberOfTravelers = numberOfTravelers;
        this.totalPrice = totalPrice;
        this.status = status;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getPackageId() {
        return packageId;
    }

    public void setPackageId(int packageId) {
        this.packageId = packageId;
    }

    public Integer getFlightId() {
        return flightId;
    }

    public void setFlightId(Integer flightId) {
        this.flightId = flightId;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public LocalDate getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(LocalDate reservationDate) {
        this.reservationDate = reservationDate;
    }

    public int getNumberOfTravelers() {
        return numberOfTravelers;
    }

    public void setNumberOfTravelers(int numberOfTravelers) {
        this.numberOfTravelers = numberOfTravelers;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public String getSpecialRequirements() {
        return specialRequirements;
    }

    public void setSpecialRequirements(String specialRequirements) {
        this.specialRequirements = specialRequirements;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", packageId=" + packageId +
                ", numberOfTravelers=" + numberOfTravelers +
                ", totalPrice=" + totalPrice +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return id * 31;
    }
}
