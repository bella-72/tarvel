package com.travelagency.model;

import java.time.LocalDate;

/**
 * Travel Package model representing tour packages
 */
public class TravelPackage extends BaseEntity {
    private String packageName;
    private String destination;
    private String description;
    private int durationDays;
    private double pricePerPerson;
    private int maxCapacity;
    private int availableSeats;
    private LocalDate startDate;
    private LocalDate endDate;
    private PackageType packageType;
    private boolean isActive;

    public enum PackageType {
        BEACH, MOUNTAIN, CITY, ADVENTURE, CULTURAL
    }

    public TravelPackage() {
        super();
        this.isActive = true;
    }

    public TravelPackage(int id, String packageName, String destination, int durationDays,
                        double pricePerPerson, int maxCapacity, int availableSeats,
                        LocalDate startDate, LocalDate endDate, PackageType packageType) {
        super(id);
        this.packageName = packageName;
        this.destination = destination;
        this.durationDays = durationDays;
        this.pricePerPerson = pricePerPerson;
        this.maxCapacity = maxCapacity;
        this.availableSeats = availableSeats;
        this.startDate = startDate;
        this.endDate = endDate;
        this.packageType = packageType;
        this.isActive = true;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDurationDays() {
        return durationDays;
    }

    public void setDurationDays(int durationDays) {
        this.durationDays = durationDays;
    }

    public double getPricePerPerson() {
        return pricePerPerson;
    }

    public void setPricePerPerson(double pricePerPerson) {
        this.pricePerPerson = pricePerPerson;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public PackageType getPackageType() {
        return packageType;
    }

    public void setPackageType(PackageType packageType) {
        this.packageType = packageType;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return "TravelPackage{" +
                "id=" + id +
                ", packageName='" + packageName + '\'' +
                ", destination='" + destination + '\'' +
                ", durationDays=" + durationDays +
                ", pricePerPerson=" + pricePerPerson +
                ", availableSeats=" + availableSeats +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TravelPackage that = (TravelPackage) o;
        return id == that.id && packageName.equals(that.packageName);
    }

    @Override
    public int hashCode() {
        return id * 31 + packageName.hashCode();
    }
}
