package com.foodies.freshmeal.common.audit.annotation;

import com.foodies.freshmeal.common.constants.ActionType;
import com.foodies.freshmeal.common.constants.ModuleType;
import com.foodies.freshmeal.common.constants.MethodType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ====================================================================================
 * <b>Audit API Annotation</b>
 * ====================================================================================
 *
 * <p>
 * Marks an API for audit logging and provides business metadata used by the
 * audit framework.
 * </p>
 *
 * <h3>Uses</h3>
 *
 * <p>
 * <b>Example 1:</b>
 * </p>
 *
 * <pre>
 * {@code
 * @AuditApi(
 *     module = ModuleType.AUTH,
 *     action = ActionType.RESET_PASSWORD,
 *     method = MethodType.CREATE,
 *     logRequest = false,
 *     logResponse = false
 * )
 * }
 * </pre>
 *
 * <p>
 * <b>Example 2:</b>
 * </p>
 *
 * <pre>
 * {@code
 * @AuditApi(
 *     module = ModuleType.FOOD,
 *     action = ActionType.CREATE
 * )
 * @PostMapping("/add")
 * }
 * </pre>
 *
 * <h3>Retrieve</h3>
 *
 * <pre>
 * {@code
 * MethodSignature signature =
 *         (MethodSignature) joinPoint.getSignature();
 *
 * AuditApi auditApi =
 *         signature.getMethod().getAnnotation(AuditApi.class);
 * }
 * </pre>
 *
 * ============================================================================
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