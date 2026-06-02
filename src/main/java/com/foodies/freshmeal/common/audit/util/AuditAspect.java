package com.foodies.freshmeal.common.audit.util;

import java.time.LocalDateTime;
import java.util.UUID;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodies.freshmeal.common.audit.entity.impl.AuditLog;
import com.foodies.freshmeal.common.audit.service.IAuditService;
import com.foodies.freshmeal.common.constants.HttpStatusCode;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * =====================================================
 * Audit Aspect
 * =====================================================
 *
 * Intercepts all controller methods annotated with
 * {@code @AuditApi}.
 *
 * Captures:
 * - API URL
 * - HTTP Method
 * - Request ID
 * - IP Address
 * - Query Parameters
 * - Request Payload
 * - Response Payload
 * - Execution Time
 * - Exception Details
 *
 * Stores audit records asynchronously in MongoDB.
 *
 * =====================================================
 */
@Aspect
@Component
public class AuditAspect {

        private static final Logger LOGGER = LoggerFactory.getLogger(AuditAspect.class);

        private final IAuditService auditService;
        private final ObjectMapper objectMapper;
        private final HttpServletRequest httpRequest;
        private final HttpServletResponse httpResponse;

        public AuditAspect(
                        IAuditService auditService,
                        ObjectMapper objectMapper,
                        HttpServletRequest httpRequest, HttpServletResponse httpResponse) {

                this.auditService = auditService;
                this.objectMapper = objectMapper;
                this.httpRequest = httpRequest;
                this.httpResponse = httpResponse;
        }

        /**
         * =====================================================
         * Audit API Calls
         * =====================================================
         *
         * Intercepts methods annotated with @AuditApi.
         *
         * =====================================================
         */
        @Around("@annotation(com.foodies.freshmeal.common.audit.annotation.AuditApi)")
        public Object audit(ProceedingJoinPoint joinPoint) throws Throwable {

                long startTime = System.currentTimeMillis();

                String requestId = UUID.randomUUID().toString();

                LOGGER.info("AUDIT STARTED | RequestId={} | API={} | Method={}", requestId, httpRequest.getRequestURI(), httpRequest.getMethod());

                AuditLog auditLog = new AuditLog();
                auditLog.setRequestId(requestId);
                auditLog.setApi(httpRequest.getRequestURI());
                auditLog.setMethod(httpRequest.getMethod());
                auditLog.setIpAddress(httpRequest.getRemoteAddr());
                auditLog.setQueryParams(httpRequest.getQueryString());
                auditLog.setCreatedAt(LocalDateTime.now());

                try {

                        auditLog.setRequestBody(buildRequestDocument(joinPoint.getArgs()));

                        Object response = joinPoint.proceed();

                        auditLog.setResponseBody(convertToDocument(response));
                        auditLog.setResponseStatus(httpResponse.getStatus());
                        auditLog.setResponseMessage(HttpStatusCode.getDescription(httpResponse.getStatus()));
                        auditLog.setExecutionTimeMs(System.currentTimeMillis() - startTime);

                        try {
                                auditService.saveAuditLogAsync(auditLog);
                        } catch (Exception e) {
                                LOGGER.error("Failed to save audit log. RequestId={}", requestId, e);
                        }

                        LOGGER.info("AUDIT SUCCESS | RequestId={} | Duration={} ms", requestId, auditLog.getExecutionTimeMs());

                        return response;

                } catch (Throwable ex) {

                        auditLog.setResponseStatus(httpResponse.getStatus());
                        auditLog.setResponseMessage(HttpStatusCode.getDescription(httpResponse.getStatus()));
                        auditLog.setExceptionMessage(ex.getMessage());
                        auditLog.setExecutionTimeMs(System.currentTimeMillis() - startTime);
                        try {
                                auditService.saveAuditLogAsync(auditLog);
                        } catch (Exception e) {
                                LOGGER.error("Failed to save audit log. RequestId={}", requestId, e);
                        }

                        LOGGER.error("AUDIT FAILURE | RequestId={} | Duration={} ms | Error={}", requestId, auditLog.getExecutionTimeMs(), ex.getMessage(), ex);

                        throw ex;
                }
        }

        /**
         * =====================================================
         * Build Request Document
         * =====================================================
         *
         * Converts controller arguments into Mongo
         * Document format.
         *
         * Multipart files are converted into metadata
         * instead of storing binary content.
         *
         * =====================================================
         */
        private Document buildRequestDocument(Object[] args) {
                Document requestDoc = new Document();

                for (Object arg : args) {
                        if (arg == null) {
                                continue;
                        }
                        try {
                                if (arg instanceof MultipartFile file) {
                                        requestDoc.append("file", new Document()
                                                .append("fileName", file.getOriginalFilename())
                                                .append("contentType", file.getContentType())
                                                .append("fileSize", file.getSize()));

                                } else if (arg instanceof String str) {
                                        try {
                                                requestDoc.append("foodRequest", Document.parse(str));

                                        } catch (Exception ex) {
                                                requestDoc.append("foodRequest", str);
                                        }
                                } else {

                                        requestDoc.append(arg.getClass().getSimpleName(),Document.parse(objectMapper.writeValueAsString(arg)));
                                }

                        } catch (JsonProcessingException e) {
                                LOGGER.error("Failed to serialize request argument : {}", e.getMessage(), e);
                                requestDoc.append( "serializationError", e.getMessage());
                        }
                }
                return requestDoc;
        }

        /**
         * =====================================================
         * Convert Object To Mongo Document
         * =====================================================
         *
         * Converts any Java object into BSON Document
         * suitable for MongoDB storage.
         *
         * =====================================================
         */
        private Document convertToDocument(Object object) {

                if (object == null) {
                        return null;
                }

                try {
                        return Document.parse(objectMapper.writeValueAsString(object));

                } catch (JsonProcessingException e) {
                        LOGGER.error("Failed to convert object to document : {}",e.getMessage(), e);
                        return new Document().append("error", e.getMessage());
                }
        }
}