package com.foodies.freshmeal.authentication.repository;

import com.foodies.freshmeal.authentication.entity.EmailVerificationOtpEntity;
import com.foodies.freshmeal.common.repository.base.IBaseRepository;

/**
 * ============================================================================
 * Email Verification OTP Repository
 * ============================================================================
 *
 * Persistence repository for
 * {@link EmailVerificationOtpEntity}.
 *
 * <p>
 * This repository inherits the common FreshMeal persistence infrastructure
 * through {@link IBaseRepository}. Active-record filtering, soft deletion,
 * query-based access, and standard MongoDB persistence operations are therefore
 * handled by the existing repository architecture.
 * </p>
 *
 * <h3>Responsibility</h3>
 *
 * <p>
 * This repository is responsible only for persistence access. Authentication
 * business rules such as OTP expiration, attempt limits, hash verification,
 * revocation, and successful consumption remain within the Authentication
 * service layer.
 * </p>
 *
 * <p>
 * Authentication-specific lookups should use the existing
 * {@code Query}-based repository operations rather than introducing derived
 * query methods that could bypass FreshMeal's active-record filtering.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
public interface IEmailVerificationOtpRepository extends IBaseRepository<EmailVerificationOtpEntity, String> {

}