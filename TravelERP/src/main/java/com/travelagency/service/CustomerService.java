package com.travelagency.service;

import com.travelagency.dao.CustomerDAO;
import com.travelagency.model.Customer;
import java.util.List;
import java.util.Optional;

/**
 * Customer Management Service
 */
public class CustomerService {
    private final CustomerDAO customerDAO;

    public CustomerService() {
        this.customerDAO = new CustomerDAO();
    }

    /**
     * Create new customer
     */
    public boolean createCustomer(Customer customer) {
        return customerDAO.create(customer);
    }

    /**
     * Get customer by ID
     */
    public Optional<Customer> getCustomerById(int id) {
        Customer customer = customerDAO.readById(id);
        return Optional.ofNullable(customer);
    }

    /**
     * Get all customers
     */
    public List<Customer> getAllCustomers() {
        return customerDAO.readAll();
    }

    /**
     * Update customer
     */
    public boolean updateCustomer(Customer customer) {
        return customerDAO.update(customer);
    }

    /**
     * Delete customer (soft delete)
     */
    public boolean deleteCustomer(int id) {
        return customerDAO.delete(id);
    }

    /**
     * Search customers by name
     */
    public List<Customer> searchCustomers(String searchTerm) {
        return customerDAO.searchByName(searchTerm);
    }

    /**
     * Get customers by country
     */
    public List<Customer> getCustomersByCountry(String country) {
        return customerDAO.getByCountry(country);
    }

    /**
     * Get total number of customers
     */
    public int getTotalCustomerCount() {
        return customerDAO.readAll().size();
    }
}
