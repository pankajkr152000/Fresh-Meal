package com.foodies.freshmeal.common.exception;

import java.io.Serializable;
import java.sql.Timestamp;

public interface IError extends Serializable {

	public static final int INFO = 1;

	public static final int WARNING = 2;

	public static final int ERROR = 3;

	public static final int FATAL = 4;

	public static final int STOP = 5;


	
	public abstract int getSeverity();

	public abstract String getErrorCode();

	public abstract String getErrorMessage();

	public abstract String getErrorDetails();

	public abstract StackTraceElement[] getErrorStackTrace();

	public Timestamp getErrorTimeStamp();

	public String getFixedMessage();

	public Boolean getErrorFlag();

	public Long getId();

	public String getUserName();

	public String getAddress();

}
