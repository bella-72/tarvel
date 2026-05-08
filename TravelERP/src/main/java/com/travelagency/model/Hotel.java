package com.travelagency.model;

import java.time.LocalDate;

/**
 * Hotel model representing hotels in travel packages
 */
public class Hotel extends BaseEntity {
    private int packageId;
    private String hotelName;
    private String city;
    private String address;
    private double rating;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private double pricePerNight;
    private int totalRooms;
    private boolean isActive;

    public Hotel() {
        super();
        this.isActive = true;
    }

    public Hotel(int id, int packageId, String hotelName, String city, String address,
                 double rating, LocalDate checkInDate, LocalDate checkOutDate,
                 double pricePerNight, int totalRooms) {
        super(id);
        this.packageId = packageId;
        this.hotelName = hotelName;
        this.city = city;
        this.address = address;
        this.rating = rating;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.pricePerNight = pricePerNight;
        this.totalRooms = totalRooms;
        this.isActive = true;
    }

    public int getPackageId() {
        return packageId;
    }

    public void setPackageId(int packageId) {
        this.packageId = packageId;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public int getTotalRooms() {
        return totalRooms;
    }

    public void setTotalRooms(int totalRooms) {
        this.totalRooms = totalRooms;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return "Hotel{" +
                "id=" + id +
                ", hotelName='" + hotelName + '\'' +
                ", city='" + city + '\'' +
                ", rating=" + rating +
                ", pricePerNight=" + pricePerNight +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hotel hotel = (Hotel) o;
        return id == hotel.id && hotelName.equals(hotel.hotelName);
    }

    @Override
    public int hashCode() {
        return id * 31 + hotelName.hashCode();
    }
}
