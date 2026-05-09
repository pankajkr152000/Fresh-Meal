package com.foodies.freshmeal.exception;

public class SystemConfigurationException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1366117106348972578L;

	public SystemConfigurationException () {
		
	}
	
	public SystemConfigurationException(String message) {
		super(message);
	}
	
	public SystemConfigurationException(Throwable ex) {
		super(ex);
	}
	
}
