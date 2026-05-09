package com.foodies.freshmeal.exception.impl;

import java.sql.Timestamp;
import java.util.List;

import com.foodies.freshmeal.common.IAppEvent;
import com.foodies.freshmeal.exception.IError;
import com.foodies.freshmeal.exception.IErrors;

public class ErrorsImpl implements IErrors {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8673200077208816921L;

	@Override
	public List<IError> getErrors() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addError(IError error) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addError(IErrors errors) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Boolean hasErrors() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Timestamp getErrorTimeStamp() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setErrorEvent(IAppEvent errorEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addError(ErrorImpl errorImpl) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setErrors(List<IError> errors) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IAppEvent getErrorEvent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Timestamp getStartTime() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setErrorCount(Long errorCount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setIPAddress(String address) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getIPAddress() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long getErrorCount() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setUserName(String userName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setStartTime(Timestamp startTime) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getUserName() {
		// TODO Auto-generated method stub
		return null;
	}

	

}
