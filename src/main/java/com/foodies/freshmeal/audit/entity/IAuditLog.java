package com.foodies.freshmeal.audit.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

public interface IAuditLog extends Serializable  {
	
	public String getId();

    public String getApi();
	
    public String getMethod();

    public String getRequestBody();

    public String getResponseBody();

    public Integer getResponseStatus();

    public Long getExecutionTimeMs();

    public String getIpAddress();

    public LocalDateTime getCreatedAt();
	
	
}
