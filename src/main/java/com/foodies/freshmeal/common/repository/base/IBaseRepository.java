package com.foodies.freshmeal.common.repository.base;


import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface IBaseRepository<T, ID extends Serializable> {

    T save(T entity);

    List<T> saveAll(List<T> entities);

    Optional<T> findById(ID id);

    List<T> findAll();

    void deleteById(ID id);

    void delete(T entity);

    boolean existsById(ID id);

    long count();
}
