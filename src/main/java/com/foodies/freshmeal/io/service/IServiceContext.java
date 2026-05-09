package com.foodies.freshmeal.io.service;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.context.ApplicationContext;

import com.foodies.freshmeal.audit.entity.impl.AuditDataInput;
import com.foodies.freshmeal.exception.IError;
import com.foodies.freshmeal.exception.IErrors;
import com.foodies.freshmeal.io.IDataContext;
import com.foodies.freshmeal.security.entity.IUserProfile;

public interface IServiceContext extends IDataContext {

    public AuditDataInput getAuditDataInput();

    public Timestamp getRequestReceivedTime();

    public ApplicationContext getApplicationContext();

    public IUserProfile getUserProfile();

    public List<IError> getOverridentErrors();

    public void addOverridentError(IError error);

    public void addOverridentErrors(IErrors errors);

    public Boolean hasOverridentErrors();

    public void addOverridentError(List<IError> errors);
}
