package com.foodies.freshmeal.common.exception;

import java.io.Serializable;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.util.StringUtils;

import com.foodies.freshmeal.common.IAppEvent;
import com.foodies.freshmeal.common.exception.impl.ErrorImpl;
import com.foodies.freshmeal.common.exception.impl.ErrorsImpl;
import com.foodies.freshmeal.common.exception.util.DataExceptionUtils;


public class ExceptionCollection extends Exception implements  IErrors {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3227764714325131890L;
	IErrors	errorsDelegate;
	
	private Set<String> bypassErrorCode = null;
	
	public final static List<IError> errorList = new ArrayList<>();
	
	/**
	 * 
	 * @param errors
	 */
	public ExceptionCollection(IErrors errors) {

		this.errorsDelegate = errors;
	}

	public ExceptionCollection() {

		this.errorsDelegate = new ErrorsImpl(errorList);
	}
	
	public ExceptionCollection(String RuleCd, Map<String, Serializable> objectMap, String... concessionCode) {

		if (StringUtils.hasText(RuleCd) && objectMap != null && !objectMap.isEmpty() && concessionCode != null
				&& concessionCode.length != 0) {

			for (String code : concessionCode) {

				if(!StringUtils.hasText(code))
					continue;
				
				Set<String> errorSet = DataExceptionUtils.getBypassErrorByConcessionCd(RuleCd, objectMap, code);

				if (errorSet != null && !errorSet.isEmpty()) {

					if (bypassErrorCode == null)
						bypassErrorCode = new HashSet<>();

					bypassErrorCode.addAll(errorSet);
				}
			}
		}
		this.errorsDelegate = new ErrorsImpl(errorList);
	}

	public ExceptionCollection(ParseException e) {
		this.errorsDelegate = new ErrorsImpl(errorList);
		this.errorsDelegate.addError(new ErrorImpl(e));
	}

	/**
	 * @param error
	 * @see com.tcs.iims.exception.core.IErrors#addError(com.tcs.iims.exception.core.IError)
	 */
	@Override
	public void addError(IError error) {

		this.errorsDelegate.addError(error);
	}

	/**
	 * @param errors
	 * @see com.tcs.iims.exception.core.IErrors#addError(com.tcs.iims.exception.core.IErrors)
	 */
	@Override
	public void addError(IErrors errors) {

		this.errorsDelegate.addError(errors);
	}
	
	public void addThrowable(Exception th) {
		if(th instanceof ExceptionCollection){
			this.errorsDelegate.addError((IErrors)th);
		}else{
			this.errorsDelegate.addError(new ErrorImpl(th));
		}
	}

	/**
	 * @return
	 * @see com.tcs.iims.exception.core.IErrors#getErrors()
	 */
	@Override
	public List<IError> getErrors() {

		return this.errorsDelegate.getErrors();
	}

	/**
	 * @param errors
	 * @see com.tcs.iims.exception.core.IErrors#setErrors(java.util.List)
	 */
	@Override
	public void setErrors(List<IError> errors) {

		this.errorsDelegate.setErrors(errors);
	}

	/**
	 * @return the errorsDelegate
	 */
	public final IErrors getErrorsDelegate() {

		return this.errorsDelegate;
	}

	/**
	 * @param errorsDelegate
	 *            the errorsDelegate to set
	 */
	public final void setErrorsDelegate(IErrors errorsDelegate) {

		this.errorsDelegate = errorsDelegate;
	}

	/**
	 * @return
	 * @see com.tcs.iims.exception.core.IErrors#getStartTime()
	 */
	@Override
	public LocalDateTime getStartTime() {

		return this.errorsDelegate.getStartTime();
	}

	/**
	 * @return
	 * @see com.tcs.iims.exception.core.IErrors#getUserName()
	 */
	@Override
	public String getUserName() {

		return this.errorsDelegate.getUserName();
	}

	/**
	 * @param startTime
	 * @see com.tcs.iims.exception.core.IErrors#setStartTime(java.lang.String)
	 */
	@Override
	public void setStartTime(LocalDateTime startTime) {

		this.errorsDelegate.setStartTime(startTime);
	}

	/**
	 * @param userName
	 * @see com.tcs.iims.exception.core.IErrors#setUserName(java.lang.String)
	 */
	@Override
	public void setUserName(String userName) {

		this.errorsDelegate.setUserName(userName);
	}

	

	@Override
	public Long getErrorCount() {

		return this.errorsDelegate.getErrorCount();
	}

	@Override
	public String getIPAddress() {

		return this.errorsDelegate.getIPAddress();
	}

	

	@Override
	public void setErrorCount(Long errorCount) {

		this.errorsDelegate.setErrorCount(errorCount);
		
	}

	@Override
	public void setIPAddress(String address) {

		this.errorsDelegate.setIPAddress(address);
		
	}

	

	@Override
	public boolean isEmpty() {
		return this.errorsDelegate.isEmpty();
	}

	
	@Override
	public IAppEvent getErrorEvent() {
		return this.errorsDelegate.getErrorEvent();
	}

	@Override
	public void setErrorEvent(IAppEvent errorEvent) {
		this.errorsDelegate.setErrorEvent(errorEvent);
	}
	
	/**
	 * This method is used for only those error which are not a bypass error
	 * 
	 * @param error
	 * @throws ExceptionCollection 
	 */
	public void addAndThrowError(IError error) throws ExceptionCollection {

		if (bypassErrorCode == null || !bypassErrorCode.contains(error.getErrorCode())) {

			this.addError(error);
			throw this;
		}
	}


	@Override
	public Boolean hasErrors() {
		return this.errorsDelegate.hasErrors();
	}

	@Override
	public LocalDateTime getErrorLocalDateTime() {
		return this.errorsDelegate.getErrorLocalDateTime();
	}

	@Override
	public void addError(ErrorImpl errorImpl) {
		this.errorsDelegate.addError(errorImpl);
		
	}

}
