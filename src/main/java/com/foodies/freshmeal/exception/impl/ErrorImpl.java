package com.foodies.freshmeal.exception.impl;

import java.sql.Timestamp;

import com.foodies.freshmeal.exception.IError;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ErrorImpl implements IError {

    private static final long serialVersionUID = 1L;

    private int severity = ERROR;

    private String errorCode;

    private String errorMessage;

    private String errorDetails;

    private StackTraceElement[] errorStackTrace;

    private Timestamp errorTimeStamp = new Timestamp(System.currentTimeMillis());

    private String fixedMessage;

    private Boolean errorFlag = true;

    private Long id;

    private String userName;

    private String address;

    private String exceptionClass;

    /**
	 * @return the exceptionClass
	 */
	public String getExceptionClass() {
		return exceptionClass;
	}

	/**
	 * @param exceptionClass the exceptionClass to set
	 */
	public void setExceptionClass(String exceptionClass) {
		this.exceptionClass = exceptionClass;
	}

	public ErrorImpl(Exception th) {
        this.errorMessage = th.getMessage();
        this.exceptionClass = th.getClass().getName();
        this.errorStackTrace = th.getStackTrace();
        this.errorDetails = th.toString();
    }

    @Override
    public int getSeverity() {
        return severity;
    }

    @Override
    public String getErrorCode() {
        return errorCode;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public String getErrorDetails() {
        return errorDetails;
    }

    @Override
    public StackTraceElement[] getErrorStackTrace() {
        return errorStackTrace;
    }

    @Override
    public Timestamp getErrorTimeStamp() {
        return errorTimeStamp;
    }

    @Override
    public String getFixedMessage() {
        return fixedMessage;
    }

    @Override
    public Boolean getErrorFlag() {
        return errorFlag;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public String getAddress() {
        return address;
    }
}