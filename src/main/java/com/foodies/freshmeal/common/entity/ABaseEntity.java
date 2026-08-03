package com.foodies.freshmeal.common.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.foodies.freshmeal.common.constants.RecordStatus;
import com.foodies.freshmeal.common.date.DateConstants;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class ABaseEntity implements IEntity {
    private static final long serialVersionUID = 76880402025344019L;
    @JsonFormat(pattern = DateConstants.DEFAULT_DATE_TIME_FORMAT)
    private LocalDateTime createdAt;
    private String createdBy;

    @JsonFormat(pattern = DateConstants.DEFAULT_DATE_TIME_FORMAT)
    private LocalDateTime updatedAt;
    private String updatedBy;

    private boolean deletedFlag = false;

    @JsonFormat(pattern = DateConstants.DEFAULT_DATE_TIME_FORMAT)
    private LocalDateTime deletedAt;
    private String deletedBy;

    private RecordStatus recordStatus = RecordStatus.ACTIVE;
}
