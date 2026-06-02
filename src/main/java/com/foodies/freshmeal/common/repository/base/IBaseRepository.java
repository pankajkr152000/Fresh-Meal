package com.foodies.freshmeal.common.repository.base;


import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface IBaseRepository<T, ID extends Serializable> extends MongoRepository<T, ID> {
    @Override
    <S extends T> S save(S entity);

    @Override
    Optional<T> findById(ID id);

    @Override
    List<T> findAll();

    @Override
    void deleteById(ID id);

    @Override
    void delete(T entity);

    @Override
    boolean existsById(ID id);

    @Override
    long count();
}
