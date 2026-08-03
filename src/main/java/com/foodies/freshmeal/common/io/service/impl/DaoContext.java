package com.foodies.freshmeal.common.io.service.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import com.foodies.freshmeal.common.audit.entity.impl.AuditDataInput;
import com.foodies.freshmeal.common.date.AppCalendar;
import com.foodies.freshmeal.common.date.DateConverter;
import com.foodies.freshmeal.common.exception.IErrors;
import com.foodies.freshmeal.common.exception.SystemConfigBean;
import com.foodies.freshmeal.common.exception.impl.ErrorsImpl;
import com.foodies.freshmeal.common.io.IDataContext;
import com.foodies.freshmeal.common.io.service.IDaoContext;
import com.foodies.freshmeal.user.entity.UserProfile;

public class DaoContext implements IDaoContext {
    private static final long serialVersionUID = 1396937515847253810L;

    private final IDataContext delegate;

    private final Map<String, Object> attributeMap = new LinkedHashMap<>();

    private static final String TCN_KEY = "TCN_KEY";
    private static final String TCN_UPDATED_KEY = "TCN_UPDATED_KEY";
    private static final String USER_KEY = "USER_KEY";
    private static final String TIMESTAMP_KEY = "TIMESTAMP_KEY";
    private static final String AUDIT_DATA_KEY = "AUDIT_DATA_KEY";
    private static final String IGNORE_WARNING_KEY = "IGNORE_WARNING_KEY";
    private static final String INFORMATION_MESSAGE_KEY = "INFORMATION_MESSAGE_KEY";
    private Date asOfBusinessDate = null;

    /**
     *
     */
    public DaoContext() {
        delegate = this;
        delegate.getAllAttributes().put(TCN_UPDATED_KEY, false);
    }

    public DaoContext(IDataContext dataContext) {
        delegate = dataContext;
    }

    @Override
    public Object getAttribute(String key) {

        String absoluteFilePath = (SystemConfigBean.getAttribute("//Config/EnvSystem/DataRoot"))
                .concat("/PolicyDueProbe");
        String absoluteFileName = absoluteFilePath + File.separator + "SearchProbe" + ".txt";
        // BufferedWriter writer = null;
        @SuppressWarnings("unused")
        Boolean fileCreated = new File(absoluteFilePath).mkdirs();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(absoluteFileName), true));) {
            // writer = new BufferedWriter(new FileWriter(new File
            // (absoluteFileName),true));
            writer.write("\n In DaoContext.getAttribute() Value for Key " + key + " ------ "
                    + delegate.getAllAttributes().get(key));
            writer.close();
        } catch (Exception e) {
            // Log exception instead of printStackTrace
        }
        /*
         * finally
         * {
         * if(writer!=null)
         * {
         * try{
         * writer.close();
         * }catch(Exception e)
         * {
         * 
         * }
         * }
         * }
         */

        return delegate.getAllAttributes().get(key);
    }

    @Override
    public boolean hasAttribute(String key) {
        return delegate.getAllAttributes().containsKey(key);
    }

    @Override
    public void setAttribute(String key, Object value) {

        String absoluteFilePath = (SystemConfigBean.getAttribute("//Config/EnvSystem/DataRoot"))
                .concat("/PolicyDueProbe");
        String absoluteFileName = absoluteFilePath + File.separator + "SearchProbe" + ".txt";

        // BufferedWriter writer = null;
        @SuppressWarnings("unused")
        Boolean fileCreated = new File(absoluteFilePath).mkdirs();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(absoluteFileName), true))) {
            // writer = new BufferedWriter(new FileWriter(new File
            // (absoluteFileName),true));
            writer.write("\n In DaoContext.setAttribute() Key = " + key + " ------ Value = " + value);
            writer.close();
        } catch (Exception e) {
            // Log exception instead of printStackTrace
        }
        /*
         * finally
         * {
         * if(writer!=null)
         * {
         * try{
         * writer.close();
         * }catch(Exception e)
         * {
         * 
         * }
         * }
         * }
         */

        delegate.getAllAttributes().put(key, value);
    }

    @Override
    public Map<String, Object> getAllAttributes() {
        if (delegate != this) {
            return delegate.getAllAttributes();
        }
        return attributeMap;
    }

    public void setAllAttributes(Map<String, Object> attributes) {
        this.delegate.getAllAttributes().putAll(attributes);
    }

    @Override
    public Long getTcn() {
        return (Long) delegate.getAllAttributes().get(TCN_KEY);
    }

    @Override
    public void setTcn(Long tcn) {
        delegate.getAllAttributes().put(TCN_KEY, tcn);
    }

    public Boolean isTcnUpdated() {
        return (Boolean) delegate.getAllAttributes().get(TCN_UPDATED_KEY);
    }

    public void setTcnUpated(Boolean tcnUpdated) {
        delegate.getAllAttributes().put(TCN_UPDATED_KEY, tcnUpdated);
    }

    @Override
    public void setUserProfile(Object userData) {
        delegate.getAllAttributes().put(USER_KEY, userData);
    }

    @Override
    public UserProfile getUserProfile() {
        return (UserProfile) delegate.getAllAttributes().get(USER_KEY);
    }

    @Override
    public LocalDateTime getLocalDateTime() {
        return (LocalDateTime) delegate.getAllAttributes().get(TIMESTAMP_KEY);
    }

    @Override
    public void setLocalDateTime(LocalDateTime timestamp) {
        delegate.getAllAttributes().put(TIMESTAMP_KEY, timestamp);
    }

    /**
     * 
     * @return
     */
    @Override
    public AuditDataInput getAuditDataInput() {
        return (AuditDataInput) delegate.getAllAttributes().get(AUDIT_DATA_KEY);
    }

    /**
     *
     * @param auditDataInput
     */
    @Override
    public void setAuditDataInput(AuditDataInput auditDataInput) {
        // attributes.put(AUDIT_DATA_KEY, auditDataInput);
        delegate.getAllAttributes().put(AUDIT_DATA_KEY, auditDataInput);
    }

    public Boolean isIgnoreWarning() {
        return (Boolean) delegate.getAllAttributes().get(IGNORE_WARNING_KEY);
    }

    public void setIgnoreWarning(Boolean ignoreWarning) {
        delegate.getAllAttributes().put(IGNORE_WARNING_KEY, ignoreWarning);
    }

    /**
     * @return IErrors information collection holder
     */
    public IErrors getInfoMessage() {
        IErrors infoCollection = (IErrors) delegate.getAllAttributes().get(INFORMATION_MESSAGE_KEY);
        if (infoCollection == null) {
            infoCollection = new ErrorsImpl();
            delegate.getAllAttributes().put(INFORMATION_MESSAGE_KEY, infoCollection);
        }
        return infoCollection;
    }

    public void setAdditionalContext(String key, Object context) {
        delegate.getAllAttributes().put(key, context);
    }

    public Object getAdditionalContext(String key) {
        return delegate.getAllAttributes().get(key);
    }

    @Override
    public void setAttributes(Map<String, Object> attributes) {
        this.delegate.getAllAttributes().putAll(attributes);
    }

    @Override
    public Date getAsOfBusinessDate() {
        if (asOfBusinessDate == null) {
            asOfBusinessDate = DateConverter.toDate(AppCalendar.getBusinessLocalDateTime());
        }
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

}
