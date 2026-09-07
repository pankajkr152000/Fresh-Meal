package com.foodies.freshmeal.common.audit.util;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.foodies.freshmeal.common.audit.entity.impl.AuditLog;
import com.foodies.freshmeal.common.io.service.IServiceContext;
import com.foodies.freshmeal.user.entity.UserProfile;

import jakarta.servlet.http.HttpServletRequest;

/**
 * ===================================================== Audit Log Builder
 * =====================================================
 *
 * Responsible for creating and populating AuditLog instances with request
 * metadata and business context.
 *
 * This builder centralizes AuditLog creation so that AuditAspect remains
 * focused only on interception.
 *
 * Responsibilities:
 *
 * • Create AuditLog • Populate request information • Populate user information
 * • Populate business audit information
 *
 * @author Pankaj Kumar
 *
 *         =====================================================
 */
@Component
public class AuditLogBuilder {

	/**
	 * Builds a new AuditLog populated with request metadata.
	 *
	 * @param request        Current HTTP request
	 * @param serviceContext Current request service context
	 *
	 * @return populated AuditLog
	 */
	public AuditLog build(HttpServletRequest request, IServiceContext serviceContext) {

		AuditLog auditLog = new AuditLog();

		auditLog.setRequestId(UUID.randomUUID().toString());

		auditLog.setApi(request.getRequestURI());

		auditLog.setRequestUrl(request.getRequestURL().toString());

		auditLog.setRequestMethod(request.getMethod());

		auditLog.setQueryParams(request.getQueryString());

		auditLog.setIpAddress(request.getRemoteAddr());

		auditLog.setCreatedAt(LocalDateTime.now());

		populateUserInformation(auditLog, serviceContext);

		populateBusinessInformation(auditLog, serviceContext);

		return auditLog;
	}

	/**
	 * Populates authenticated user information.
	 *
	 * <p>
	 * User identity and role information are obtained from the authenticated
	 * {@link UserProfile}. The user number is used as the business-facing user
	 * identifier, while the assigned roles are represented for audit purposes.
	 * </p>
	 *
	 * @param auditLog       audit log being populated.
	 * @param serviceContext current service context.
	 */
	private void populateUserInformation(AuditLog auditLog, IServiceContext serviceContext) {

		if (serviceContext == null || serviceContext.getUserProfile() == null) {
			return;
		}

		UserProfile userProfile = serviceContext.getUserProfile();

		auditLog.setUserId(userProfile.getUserNumber());

		if (userProfile.getUserEntity() != null && userProfile.getUserEntity().getRoles() != null) {

			auditLog.setRole(userProfile.getUserEntity().getRoles());
		}
	}

	/**
	 * Populates business execution metadata.
	 */
	private void populateBusinessInformation(AuditLog auditLog, IServiceContext serviceContext) {

		if (serviceContext == null) {

			return;
		}

		auditLog.setModule(serviceContext.getModuleType());

		auditLog.setAction(serviceContext.getActionType());

		auditLog.setMethod(serviceContext.getMethodType());
	}

}
