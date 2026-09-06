package com.foodies.freshmeal.user.repository;

import com.foodies.freshmeal.common.repository.base.IBaseRepository;
import com.foodies.freshmeal.user.entity.LoginHistoryEntity;

/**
 * ============================================================================
 * Repository : ILoginHistoryRepository
 * ============================================================================
 *
 * Repository abstraction for {@link LoginHistoryEntity}.
 *
 * <p>
 * This repository provides persistence access to FreshMeal authentication
 * history records while reusing the standard repository infrastructure
 * provided by {@link IBaseRepository}.
 * </p>
 *
 * <h3>Responsibilities</h3>
 * <ul>
 * <li>Persist authentication history records.</li>
 * <li>Retrieve authentication history records.</li>
 * <li>Support login and logout history operations.</li>
 * <li>Reuse common active-record and soft-delete behavior.</li>
 * </ul>
 *
 * <h3>Repository Design</h3>
 * <p>
 * Authentication history lookup requirements are intentionally not exposed
 * through authentication-specific derived query methods. The existing
 * {@code Query}-based repository infrastructure can be used to compose
 * queries according to the required authentication history use case.
 * </p>
 *
 * <p>
 * This keeps the repository consistent with the FreshMeal common repository
 * architecture and avoids duplicating persistence behavior.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
public interface ILoginHistoryRepository extends IBaseRepository<LoginHistoryEntity, String> {

}