package com.foodies.freshmeal.common.io.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import com.foodies.freshmeal.common.audit.entity.impl.AuditDataInput;
import com.foodies.freshmeal.common.constants.ActionType;
import com.foodies.freshmeal.common.constants.MethodType;
import com.foodies.freshmeal.common.constants.ModuleType;
import com.foodies.freshmeal.common.exception.IError;
import com.foodies.freshmeal.common.exception.IErrors;
import com.foodies.freshmeal.common.io.service.IServiceContext;
import com.foodies.freshmeal.common.validation.model.ValidationResult;
import com.foodies.freshmeal.user.entity.UserProfile;

@Component
@RequestScope
public class ServiceContext implements IServiceContext {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceContext.class);

    private AuditDataInput auditDataInput;

    private LocalDateTime requestReceivedTime;

    private ApplicationContext applicationContext;

    private UserProfile userProfile;

    private String requestId;

    private String correlationId;

    private AuditDataInput auditData;

    private List<IError> validationErrors = new ArrayList<>();

    private List<IError> overridentErrors = new ArrayList<>();

    private Map<String, Object> attributes = new HashMap<>();

    private Date asOfBusinessDate;

    private static final long serialVersionUID = 1L;

    private ModuleType moduleType;

    private MethodType methodType;

    private ActionType actionType;

    private ValidationResult validationResult;

    public ServiceContext() {
        // Default constructor
    }

    // =====================================================
    // GETTERS
    // =====================================================

    @Override
    public AuditDataInput getAuditDataInput() {
        return auditDataInput;
    }

    @Override
    public LocalDateTime getRequestReceivedTime() {
        return requestReceivedTime;
    }

    @Override
    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public UserProfile getUserProfile() {
        return userProfile;
    }

    @Override
    public List<IError> getOverridentErrors() {
        return overridentErrors;
    }

    @Override
    public String getRequestId() {
        return requestId;
    }

    @Override
    public AuditDataInput getAuditData() {
        return auditData;
    }

    @Override
    public List<IError> getValidationErrors() {
        return validationErrors;
    }

    @Override
    public String getCorrelationId() {
        return correlationId;
    }

    // =====================================================
    // SETTERS
    // =====================================================

    @Override
    public void setAuditDataInput(
            AuditDataInput auditDataInput) {

        LOGGER.debug(
                "Setting AuditDataInput");

        this.auditDataInput = auditDataInput;
    }

    @Override
    public void setRequestReceivedTime(
            LocalDateTime requestReceivedTime) {

        this.requestReceivedTime = requestReceivedTime;
    }

    @Override
    public void setApplicationContext(
            ApplicationContext applicationContext) {

        this.applicationContext = applicationContext;
    }

    @Override
    public void setUserProfile(
            UserProfile userProfile) {

        this.userProfile = userProfile;
    }

    @Override
    public void setCorrelationId(
            String correlationId) {

        this.correlationId = correlationId;
    }

    @Override
    public void setValidationErrors(
            List<IError> validationErrors) {

        this.validationErrors = validationErrors;
    }

    @Override
    public void setOverridentErrors(
            List<IError> overridentErrors) {

        this.overridentErrors = overridentErrors;
    }

    @Override
    public void setOverridentErrors(
            IErrors overridentErrors) {

        if (overridentErrors != null &&
                overridentErrors.getErrors() != null) {

            this.overridentErrors = overridentErrors.getErrors();
        }
    }

    @Override
    public void setOverridentError(
            IError overridentError) {

        if (overridentError != null) {

            this.overridentErrors
                    .add(overridentError);
        }
    }

    @Override
    public void setOverridentError(
            List<IError> overridentErrors) {

        if (overridentErrors != null) {

            this.overridentErrors
                    .addAll(overridentErrors);
        }
    }

    @Override
    public void setRequestId(
            String requestId) {

        this.requestId = requestId;
    }

    // =====================================================
    // ERROR HELPERS
    // =====================================================

    @Override
    public void addOverridentError(
            IError error) {

        if (error != null) {

            overridentErrors.add(error);
        }
    }

    @Override
    public void addOverridentErrors(
            IErrors errors) {

        if (errors != null &&
                errors.getErrors() != null) {

            overridentErrors.addAll(
                    errors.getErrors());
        }
    }

    @Override
    public void addOverridentError(
            List<IError> errors) {

        if (errors != null) {

            overridentErrors.addAll(
                    errors);
        }
    }

    @Override
    public Boolean hasOverridentErrors() {

        return !overridentErrors.isEmpty();
    }

    // =====================================================
    // Attribute HELPERS
    // =====================================================
    @Override
    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    @Override
    public boolean hasAttribute(String name) {
        return attributes.containsKey(name);
    }

    @Override
    public void setAttribute(String attributeName, Object attributevalue) {
        attributes.put(attributeName, attributevalue);
    }

    @Override
    public Map<String, Object> getAllAttributes() {
        return new HashMap<>(attributes);
    }

    @Override
    public void setAttributes(Map<String, Object> attributes) {
        if (attributes == null) {
            this.attributes = new HashMap<>();
        } else {
            this.attributes = new HashMap<>(attributes);
        }
    }

    @Override
    public Date getAsOfBusinessDate() {
        return asOfBusinessDate;
    }

    @Override
    public void setAsOfBusinessDate(Date asOfBusinessDate) {
        this.asOfBusinessDate = asOfBusinessDate;
    }

    @Override
    public Date getAsOfDateBusiness() {
        return getAsOfBusinessDate();
    }

    // =====================================================
    // Constant HELPERS
    // =====================================================

    @Override
    public ModuleType getModuleType() {
        return moduleType;
    }

    @Override
    public void setModuleType(ModuleType moduleType) {
        this.moduleType = moduleType;
    }

    @Override
    public MethodType getMethodType() {
        return methodType;
    }

    @Override
    public void setMethodType(MethodType methodType) {
        this.methodType = methodType;
    }

    @Override
    public ActionType getActionType() {
        return actionType;
    }

    @Override
    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

    @Override
    public ValidationResult getValidationResult() {
        return validationResult;
    }

    @Override
    public void setValidationResult(ValidationResult validationResult) {
        this.validationResult = validationResult;
    }

}
