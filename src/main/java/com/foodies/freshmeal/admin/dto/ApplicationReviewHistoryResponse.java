
package com.foodies.freshmeal.admin.dto;

import java.time.LocalDateTime;

import com.foodies.freshmeal.admin.constants.ApplicationReviewActionConstant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ============================================================================
 * DTO : ApplicationReviewHistoryResponse
 * ============================================================================
 *
 * <p>
 * Carries an individual application review history entry as part of
 * an application review response.
 * </p>
 *
 * <p>
 * This DTO represents the action performed, the responsible actor,
 * the timestamp, and any remarks associated with the action.
 * </p>
 *
 * <p>
 * It is a response DTO and does not expose the internal
 * ApplicationReviewHistory value object directly.
 * </p>
 *
 * @author Pankaj Kumar
 * @since 2.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationReviewHistoryResponse {

    /**
     * Action performed during the application lifecycle.
     */
    private ApplicationReviewActionConstant action;

    /**
     * User number of the actor who performed the action.
     */
    private String performedByUserNumber;

    /**
     * Timestamp when the action was performed.
     */
    private LocalDateTime performedAt;

    /**
     * Remarks associated with the action.
     */
    private String remarks;

}
