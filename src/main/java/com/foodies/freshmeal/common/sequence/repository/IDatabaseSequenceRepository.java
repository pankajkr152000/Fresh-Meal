package com.foodies.freshmeal.common.sequence.repository;

import org.springframework.stereotype.Repository;

import com.foodies.freshmeal.common.entity.ABaseEntity;
import com.foodies.freshmeal.common.repository.base.IBaseRepository;

@Repository
public interface IDatabaseSequenceRepository extends IBaseRepository<ABaseEntity, Object> {

}
