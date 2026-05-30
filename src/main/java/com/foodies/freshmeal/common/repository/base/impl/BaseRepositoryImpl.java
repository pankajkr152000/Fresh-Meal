package com.foodies.freshmeal.common.repository.base.impl;



import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * =====================================================
 * Base Repository
 * =====================================================
 *
 * Common reusable repository methods.
 *
 * Adds:
 * - logging
 * - reusable CRUD
 * - centralized DB access
 *
 * =====================================================
 */
public abstract class BaseRepositoryImpl<T,ID extends Serializable> {

    protected final Logger LOGGER = LoggerFactory.getLogger(getClass());

    protected final MongoRepository<T, ID> repository;

    protected BaseRepositoryImpl (
            MongoRepository<T, ID> repository) {

        this.repository = repository;
    }

    public T save(T entity) {

        LOGGER.info(
                "Saving entity : {}",
                entity.getClass().getSimpleName());

        return repository.save(entity);
    }

    public List<T> saveAll(List<T> entities) {

        LOGGER.info(
                "Saving entity list. Size : {}",
                entities.size());

        return repository.saveAll(entities);
    }

    public Optional<T> findById(ID id) {

        LOGGER.info(
                "Finding entity by id : {}",
                id);

        return repository.findById(id);
    }

    public List<T> findAll() {

        LOGGER.info("Finding all entities");

        return repository.findAll();
    }

    public void deleteById(ID id) {

        LOGGER.info(
                "Deleting entity by id : {}",
                id);

        repository.deleteById(id);
    }

    public void delete(T entity) {

        LOGGER.info(
                "Deleting entity : {}",
                entity.getClass().getSimpleName());

        repository.delete(entity);
    }

    public boolean existsById(ID id) {

        LOGGER.info(
                "Checking entity existence : {}",
                id);

        return repository.existsById(id);
    }

    public long count() {

        LOGGER.info("Counting entities");

        return repository.count();
    }
}
