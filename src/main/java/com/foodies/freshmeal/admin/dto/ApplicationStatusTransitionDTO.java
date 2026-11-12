
package com.foodies.freshmeal.admin.dto;

import com.foodies.freshmeal.admin.constants.ApplicationStatusConstant;
import com.foodies.freshmeal.admin.valueObject.ApplicationReviewHistory;

import lombok.Getter;
import lombok.Setter;

/**
 * Contains the information required to perform an atomic
 * application status transition.
 *
 * @author Pankaj Kumar
 * @since 2.0
 */
@Getter
@Setter
public class ApplicationStatusTransitionDTO {

    private String applicationNumber;

    private ApplicationStatusConstant expectedStatus;

    private ApplicationStatusConstant targetStatus;

    private ApplicationReviewHistory reviewHistory;
}
