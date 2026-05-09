package com.foodies.freshmeal.common.io;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

public interface IDataContext extends Serializable {
	public Object getAttribute(String name);
	
	public boolean hasAttribute(String name);
	
	public void setAttribute(String name, Object value);
	
	public Map<String,Object> getAllAttributes();
	
	public void setAttributes(Map<String,Object> attributes);
	
	public Date getAsOfBusinessDate();
	
	public void setAsOfBusinessDate(Date asOfBusinessDate);
	
}
