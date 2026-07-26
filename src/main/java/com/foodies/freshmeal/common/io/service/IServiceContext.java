package com.foodies.freshmeal.common.io.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.context.ApplicationContext;

import com.foodies.freshmeal.common.audit.entity.impl.AuditDataInput;
import com.foodies.freshmeal.common.constants.ActionType;
import com.foodies.freshmeal.common.constants.MethodType;
import com.foodies.freshmeal.common.constants.ModuleType;
import com.foodies.freshmeal.common.exception.IError;
import com.foodies.freshmeal.common.exception.IErrors;
import com.foodies.freshmeal.common.io.IDataContext;
import com.foodies.freshmeal.common.validation.model.ValidationResult;
import com.foodies.freshmeal.user.entity.UserProfile;

public interface IServiceContext extends IDataContext {

    public AuditDataInput getAuditDataInput();

    public LocalDateTime getRequestReceivedTime();

    public ApplicationContext getApplicationContext();

    public UserProfile getUserProfile();

    public List<IError> getOverridentErrors();

    public void addOverridentError(IError error);

    public void addOverridentErrors(IErrors errors);

    public Boolean hasOverridentErrors();

    public void addOverridentError(List<IError> errors);

    public String getRequestId();

    public AuditDataInput getAuditData();

    public List<IError> getValidationErrors();

    public String getCorrelationId();

    public void setAuditDataInput(AuditDataInput auditDataInput);

    public void setRequestReceivedTime(LocalDateTime requestReceivedTime);

    public void setApplicationContext(ApplicationContext applicationContext);

    public void setUserProfile(UserProfile userProfile);

    public void setCorrelationId(String correlationId);

    public void setValidationErrors(List<IError> validationErrors);

    public void setOverridentErrors(List<IError> overridentErrors);

    public void setOverridentErrors(IErrors overridentErrors);

    public void setOverridentError(IError overridentError);

    public void setOverridentError(List<IError> overridentErrors);

    public void setRequestId(String requestId);

    public ModuleType getModuleType();

    public void setModuleType(ModuleType moduleType);

    public ActionType getActionType();

    public void setActionType(ActionType actionType);

    public MethodType getMethodType();

    public void setMethodType(MethodType methodType);

    public ValidationResult getValidationResult();

    public void setValidationResult(ValidationResult validationResult);

}
