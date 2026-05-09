package com.foodies.freshmeal.common.io.service;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.context.ApplicationContext;

import com.foodies.freshmeal.common.audit.entity.impl.AuditDataInput;
import com.foodies.freshmeal.common.exception.IError;
import com.foodies.freshmeal.common.exception.IErrors;
import com.foodies.freshmeal.common.io.IDataContext;
import com.foodies.freshmeal.user.entity.IUserProfile;

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
