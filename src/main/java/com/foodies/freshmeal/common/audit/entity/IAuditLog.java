package com.foodies.freshmeal.common.audit.entity;

import java.time.LocalDateTime;

import org.bson.Document;

import com.foodies.freshmeal.common.constants.ActionType;
import com.foodies.freshmeal.common.constants.MethodType;
import com.foodies.freshmeal.common.constants.ModuleType;
import com.foodies.freshmeal.common.constants.RoleType;
import com.foodies.freshmeal.common.entity.IEntity;

/**
 * Contract for audit log entities.
 *
 * <p>
 * Stores request, response, execution metadata and business audit
 * information for every API invocation.
 * </p>
 *
 * @author Pankaj Kumar
 */
public interface IAuditLog extends IEntity {

    // =====================================================
    // BASIC DETAILS
    // =====================================================

    String getId();

    void setId(String id);

    String getApi();

    void setApi(String api);

    /**
     * Returns the business method executed.
     * Example:
     * <ul>
     * <li>ADD_FOOD</li>
     * <li>UPDATE_FOOD</li>
     * <li>VIEW_FOOD</li>
     * </ul>
     */
    MethodType getMethod();

    void setMethod(MethodType method);

    // =====================================================
    // REQUEST / RESPONSE
    // =====================================================

    Document getRequestBody();

    void setRequestBody(Document requestBody);

    Document getResponseBody();

    void setResponseBody(Document responseBody);

    Integer getResponseStatus();

    void setResponseStatus(Integer responseStatus);

    String getResponseMessage();

    void setResponseMessage(String message);

    // =====================================================
    // EXECUTION DETAILS
    // =====================================================

    Long getExecutionTimeMs();

    void setExecutionTimeMs(Long executionTimeMs);

    String getIpAddress();

    void setIpAddress(String ipAddress);

    LocalDateTime getCreatedAt();

    void setCreatedAt(LocalDateTime createdAt);

    // =====================================================
    // BUSINESS AUDIT
    // =====================================================

    RoleType getRole();

    void setRole(RoleType role);

    ModuleType getModule();

    void setModule(ModuleType module);

    ActionType getAction();

    void setAction(ActionType action);
}