
package com.foodies.freshmeal.admin.service;

import com.foodies.freshmeal.admin.dto.ApplicationApprovalDTO;
import com.foodies.freshmeal.admin.dto.ApplicationCancellationDTO;
import com.foodies.freshmeal.admin.dto.ApplicationCorrectionRequestDTO;
import com.foodies.freshmeal.admin.dto.ApplicationDecisionDTO;
import com.foodies.freshmeal.admin.dto.ApplicationNumberRequestDTO;
import com.foodies.freshmeal.admin.dto.ApplicationRejectionDTO;
import com.foodies.freshmeal.admin.dto.ApplicationResubmissionDTO;
import com.foodies.freshmeal.admin.dto.ApplicationReviewPageResponse;
import com.foodies.freshmeal.admin.dto.ApplicationReviewResponse;
import com.foodies.freshmeal.admin.dto.ApplicationReviewSearchDTO;
import com.foodies.freshmeal.admin.dto.ApplicationSubmissionDTO;
import com.foodies.freshmeal.admin.dto.ApplicationWithdrawalDTO;
import com.foodies.freshmeal.common.io.service.IServiceInput;
import com.foodies.freshmeal.common.io.service.IServiceOutput;

/**
 * ============================================================================
 * Admin Application Review Service Contract
 * ============================================================================
 *
 * <p>
 * Defines the business operations for managing Restaurant and Delivery
 * Partner onboarding applications.
 * </p>
 *
 * <p>
 * This service owns the application review lifecycle, including submission,
 * administrative review, decisions, applicant actions and application
 * retrieval.
 * </p>
 *
 * <p>
 * Domain-specific business data remains owned by the Restaurant and
 * Delivery Partner modules. User authorization and role management remain
 * owned by the User module.
 * </p>
 *
 * ============================================================================
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
public interface IAdminApplicationReviewService {

    // =========================================================================
    // Application Submission
    // =========================================================================

    /**
     * Submits a new Restaurant or Delivery Partner application.
     *
     * <p>
     * A previously rejected application may be referenced when submitting
     * a new application of the same type.
     * </p>
     *
     * @param input application submission input
     * @return submitted application details
     */
    IServiceOutput<ApplicationReviewResponse> submitApplication(
            IServiceInput<ApplicationSubmissionDTO> input);

    // =========================================================================
    // Application Review
    // =========================================================================

    /**
     * Starts the administrative review of a submitted application.
     *
     * @param input application review action input
     * @return updated application details
     */
    IServiceOutput<ApplicationReviewResponse> startReview(
            IServiceInput<ApplicationDecisionDTO> input);

    /**
     * Requests corrections from the applicant.
     *
     * <p>
     * Correction instructions are mandatory. The application transitions
     * to NEEDS_CORRECTION.
     * </p>
     *
     * @param input correction request input
     * @return updated application details
     */
    IServiceOutput<ApplicationReviewResponse> requestCorrections(
            IServiceInput<ApplicationCorrectionRequestDTO> input);

    /**
     * Resubmits an application after the applicant has completed the
     * requested corrections.
     *
     * <p>
     * The existing application number is retained.
     * </p>
     *
     * @param input application resubmission input
     * @return updated application details
     */
    IServiceOutput<ApplicationReviewResponse> resubmitApplication(
            IServiceInput<ApplicationResubmissionDTO> input);

    // =========================================================================
    // Administrative Decisions
    // =========================================================================

    /**
     * Approves an application and initiates the corresponding user-role grant.
     *
     * <p>
     * Approval and role granting must be coordinated transactionally.
     * </p>
     *
     * @param input application decision input
     * @return approved application details
     */
    IServiceOutput<ApplicationReviewResponse> approveApplication(
            IServiceInput<ApplicationApprovalDTO> input);

    /**
     * Rejects an application.
     *
     * <p>
     * A rejection reason is mandatory. The applicant may submit a new
     * application later with a new application number.
     * </p>
     *
     * @param input application decision input
     * @return rejected application details
     */
    IServiceOutput<ApplicationReviewResponse> rejectApplication(
            IServiceInput<ApplicationRejectionDTO> input);

    /**
     * Cancels an application through an authorized administrative
     * or system operation.
     *
     * @param input application cancellation input
     * @return cancelled application details
     */
    IServiceOutput<ApplicationReviewResponse> cancelApplication(
            IServiceInput<ApplicationCancellationDTO> input);

    // =========================================================================
    // Applicant Operations
    // =========================================================================

    /**
     * Withdraws an application submitted by the authenticated applicant.
     *
     * @param input application withdrawal input
     * @return withdrawn application details
     */
    IServiceOutput<ApplicationReviewResponse> withdrawApplication(
            IServiceInput<ApplicationWithdrawalDTO> input);

    // =========================================================================
    // Application Retrieval
    // =========================================================================

    /**
     * Retrieves an application by its business-facing application number.
     *
     * <p>
     * Access must be restricted according to the authenticated user's
     * authorization and application ownership.
     * </p>
     *
     * @param input application number request
     * @return application details including review history
     */
    IServiceOutput<ApplicationReviewResponse> getApplicationByNumber(
            IServiceInput<ApplicationNumberRequestDTO> input);

    /**
     * Retrieves applications belonging to the authenticated applicant.
     *
     * <p>
     * Supports filtering and pagination.
     * </p>
     *
     * @param input application search input
     * @return paginated applicant applications
     */
    IServiceOutput<ApplicationReviewPageResponse> getMyApplications(
            IServiceInput<ApplicationReviewSearchDTO> input);

    /**
     * Retrieves applications for administrative review.
     *
     * <p>
     * Supports filtering by application type, status and submission
     * information, along with pagination and sorting.
     * </p>
     *
     * @param input application search input
     * @return paginated application results
     */
    IServiceOutput<ApplicationReviewPageResponse> getApplications(
            IServiceInput<ApplicationReviewSearchDTO> input);
}
