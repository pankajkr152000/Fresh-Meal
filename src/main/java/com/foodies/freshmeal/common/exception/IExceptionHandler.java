package com.foodies.freshmeal.common.exception;

public interface IExceptionHandler {
    /**
     * This method is to be called once prior to exception handing process
     */
    public void setUp();
    
    /**
     * @param message
     * @return true if this handler can process the exception
     */
    public boolean canHandleException(IError message);
    /**
     * The callback to process an exception
     * 
     * @param message 
     */
    public void doHandleException(IError message);
    /**
     * This method is to be called once after all the exception handing finishes
     */
    public void finish(IErrors messages)throws Exception;
    /**
     * 
     * @return 
     */
    public String getCode();
    /**
     * 
     * @param code 
     */
    public void setCode(String code); 
    /**
     * 
     * 
     * @param exceptionSiteKey
     */
    public void setExceptionSiteInTL(String exceptionSiteKey);
    /**
     * This method is to remove exception site from the error thread local object
     */
    public void removeExceptionSiteInTL();
    
    public void finishBusinessException(IErrors messages);
}
