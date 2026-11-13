package com.foodies.freshmeal.admin.service;

import com.foodies.freshmeal.admin.entity.ApplicationReviewEntity;
import com.foodies.freshmeal.common.io.service.IServiceInput;
import com.foodies.freshmeal.common.io.service.IServiceOutput;

/**
 * ============================================================================
 * Admin Application Review Service Contract
 * ============================================================================
 *
 * <p>
 * Defines business operations for managing Restaurant and Delivery Partner
 * onboarding applications.
 * </p>
 */
public interface IAdminApplicationReviewService {

    /**
     * Submits a new application or a reapplication after rejection.
     *
     * @param input application submission input
     * @return submitted application
     */
    IServiceOutput<ApplicationReviewEntity> submitApplication(
            IServiceInput<ApplicationSubmissionDTO> input);
}
