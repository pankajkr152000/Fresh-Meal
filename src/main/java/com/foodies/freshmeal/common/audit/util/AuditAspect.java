package com.foodies.freshmeal.common.audit.util;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodies.freshmeal.common.audit.annotation.AuditApi;
import com.foodies.freshmeal.common.audit.entity.impl.AuditLog;
import com.foodies.freshmeal.common.audit.service.IAuditService;
import com.foodies.freshmeal.common.constants.HttpStatusCode;
import com.foodies.freshmeal.common.date.DataFormatUtil;
import com.foodies.freshmeal.common.io.service.IServiceContext;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * =========================================================== Audit Aspect
 * ===========================================================
 *
 * <p>
 * Intercepts every controller method annotated with {@link AuditApi} and
 * records complete audit information.
 * </p>
 *
 * <p>
 * Responsibilities:
 * </p>
 *
 * <ul>
 * <li>Capture incoming request</li>
 * <li>Mask sensitive information</li>
 * <li>Capture outgoing response</li>
 * <li>Measure execution time</li>
 * <li>Capture exception details</li>
 * <li>Persist audit log asynchronously</li>
 * </ul>
 *
 * <p>
 * This class acts as the heart of the auditing framework.
 * </p>
 *
 * @author Pankaj Kumar
 */
@Aspect
@Component
public class AuditAspect {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuditAspect.class);

	/**
	 * Audit persistence service.
	 */
	private final IAuditService auditService;

	/**
	 * Builds AuditLog objects.
	 */
	private final AuditLogBuilder auditLogBuilder;

	/**
	 * Object mapper used for request/response serialization.
	 */
	private final ObjectMapper objectMapper;

	/**
	 * Current HTTP request.
	 */
	private final HttpServletRequest httpRequest;

	/**
	 * Current HTTP response.
	 */
	private final HttpServletResponse httpResponse;

	/**
	 * Current request service context.
	 */
	private final IServiceContext serviceContext;

	/**
	 * Creates AuditAspect.
	 *
	 * @param auditService    Audit persistence service
	 * @param auditLogBuilder Audit log builder
	 * @param objectMapper    Jackson mapper
	 * @param httpRequest     Current request
	 * @param httpResponse    Current response
	 * @param serviceContext  Current request context
	 */
	public AuditAspect(IAuditService auditService, AuditLogBuilder auditLogBuilder, ObjectMapper objectMapper,
			HttpServletRequest httpRequest, HttpServletResponse httpResponse, IServiceContext serviceContext) {

		this.auditService = auditService;
		this.auditLogBuilder = auditLogBuilder;
		this.objectMapper = objectMapper;
		this.httpRequest = httpRequest;
		this.httpResponse = httpResponse;
		this.serviceContext = serviceContext;
	}

	/**
	 * =========================================================== Audit API
	 * Execution ===========================================================
	 *
	 * <p>
	 * Intercepts every API annotated with {@link AuditApi}. Captures request,
	 * response, execution time and exceptions.
	 * </p>
	 *
	 * @param joinPoint intercepted controller method
	 *
	 * @return controller response
	 *
	 * @throws Throwable propagated exception
	 */
	@Around("@annotation(com.foodies.freshmeal.common.audit.annotation.AuditApi)")
	public Object audit(ProceedingJoinPoint joinPoint) throws Throwable {

		long startTime = System.currentTimeMillis();

		MethodSignature signature = (MethodSignature) joinPoint.getSignature();

		AuditApi auditApi = signature.getMethod().getAnnotation(AuditApi.class);

		AuditLog auditLog = auditLogBuilder.build(httpRequest, serviceContext);

		auditLog.setModule(auditApi.module());

		auditLog.setAction(auditApi.action());

		auditLog.setMethod(auditApi.method());

		LOGGER.info("AUDIT STARTED | RequestId={} | API={} | HTTP Method={}", auditLog.getRequestId(),
				auditLog.getApi(), auditLog.getRequestMethod());

		try {

			if (auditApi.logRequest()) {

				auditLog.setRequestBody(buildRequestDocument(joinPoint.getArgs()));
			}

			logRequest(auditLog);

			Object response = joinPoint.proceed();

			return handleSuccess(response, auditLog, auditApi, startTime);

		} catch (Throwable ex) {

			handleFailure(ex, auditLog, startTime);

			throw ex;
		}
	}

	/**
	 * =========================================================== Handle Successful
	 * API Execution ===========================================================
	 *
	 * <p>
	 * Populates the audit log with response details, execution time and persists
	 * the audit record.
	 * </p>
	 *
	 * @param response  Controller response
	 * @param auditLog  Current audit log
	 * @param startTime Request start time
	 *
	 * @return Original controller response
	 */
	private Object handleSuccess(Object response, AuditLog auditLog, AuditApi auditApi, long startTime) {

		Object responseBody = response;

		if (response instanceof ResponseEntity<?> entity) {

			auditLog.setResponseStatus(entity.getStatusCode().value());

			auditLog.setResponseMessage(HttpStatusCode.getDescription(entity.getStatusCode().value()));

			responseBody = entity.getBody();

		} else {

			auditLog.setResponseStatus(httpResponse.getStatus());

			auditLog.setResponseMessage(HttpStatusCode.getDescription(httpResponse.getStatus()));
		}

		/*
		 * Mask sensitive response fields before persisting.
		 */
		Object maskedResponse = AuditMaskingUtil.maskObject(responseBody);

		if (auditApi.logResponse()) {

			auditLog.setResponseBody(convertToDocument(maskedResponse));
		}

		auditLog.setExecutionTimeMs(System.currentTimeMillis() - startTime);

		logResponse(auditLog, responseBody);

		saveAuditLog(auditLog);

		LOGGER.info("AUDIT SUCCESS | RequestId={} | Duration={} ms", auditLog.getRequestId(),
				auditLog.getExecutionTimeMs());

		return response;
	}

	/**
	 * =========================================================== Handle Failed API
	 * Execution ===========================================================
	 *
	 * <p>
	 * Populates the audit log with exception details, execution time and persists
	 * the audit record before propagating the exception.
	 * </p>
	 *
	 * @param exception Exception thrown by controller/service
	 * @param auditLog  Current audit log
	 * @param startTime Request start time
	 */
	private void handleFailure(Throwable exception, AuditLog auditLog, long startTime) {

		/*
		 * Determine HTTP status.
		 */
		if (httpResponse.getStatus() > 0) {

			auditLog.setResponseStatus(httpResponse.getStatus());

			auditLog.setResponseMessage(HttpStatusCode.getDescription(httpResponse.getStatus()));

		} else {

			auditLog.setResponseStatus(HttpStatusCode.INTERNAL_SERVER_ERROR);

			auditLog.setResponseMessage(HttpStatusCode.getDescription(HttpStatusCode.INTERNAL_SERVER_ERROR));
		}

		/*
		 * Store exception details.
		 */
		auditLog.setExceptionMessage(exception.getMessage());

		/*
		 * Calculate execution time.
		 */
		auditLog.setExecutionTimeMs(System.currentTimeMillis() - startTime);

		LOGGER.error("""

				================= ERROR =================
				RequestId    : {}
				HTTP Method  : {}
				URL          : {}
				Duration(ms) : {}
				Exception    : {}
				=========================================
				""", auditLog.getRequestId(), auditLog.getRequestMethod(), auditLog.getApi(),
				auditLog.getExecutionTimeMs(), exception.getMessage(), exception);

		saveAuditLog(auditLog);

		LOGGER.error("AUDIT FAILURE | RequestId={} | Duration={} ms", auditLog.getRequestId(),
				auditLog.getExecutionTimeMs());
	}

	/**
	 * =========================================================== Persist Audit Log
	 * ===========================================================
	 *
	 * <p>
	 * Persists the audit log asynchronously. Failure to save an audit record must
	 * never impact the API.
	 * </p>
	 *
	 * @param auditLog Audit log to persist
	 */
	private void saveAuditLog(AuditLog auditLog) {

		try {

			auditService.saveAuditLogAsync(auditLog);

		} catch (Exception ex) {

			LOGGER.error("Failed to persist audit log. RequestId={}", auditLog.getRequestId(), ex);
		}
	}

	/**
	 * =========================================================== Log Incoming
	 * Request ===========================================================
	 *
	 * @param auditLog Current audit log
	 */
	private void logRequest(AuditLog auditLog) {

		LOGGER.info("""

				================= REQUEST =================
				Timestamp   : {}
				RequestId   : {}
				HTTP Method : {}
				URL         : {}
				Payload     :
				{}
				===========================================
				""", auditLog.getCreatedAt().format(DataFormatUtil.LOG_DATE_FORMATTER), auditLog.getRequestId(),
				auditLog.getRequestMethod(), auditLog.getApi(), toPrettyJson(auditLog.getRequestBody()));
	}

	/**
	 * =========================================================== Log Outgoing
	 * Response ===========================================================
	 *
	 * @param auditLog Current audit log
	 * @param response Response body
	 */
	private void logResponse(AuditLog auditLog, Object response) {

		LOGGER.info("""

				================= RESPONSE =================
				RequestId    : {}
				Status       : {}
				Duration(ms) : {}
				Payload      :
				{}
				===========================================
				""", auditLog.getRequestId(), auditLog.getResponseStatus(), auditLog.getExecutionTimeMs(),
				toPrettyJson(response));
	}

	/**
	 * =========================================================== Build Request
	 * Document ===========================================================
	 *
	 * Converts controller arguments into MongoDB BSON format. Sensitive fields are
	 * automatically masked.
	 *
	 * @param args Controller arguments
	 *
	 * @return BSON document
	 */
	private Document buildRequestDocument(Object[] args) {

		Document requestDocument = new Document();

		for (Object arg : args) {

			if (arg == null) {
				continue;
			}

			try {

				if (arg instanceof MultipartFile file) {

					requestDocument.append("file", new Document().append("fileName", file.getOriginalFilename())
							.append("contentType", file.getContentType()).append("fileSize", file.getSize()));

					continue;
				}

				/*
				 * Mask sensitive fields.
				 */
				Object maskedObject = AuditMaskingUtil.maskObject(arg);

				requestDocument.append(arg.getClass().getSimpleName(), convertToDocument(maskedObject));

			} catch (Exception ex) {

				LOGGER.error("Failed to serialize request object.", ex);

				requestDocument.append(arg.getClass().getSimpleName(), "Serialization Failed");
			}
		}

		return requestDocument;
	}

	/**
	 * =========================================================== Convert Object To
	 * BSON Document ===========================================================
	 *
	 * @param object Java object
	 *
	 * @return BSON document
	 */
	private Document convertToDocument(Object object) {

		if (object == null) {
			return null;
		}

		try {

			return Document.parse(objectMapper.writeValueAsString(object));

		} catch (JsonProcessingException ex) {

			LOGGER.error("Failed to convert object to BSON document.", ex);

			return new Document().append("error", ex.getMessage());
		}
	}

	/**
	 * =========================================================== Pretty JSON
	 * Formatter ===========================================================
	 *
	 * @param object Java object
	 *
	 * @return Pretty formatted JSON
	 */
	private String toPrettyJson(Object object) {

		if (object == null) {
			return "null";
		}

		try {

			return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);

		} catch (JsonProcessingException ex) {

			return String.valueOf(object);
		}

	}
}

///**
// * ===================================================== Audit Aspect
// * =====================================================
// *
// * Intercepts all controller methods annotated with {@code @AuditApi}.
// *
// * Captures: - API URL - HTTP Method - Request ID - IP Address - Query
// * Parameters - Request Payload - Response Payload - Execution Time - Exception
// * Details
// *
// * Stores audit records asynchronously in MongoDB.
// *
// * =====================================================
// */
//@Aspect
//@Component
//public class AuditAspect {
//
//	private static final Logger LOGGER = LoggerFactory.getLogger(AuditAspect.class);
//
//	private final IAuditService auditService;
//	private final ObjectMapper objectMapper;
//	private final HttpServletRequest httpRequest;
//	private final HttpServletResponse httpResponse;
//
//	public AuditAspect(IAuditService auditService, ObjectMapper objectMapper, HttpServletRequest httpRequest,
//			HttpServletResponse httpResponse) {
//
//		this.auditService = auditService;
//		this.objectMapper = objectMapper;
//		this.httpRequest = httpRequest;
//		this.httpResponse = httpResponse;
//	}
//
//	/**
//	 * ===================================================== Audit API Calls
//	 * =====================================================
//	 *
//	 * Intercepts methods annotated with @AuditApi.
//	 *
//	 * =====================================================
//	 */
//	@Around("@annotation(com.foodies.freshmeal.common.audit.annotation.AuditApi)")
//	public Object audit(ProceedingJoinPoint joinPoint) throws Throwable {
//
//		long startTime = System.currentTimeMillis();
//
//		String requestId = UUID.randomUUID().toString();
//
//		LOGGER.info("AUDIT STARTED | RequestId={} | API={} | Method={}", requestId, httpRequest.getRequestURI(),
//				httpRequest.getMethod());
//
//		AuditLog auditLog = new AuditLog();
//		auditLog.setRequestId(requestId);
//		auditLog.setApi(httpRequest.getRequestURI());
//		auditLog.setMethod(httpRequest.getMethod());
//		auditLog.setIpAddress(httpRequest.getRemoteAddr());
//		auditLog.setQueryParams(httpRequest.getQueryString());
//		auditLog.setCreatedAt(LocalDateTime.now());
//
//		try {
//
//			auditLog.setRequestBody(buildRequestDocument(joinPoint.getArgs()));
//
//			LOGGER.info("""
//
//					\n ================= REQUEST =================
//					Timestamp : {}
//					RequestId : {}
//					Method    : {}
//					URL       : {}
//					Payload   :
//					{}
//					==========================================
//					""", auditLog.getCreatedAt().format(DataFormatUtil.LOG_DATE_FORMATTER), requestId,
//					httpRequest.getMethod(), httpRequest.getRequestURI(), toPrettyJson(auditLog.getRequestBody()));
//
//			Object response = joinPoint.proceed();
//
//			Object responseBody = response;
//
//			if (response instanceof ResponseEntity<?> entity) {
//
//				auditLog.setResponseStatus(entity.getStatusCode().value());
//
//				auditLog.setResponseMessage(HttpStatusCode.getDescription(entity.getStatusCode().value()));
//
//				responseBody = entity.getBody();
//
//			} else {
//
//				auditLog.setResponseStatus(httpResponse.getStatus());
//
//				auditLog.setResponseMessage(HttpStatusCode.getDescription(httpResponse.getStatus()));
//			}
//
//			auditLog.setResponseBody(convertToDocument(responseBody));
//
//			auditLog.setExecutionTimeMs(System.currentTimeMillis() - startTime);
//
//			LOGGER.info("""
//					\n ================= RESPONSE =================
//					RequestId    : {}
//					Status       : {}
//					Duration(ms) : {}
//					Payload      :
//					{}
//					============================================
//					""", requestId, auditLog.getResponseStatus(), auditLog.getExecutionTimeMs(),
//					toPrettyJson(responseBody));
//			try {
//				auditService.saveAuditLogAsync(auditLog);
//
//			} catch (Exception e) {
//				LOGGER.error("Failed to save audit log. RequestId={}", requestId, e);
//			}
//
//			LOGGER.info("AUDIT SUCCESS | RequestId={} | Duration={} ms", requestId, auditLog.getExecutionTimeMs());
//
//			return response;
//
//		} catch (Throwable ex) {
//
//			if (httpResponse.getStatus() > 0) {
//
//				auditLog.setResponseStatus(httpResponse.getStatus());
//
//				auditLog.setResponseMessage(HttpStatusCode.getDescription(httpResponse.getStatus()));
//
//			} else {
//
//				auditLog.setResponseStatus(HttpStatusCode.INTERNAL_SERVER_ERROR);
//
//				auditLog.setResponseMessage(HttpStatusCode.getDescription(HttpStatusCode.INTERNAL_SERVER_ERROR));
//			}
//			auditLog.setExceptionMessage(ex.getMessage());
//			auditLog.setExecutionTimeMs(System.currentTimeMillis() - startTime);
//			LOGGER.error("""
//					\n ================= ERROR =================
//					RequestId    : {}
//					Method       : {}
//					URL          : {}
//					Duration(ms) : {}
//					Error       :
//					{}
//					============================================
//					""", requestId, httpRequest.getMethod(), httpRequest.getRequestURI(), auditLog.getExecutionTimeMs(),
//					ex.getMessage(), ex);
//			try {
//				auditService.saveAuditLogAsync(auditLog);
//
//			} catch (Exception e) {
//				LOGGER.error("Failed to save audit log. RequestId={}", requestId, e);
//			}
//
//			LOGGER.error("AUDIT FAILURE | RequestId={} | Duration={} ms | Error={}", requestId,
//					auditLog.getExecutionTimeMs(), ex.getMessage(), ex);
//
//			throw ex;
//		}
//	}
//
//	/**
//	 * ===================================================== Build Request Document
//	 * =====================================================
//	 *
//	 * Converts controller arguments into Mongo Document format.
//	 *
//	 * Multipart files are converted into metadata instead of storing binary
//	 * content.
//	 *
//	 * =====================================================
//	 */
//	private Document buildRequestDocument(Object[] args) {
//		Document requestDoc = new Document();
//
//		for (Object arg : args) {
//			if (arg == null) {
//				continue;
//			}
//			try {
//				switch (arg) {
//				case MultipartFile file ->
//					requestDoc.append("file", new Document().append("fileName", file.getOriginalFilename())
//							.append("contentType", file.getContentType()).append("fileSize", file.getSize()));
//				case String str -> {
//					try {
//						requestDoc.append("foodRequest", Document.parse(str));
//
//					} catch (org.bson.json.JsonParseException | IllegalArgumentException ex) {
//						requestDoc.append("foodRequest", str);
//					}
//				}
//				default -> requestDoc.append(arg.getClass().getSimpleName(),
//						Document.parse(objectMapper.writeValueAsString(arg)));
//				}
//
//			} catch (JsonProcessingException e) {
//				LOGGER.error("Failed to serialize request argument : {}", e.getMessage(), e);
//				requestDoc.append("serializationError", e.getMessage());
//			}
//		}
//		return requestDoc;
//	}
//
//	/**
//	 * ===================================================== Convert Object To Mongo
//	 * Document =====================================================
//	 *
//	 * Converts any Java object into BSON Document suitable for MongoDB storage.
//	 *
//	 * =====================================================
//	 */
//	private Document convertToDocument(Object object) {
//
//		if (object == null) {
//			return null;
//		}
//
//		try {
//			return Document.parse(objectMapper.writeValueAsString(object));
//
//		} catch (JsonProcessingException e) {
//			LOGGER.error("Failed to convert object to document : {}", e.getMessage(), e);
//			return new Document().append("error", e.getMessage());
//		}
//	}
//
//	private String toPrettyJson(Object object) {
//
//		if (object == null) {
//			return "null";
//		}
//
//		try {
//
//			return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
//
//		} catch (JsonProcessingException ex) {
//
//			return String.valueOf(object);
//		}
//	}
//}