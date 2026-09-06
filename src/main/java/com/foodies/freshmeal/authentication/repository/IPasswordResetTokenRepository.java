package com.foodies.freshmeal.authentication.repository;

import com.foodies.freshmeal.authentication.entity.PasswordResetTokenEntity;
import com.foodies.freshmeal.common.repository.base.IBaseRepository;

/**
 * ============================================================================
 * Repository : IPasswordResetTokenRepository
 * ============================================================================
 *
 * Repository abstraction for {@link PasswordResetTokenEntity}.
 *
 * <p>
 * This repository provides persistence access to password reset token records
 * while reusing the standard FreshMeal repository infrastructure provided by
 * {@link IBaseRepository}.
 * </p>
 *
 * <h3>Responsibilities</h3>
 * <ul>
 * <li>Persist password reset token records.</li>
 * <li>Retrieve password reset token records.</li>
 * <li>Support password reset token lifecycle operations.</li>
 * <li>Reuse common active-record and soft-delete behavior.</li>
 * </ul>
 *
 * <h3>Repository Design</h3>
 * <p>
 * No authentication-specific derived query methods are declared here.
 * Password reset token lookup rules such as token validity, expiration,
 * usage, and revocation are composed using the existing
 * {@code Query}-based methods inherited from {@link IBaseRepository}.
 * </p>
 *
 * <p>
 * This keeps authentication persistence logic aligned with the common
 * FreshMeal repository architecture.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
public interface IPasswordResetTokenRepository
        extends IBaseRepository<PasswordResetTokenEntity, String> {

}