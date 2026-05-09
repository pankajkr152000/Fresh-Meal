package com.foodies.freshmeal.ruleContext;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

public interface IRuleContext extends Serializable {
    
    public Object get(String key);
    
    public void set(String key, Object value);

    public Object remove(String theName);

    public void setContextAttributes(String[] contextAttributes);

    public void setRunDate(Date runDate);

    public Map<String,Serializable> getParameters();

    public boolean contains(String name);

    public Collection<String> getOutputParameters();

    

}
