package com.travelagency.service;

import com.travelagency.dao.UserDAO;
import com.travelagency.model.User;
import java.util.Optional;

/**
 * Authentication Service
 */
public class AuthenticationService {
    private final UserDAO userDAO;

    public AuthenticationService() {
        this.userDAO = new UserDAO();
    }

    /**
     * Authenticate user with username and password
     */
    public Optional<User> authenticate(String username, String password) {
        Optional<User> user = userDAO.findByUsername(username);
        if (user.isPresent() && user.get().getPassword().equals(password) && user.get().isActive()) {
            return user;
        }
        return Optional.empty();
    }

    /**
     * Register new user
     */
    public boolean registerUser(User user) {
        // Check if user already exists
        if (userDAO.findByUsername(user.getUsername()).isPresent()) {
            System.err.println("Username already exists");
            return false;
        }
        if (userDAO.findByEmail(user.getEmail()).isPresent()) {
            System.err.println("Email already exists");
            return false;
        }
        return userDAO.create(user);
    }

    /**
     * Validate user credentials
     */
    public boolean validateCredentials(String username, String password) {
        return authenticate(username, password).isPresent();
    }

    /**
     * Get user by ID
     */
    public Optional<User> getUserById(int userId) {
        User user = userDAO.readById(userId);
        return Optional.ofNullable(user);
    }

    /**
     * Update user information
     */
    public boolean updateUser(User user) {
        return userDAO.update(user);
    }
}
