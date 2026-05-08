package com.travelagency.service;

import com.travelagency.dao.TravelPackageDAO;
import com.travelagency.model.TravelPackage;
import java.util.List;
import java.util.Optional;

/**
 * Travel Package Management Service
 */
public class TravelPackageService {
    private final TravelPackageDAO packageDAO;

    public TravelPackageService() {
        this.packageDAO = new TravelPackageDAO();
    }

    /**
     * Create new travel package
     */
    public boolean createPackage(TravelPackage pkg) {
        return packageDAO.create(pkg);
    }

    /**
     * Get package by ID
     */
    public Optional<TravelPackage> getPackageById(int id) {
        TravelPackage pkg = packageDAO.readById(id);
        return Optional.ofNullable(pkg);
    }

    /**
     * Get all available packages
     */
    public List<TravelPackage> getAllPackages() {
        return packageDAO.readAll();
    }

    /**
     * Get packages with available seats
     */
    public List<TravelPackage> getAvailablePackages() {
        return packageDAO.getAvailablePackages();
    }

    /**
     * Get packages by destination
     */
    public List<TravelPackage> getPackagesByDestination(String destination) {
        return packageDAO.getByDestination(destination);
    }

    /**
     * Get packages by type
     */
    public List<TravelPackage> getPackagesByType(String packageType) {
        return packageDAO.getByType(packageType);
    }

    /**
     * Update package
     */
    public boolean updatePackage(TravelPackage pkg) {
        return packageDAO.update(pkg);
    }

    /**
     * Delete package (soft delete)
     */
    public boolean deletePackage(int id) {
        return packageDAO.delete(id);
    }

    /**
     * Book seats in package
     */
    public boolean bookSeats(int packageId, int numberOfSeats) {
        Optional<TravelPackage> pkg = getPackageById(packageId);
        if (pkg.isPresent() && pkg.get().getAvailableSeats() >= numberOfSeats) {
            TravelPackage travelPackage = pkg.get();
            travelPackage.setAvailableSeats(travelPackage.getAvailableSeats() - numberOfSeats);
            return updatePackage(travelPackage);
        }
        return false;
    }

    /**
     * Release booked seats
     */
    public boolean releaseSeats(int packageId, int numberOfSeats) {
        Optional<TravelPackage> pkg = getPackageById(packageId);
        if (pkg.isPresent()) {
            TravelPackage travelPackage = pkg.get();
            int newSeats = Math.min(travelPackage.getAvailableSeats() + numberOfSeats, travelPackage.getMaxCapacity());
            travelPackage.setAvailableSeats(newSeats);
            return updatePackage(travelPackage);
        }
        return false;
    }

    /**
     * Get most booked destinations
     */
    public List<TravelPackage> getMostBookedDestinations() {
        return packageDAO.readAll();
    }
}
