package com.foodies.freshmeal.authentication.repository;

import com.foodies.freshmeal.authentication.entity.PendingRegistrationEntity;
import com.foodies.freshmeal.common.repository.base.IBaseRepository;

/**
 * Repository responsible for persistence and retrieval of
 * pending user registration records.
 *
 * <p>
 * Inherits the standard MongoDB persistence operations,
 * custom query operations, and soft-delete management
 * from {@link IBaseRepository}.
 * </p>
 *
 * <p>
 * Registration lifecycle and business validations belong
 * to the authentication service layer.
 * </p>
 *
 * @author FreshMeal
 */
public interface IPendingRegistrationRepository extends IBaseRepository<PendingRegistrationEntity, String> {

}