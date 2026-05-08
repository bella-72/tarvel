package com.travelagency.dao;

import com.travelagency.database.DatabaseConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Generic Repository implementation with generic parameters
 * Supports basic CRUD operations
 */
public abstract class GenericRepository<PhantomEntity> implements IRepository<PhantomEntity> {
    
    protected DatabaseConnection dbConnection;
    protected Class<PhantomEntity> entityClass;

    public GenericRepository(Class<PhantomEntity> entityClass) {
        this.dbConnection = DatabaseConnection.getInstance();
        this.entityClass = entityClass;
    }

    @Override
    public abstract boolean create(PhantomEntity entity);

    @Override
    public abstract PhantomEntity readById(int id);

    @Override
    public abstract List<PhantomEntity> readAll();

    @Override
    public abstract boolean update(PhantomEntity entity);

    @Override
    public abstract boolean delete(int id);

    @Override
    public abstract boolean exists(int id);

    /**
     * Execute a SELECT query and map results
     */
    protected <T> List<T> executeQuery(String sql, ResultMapper<T> mapper) {
        List<T> results = new ArrayList<>();
        try {
            return dbConnection.executeQuery(sql, stmt -> {
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    results.add(mapper.map(rs));
                }
                return results;
            });
        } catch (SQLException e) {
            System.err.println("Error executing query: " + e.getMessage());
            return results;
        }
    }

    /**
     * Execute a SELECT query for a single result
     */
    protected <T> Optional<T> executeQuerySingle(String sql, ResultMapper<T> mapper) {
        try {
            List<T> results = executeQuery(sql, mapper);
            return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
        } catch (Exception e) {
            System.err.println("Error executing single query: " + e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Execute an UPDATE or DELETE query
     */
    protected boolean executeUpdate(String sql, UpdateSetter setter) {
        try {
            dbConnection.executeUpdate(sql, setter::set);
            return true;
        } catch (SQLException e) {
            System.err.println("Error executing update: " + e.getMessage());
            return false;
        }
    }

    /**
     * Functional interface for mapping ResultSet rows
     */
    @FunctionalInterface
    protected interface ResultMapper<T> {
        T map(ResultSet rs) throws SQLException;
    }

    /**
     * Functional interface for setting prepared statement parameters
     */
    @FunctionalInterface
    protected interface UpdateSetter {
        void set(PreparedStatement stmt) throws SQLException;
    }
}
