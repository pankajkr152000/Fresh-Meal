package com.foodies.freshmeal.common.audit.util;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.foodies.freshmeal.common.constants.RecordStatus;
import com.foodies.freshmeal.common.constants.RoleType;
import com.foodies.freshmeal.common.entity.ABaseEntity;
import com.foodies.freshmeal.common.io.service.IServiceContext;

/**
 * ===========================================================
 * Audit Utility
 * ===========================================================
 *
 * Utility responsible for populating audit-related fields on
 * entities.
 */
@Component
public class AuditUtils {

    private final IServiceContext serviceContext;

    public AuditUtils(IServiceContext serviceContext) {
        this.serviceContext = serviceContext;
    }

    /**
     * Populate fields for a newly created entity.
     *
     * @param entity entity to update
     */
    public void populateCreateAudit(ABaseEntity entity) {

        LocalDateTime now = LocalDateTime.now();
        String userId = resolveUserId();

        entity.setCreatedAt(now);
        entity.setCreatedBy(userId);

        entity.setUpdatedAt(now);
        entity.setUpdatedBy(userId);

        entity.setRecordStatus(RecordStatus.ACTIVE);
    }

    /**
     * Populate fields for an updated entity.
     *
     * @param entity entity to update
     */
    public void populateUpdateAudit(ABaseEntity entity) {

        entity.setUpdatedAt(LocalDateTime.now());
        entity.setUpdatedBy(resolveUserId());
    }

    /**
     * Populate fields for a soft-deleted entity.
     *
     * @param entity entity to update
     */
    public void populateDeleteAudit(ABaseEntity entity) {

        entity.setDeletedAt(LocalDateTime.now());
        entity.setDeletedBy(resolveUserId());
        entity.setRecordStatus(RecordStatus.DELETED);
    }

    /**
     * Resolve the current user identifier.
     *
     * @return user id or "SYSTEM" if unavailable
     */
    private String resolveUserId() {

        if (serviceContext.getUserProfile() != null
                && serviceContext.getUserProfile().getUserNumber() != null) {
            return serviceContext.getUserProfile().getUserNumber();
        }

        return RoleType.SYSTEM.name();
    }
}
