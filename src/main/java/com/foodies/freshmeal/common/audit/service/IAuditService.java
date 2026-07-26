package com.foodies.freshmeal.common.audit.service;

import java.util.List;

import com.foodies.freshmeal.common.audit.entity.IAuditLog;
import com.foodies.freshmeal.common.constants.MethodType;

public interface IAuditService {

    /**
     * Save audit log.
     */
    void saveAuditLog(IAuditLog auditLog);

    /**
     * Save audit log asynchronously.
     */
    void saveAuditLogAsync(IAuditLog auditLog);

    /**
     * Create and save audit log for successful API execution.
     */
    void logSuccess(
            String api,
            MethodType method,
            Object request,
            Object response,
            Integer statusCode,
            Long executionTimeMs);

    /**
     * Create and save audit log for failed API execution.
     */
    void logFailure(
            String api,
            MethodType method,
            Object request,
            Exception exception,
            Integer statusCode,
            Long executionTimeMs);

    /**
     * Fetch audit log by id.
     */
    IAuditLog getAuditLogById(String id);

    /**
     * Find logs by request id.
     */
    List<IAuditLog> getAuditLogsByRequestId(String requestId);

}