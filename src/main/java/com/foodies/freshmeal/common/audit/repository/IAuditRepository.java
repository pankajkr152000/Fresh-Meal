package com.foodies.freshmeal.common.audit.repository;



import java.util.List;

import org.springframework.stereotype.Repository;

import com.foodies.freshmeal.common.audit.entity.impl.AuditLog;
import com.foodies.freshmeal.common.repository.base.IBaseRepository;

@Repository
public interface IAuditRepository extends IBaseRepository<AuditLog, String>{

    List<AuditLog> findByRequestId(String requestId);

}
