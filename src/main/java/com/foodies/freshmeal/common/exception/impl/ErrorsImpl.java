package com.foodies.freshmeal.common.exception.impl;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.foodies.freshmeal.common.IAppEvent;
import com.foodies.freshmeal.common.exception.IError;
import com.foodies.freshmeal.common.exception.IErrors;

import lombok.NoArgsConstructor;


@NoArgsConstructor
public class ErrorsImpl implements IErrors {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8673200077208816921L;

    private String userName;

	private String IPAddress;

	private Long errorCount;

	private Timestamp startTime;

	List<IError> errors = new LinkedList<>();

	private final Map<String, Object> attributeMap = new HashMap<>();
	
	private IAppEvent errorEvent;
     
	public ErrorsImpl(List<IError> errors) {
		this.errors = errors;
	}


	@Override
	public List<IError> getErrors() {
		return errors;
	}

	@Override
	public void addError(IError error) {
		errors.add(error);
	}

	@Override
	public void addError(IErrors errors) {
		this.errors.addAll(errors.getErrors());		
	}

	@Override
	public void addError(ErrorImpl errorImpl) {
		this.errors.add(errorImpl);
	}

	@Override
	public Boolean hasErrors() {
		return !errors.isEmpty();
	}

	@Override
	public Timestamp getErrorTimeStamp() {
		return new Timestamp(System.currentTimeMillis());
	}

	@Override
	public void setErrorEvent(IAppEvent errorEvent) {
		this.errorEvent = errorEvent;
		
	}

	public final List<IError> getErrorsList() {
		return errors;
	}

	public void addErrorList(List<IError> errorList) {
		this.errors.addAll(errorList);
	}

	@Override
	public void setErrors(List<IError> errors) {
		this.errors = errors;
		
	}

	@Override
	public boolean isEmpty() {
		return errors.isEmpty();
	}

	@Override
	public IAppEvent getErrorEvent() {
		return errorEvent;
	}

	@Override
	public Timestamp getStartTime() {
		return startTime;
	}

	@Override
	public void setErrorCount(Long errorCount) {
		this.errorCount = errorCount;
		
	}

	@Override
	public void setIPAddress(String address) {
		this.IPAddress = address;
		
	}

	@Override
	public String getIPAddress() {
		return IPAddress;
	}

	@Override
	public Long getErrorCount() {
		return errorCount;
	}

	@Override
	public void setUserName(String userName) {
		this.userName = userName;	
	}

	@Override
	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
		
	}

	@Override
	public String getUserName() {
		return userName;
	}

	public Map<String, Object> getAttributeMap() {
		return attributeMap;
	}

	public Object getAttribute(String attributeName) {
		return attributeMap.get(attributeName);
	}

	public void setAttribute(String attributeName, Object attributeValue) {
		attributeMap.put(attributeName, attributeValue);
	}

}
