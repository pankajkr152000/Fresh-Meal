package com.foodies.freshmeal.common.audit.annotation;

import com.foodies.freshmeal.common.constants.ActionType;
import com.foodies.freshmeal.common.constants.ModuleType;
import com.foodies.freshmeal.common.constants.MethodType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * =========================================================== Audit API
 * Annotation ===========================================================
 *
 *
 *
 * Marks an API for audit logging and provides business metadata used by the
 * audit framework.
 * 
 * 
 * ========= USES ========= 
 * Example 1 : 
 * @AuditApi( 
 * 		module = ModuleType.AUTH,
 * 		action = ActionType.RESET_PASSWORD, 
 * 		logRequest = false, 
 * 		logResponse = false )
 * 
 * 
 * Example 2 : 
 * @AuditApi( 
 * 		module = ModuleType.FOOD, 
 * 		action = ActionType.CREATE
 * ) 
 * @PostMapping("/add")
 * 
 * ==================
 *    Retrieve
 * ==================
 * MethodSignature signature = (MethodSignature) joinPoint.getSignature();
 * AuditApi auditApi = signature.getMethod().getAnnotation(AuditApi.class);
 * 
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuditApi {

	/**
	 * Business module.
	 */
	ModuleType module();

	/**
	 * Business action.
	 */
	ActionType action();

	/**
	 * Business method.
	 */
	MethodType method() default MethodType.API;

	/**
	 * Whether request body should be logged.
	 */
	boolean logRequest() default true;

	/**
	 * Whether response body should be logged.
	 */
	boolean logResponse() default true;
}