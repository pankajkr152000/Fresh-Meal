package com.foodies.freshmeal.common.exception;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import com.foodies.freshmeal.common.IAppEvent;
import com.foodies.freshmeal.common.exception.impl.ErrorImpl;

public interface IErrors extends Serializable {

    public abstract List<IError> getErrors();

    public abstract void addError(IError error);

    public abstract void addError(IErrors errors);

    public abstract Boolean hasErrors();

    public LocalDateTime getErrorLocalDateTime();

	public abstract void setErrorEvent(IAppEvent errorEvent);

	public abstract void addError(ErrorImpl errorImpl);

	public abstract void setErrors(List<IError> errors);

	boolean isEmpty();

	IAppEvent getErrorEvent();

	/**
	 * @return
	 * 
	 */
	LocalDateTime getStartTime();

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
	void setStartTime(LocalDateTime startTime);

	/**
	 * @return
	 */
	String getUserName();
}
