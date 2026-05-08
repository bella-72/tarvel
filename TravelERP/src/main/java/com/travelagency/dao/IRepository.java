package com.travelagency.dao;

import java.util.List;

/**
 * Generic Data Access Object interface
 * Provides standard CRUD operations
 */
public interface IRepository<PhantomEntity> {
    
    /**
     * Create a new entity
     */
    boolean create(PhantomEntity entity);
    
    /**
     * Read entity by ID
     */
    PhantomEntity readById(int id);
    
    /**
     * Read all entities
     */
    List<PhantomEntity> readAll();
    
    /**
     * Update existing entity
     */
    boolean update(PhantomEntity entity);
    
    /**
     * Delete entity by ID
     */
    boolean delete(int id);
    
    /**
     * Check if entity exists
     */
    boolean exists(int id);
}
