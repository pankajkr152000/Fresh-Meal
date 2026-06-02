package com.foodies.freshmeal.common.audit.service.impl;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.foodies.freshmeal.common.audit.entity.IAuditLog;
import com.foodies.freshmeal.common.audit.entity.impl.AuditLog;
import com.foodies.freshmeal.common.audit.repository.IAuditRepository;
import com.foodies.freshmeal.common.audit.service.IAuditService;



@Service
public class AuditServiceImpl implements IAuditService {

    private static final Set<String> SENSITIVE_FIELDS = Set.of(
            "password",
            "passwd",
            "pwd",
            "otp",
            "token",
            "accessToken",
            "refreshToken",
            "jwt",
            "cvv",
            "cardNumber");

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditServiceImpl.class);
    private final IAuditRepository repository;
    private final ObjectMapper objectMapper;

    public AuditServiceImpl(
            IAuditRepository repository,
            ObjectMapper objectMapper) {

        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    @Override
    public void saveAuditLog(IAuditLog auditLog) {

        try {

            repository.save((AuditLog) auditLog);

        } catch (Exception e) {

            LOGGER.error("Failed to save audit log : {}", e.getMessage());
        }
    }

    @Async
    @Override
    public void saveAuditLogAsync(IAuditLog auditLog) {

        saveAuditLog(auditLog);
    }

    @Override
    public void logSuccess(
            String api,
            String method,
            Object request,
            Object response,
            Integer statusCode,
            Long executionTimeMs) {

        try {

            AuditLog auditLog = new AuditLog();

            auditLog.setApi(api);
            auditLog.setMethod(method);

            auditLog.setRequestBody(
                    convertToDocument(request));

            auditLog.setResponseBody(
                    convertToDocument(response));

            auditLog.setResponseStatus(statusCode);
            auditLog.setExecutionTimeMs(executionTimeMs);

            saveAuditLogAsync(auditLog);

        } catch (Exception e) {

            LOGGER.error("Audit logging failed : {}", e.getMessage());
        }
    }

    @Override
    public void logFailure(
            String api,
            String method,
            Object request,
            Exception exception,
            Integer statusCode,
            Long executionTimeMs) {

        try {

            AuditLog auditLog = new AuditLog();

            auditLog.setApi(api);
            auditLog.setMethod(method);

            auditLog.setRequestBody(
                    convertToDocument(request));

            auditLog.setResponseStatus(statusCode);

            auditLog.setExecutionTimeMs(
                    executionTimeMs);

            auditLog.setExceptionMessage(
                    exception.getMessage());

            saveAuditLogAsync(auditLog);

        } catch (Exception e) {

            LOGGER.error("Audit logging failed : {}", e.getMessage());
        }
    }

    @Override
    public IAuditLog getAuditLogById(String id) {

        return repository.findById(id)
                .orElse(null);
    }

    @Override
    public List<IAuditLog> getAuditLogsByRequestId(
            String requestId) {

        return Collections.unmodifiableList(
                repository.findByRequestId(requestId)
                        .stream()
                        .map(log -> (IAuditLog) log)
                        .toList());
    }

    private Document convertToDocument(Object object) {

        if (object == null) {
            return null;
        }

        try {

            JsonNode jsonNode = objectMapper.valueToTree(object);

            JsonNode maskedNode = maskSensitiveData(jsonNode);

            return Document.parse(objectMapper.writeValueAsString(maskedNode));

        } catch (JacksonException e) {

            return new Document(
                    "error",
                    "Unable to serialize object");
        }
    }

    private JsonNode maskSensitiveData(JsonNode node) {

        if (node == null) {
            return null;
        }

        if (node.isObject()) {

            ObjectNode objectNode = (ObjectNode) node;

            Iterator<String> fieldNames = objectNode.fieldNames();

            while (fieldNames.hasNext()) {

                String fieldName = fieldNames.next();

                JsonNode childNode = objectNode.get(fieldName);
                if ("cardNumber".equalsIgnoreCase(fieldName)) {
                    objectNode.put(fieldName, maskCard(childNode.asText()));
}
                else if (SENSITIVE_FIELDS.contains(fieldName)) {

                    objectNode.put(fieldName, "******");

                } else {

                    maskSensitiveData(childNode);
                }
            }
        }

        if (node.isArray()) {

            ArrayNode arrayNode = (ArrayNode) node;

            for (JsonNode child : arrayNode) {

                maskSensitiveData(child);
            }
        }

        return node;
    }

    private String maskCard(String cardNumber) {

        if (cardNumber == null || cardNumber.length() < 4) {
            return "****";
        }

        return "*".repeat(cardNumber.length() - 4)
                + cardNumber.substring(cardNumber.length() - 4);
    }
}