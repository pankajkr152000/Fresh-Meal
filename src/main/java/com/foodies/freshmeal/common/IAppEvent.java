package com.foodies.freshmeal.common;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

public interface IAppEvent extends Serializable {
    public String getEventType();

    public Date getEventEffectivDate();

    public String getEventReferenceNo();

    public String getSubEvent();

    public Map<String,Object> getEventContextMap();

    public void addEventData(String paramName, Object object);    

    
}
