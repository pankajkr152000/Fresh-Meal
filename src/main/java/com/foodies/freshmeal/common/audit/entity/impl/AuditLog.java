package com.foodies.freshmeal.common.audit.entity.impl;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.foodies.freshmeal.common.audit.entity.IAuditLog;
import com.foodies.freshmeal.common.constants.ActionType;
import com.foodies.freshmeal.common.constants.MethodType;
import com.foodies.freshmeal.common.constants.ModuleType;
import com.foodies.freshmeal.common.constants.RoleType;
import com.foodies.freshmeal.common.date.AppCalendar;
import com.foodies.freshmeal.common.date.DateConstants;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document(collection = "fm_api_audit_logs")
public class AuditLog implements IAuditLog {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    private String api;
    private String requestUrl;

    private org.bson.Document requestBody; // Input JSON
    private org.bson.Document responseBody; // Output JSON

    private Integer responseStatus;
    private String responseMessage;

    private Long executionTimeMs;
    @Indexed
    private String requestId;

    private String queryParams;
    @Indexed
    private String userId;

    private String exceptionMessage;

    private String ipAddress;

    @Indexed
    @JsonFormat(pattern = DateConstants.DEFAULT_DATE_TIME_FORMAT)
    private LocalDateTime createdAt;

    private RoleType role;

    private ModuleType module;

    private MethodType method;

    private ActionType action;

    private String requestMethod;

    // Default constructor
    public AuditLog() {

    }

    // Constructor with parameters
    public AuditLog(String api,
            MethodType method,
            org.bson.Document requestBody,
            org.bson.Document responseBody,
            Integer responseStatus,
            Long executionTimeMs,
            String ipAddress) {

        this.api = api;
        this.method = method;
        this.requestBody = requestBody;
        this.responseBody = responseBody;
        this.responseStatus = responseStatus;
        this.executionTimeMs = executionTimeMs;
        this.ipAddress = ipAddress;
        this.createdAt = AppCalendar.getBusinessLocalDateTime();
    }

    // Getters and Setters
    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getApi() {
        return api;
    }

    @Override
    public void setApi(String api) {
        this.api = api;
    }

    @Override
    public org.bson.Document getRequestBody() {
        return requestBody;
    }

    @Override
    public void setRequestBody(org.bson.Document requestBody) {
        this.requestBody = requestBody;
    }

    @Override
    public org.bson.Document getResponseBody() {
        return responseBody;
    }

    @Override
    public void setResponseBody(org.bson.Document responseBody) {
        this.responseBody = responseBody;
    }

    @Override
    public Integer getResponseStatus() {
        return responseStatus;
    }

    @Override
    public void setResponseStatus(Integer responseStatus) {
        this.responseStatus = responseStatus;
    }

    @Override
    public String getResponseMessage() {
        return responseMessage;
    }

    @Override
    public void setResponseMessage(String message) {
        this.responseMessage = message;
    }

    @Override
    public Long getExecutionTimeMs() {
        return executionTimeMs;
    }

    @Override
    public void setExecutionTimeMs(Long executionTimeMs) {
        this.executionTimeMs = executionTimeMs;
    }

    @Override
    public String getIpAddress() {
        return ipAddress;
    }

    @Override
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    @Override
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "AuditLog{" +
                "id='" + id + '\'' +
                ", api='" + api + '\'' +
                ", method='" + method + '\'' +
                ", requestBody='" + requestBody + '\'' +
                ", responseBody='" + responseBody + '\'' +
                ", responseStatus=" + responseStatus +
                ", executionTimeMs=" + executionTimeMs +
                ", ipAddress='" + ipAddress + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getQueryParams() {
        return queryParams;
    }

    public void setQueryParams(String queryParams) {
        this.queryParams = queryParams;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }

    public void setExceptionMessage(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }

    /**
     * Returns the business method executed.
     */
    @Override
    public MethodType getMethod() {
        return method;
    }

    /**
     * Sets the business method executed.
     */
    @Override
    public void setMethod(MethodType method) {
        this.method = method;
    }

}