package com.foodies.freshmeal.common.exception;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import com.foodies.freshmeal.common.IAppEvent;
import com.foodies.freshmeal.common.exception.impl.ErrorImpl;

public interface IErrors extends Serializable {

    public abstract List<IError> getErrors();

    public abstract void addError(IError error);

    public abstract void addError(IErrors errors);

    public abstract Boolean hasErrors();

    public Timestamp getErrorTimeStamp();

	public abstract void setErrorEvent(IAppEvent errorEvent);

	public abstract void addError(ErrorImpl errorImpl);

	public abstract void setErrors(List<IError> errors);

	boolean isEmpty();

	IAppEvent getErrorEvent();

	/**
	 * @return
	 * 
	 */
	Timestamp getStartTime();

	void setErrorCount(Long errorCount);

	void setIPAddress(String address);

	String getIPAddress();

	Long getErrorCount();

	/**
	 * @param userName
	 */
	void setUserName(String userName);

	/**
	 * @param startTime
	 */
	void setStartTime(Timestamp startTime);

	/**
	 * @return
	 */
	String getUserName();
}
