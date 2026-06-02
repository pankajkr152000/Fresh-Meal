package com.foodies.freshmeal.common.io.service;


import java.time.LocalDateTime;

import com.foodies.freshmeal.common.audit.entity.impl.AuditDataInput;
import com.foodies.freshmeal.common.io.IDataContext;
import com.foodies.freshmeal.user.entity.IUserProfile;

public interface IDaoContext extends IDataContext {

    public AuditDataInput getAuditDataInput();
    public void setAuditDataInput(AuditDataInput auditDataInput);

    public LocalDateTime getLocalDateTime();
    public void setLocalDateTime(LocalDateTime LocalDateTime);

    public IUserProfile getUserProfile();
    public void setUserProfile(Object userData);

    public void setTcn(Long tcn);
    public Long getTcn();

}
