package com.foodies.freshmeal.common.audit.entity;

import java.time.LocalDateTime;

import com.foodies.freshmeal.common.entity.IEntity;

public interface IAuditLog extends IEntity  {
	
	public String getId();

    public String getApi();
	
    public String getMethod();

    public String getRequestBody();

    public String getResponseBody();

    public Integer getResponseStatus();

    public Long getExecutionTimeMs();

    public String getIpAddress();

    public LocalDateTime getCreatedAt();

    public void setId(String id);

    public void setApi(String api);

    public void setMethod(String method);

    public void setRequestBody(String requestBody);

    public void setResponseBody(String responseBody);

    public void setResponseStatus(Integer responseStatus);

    public void setExecutionTimeMs(Long executionTimeMs);

    public void setIpAddress(String ipAddress);

    public void setCreatedAt(LocalDateTime createdAt);
	
	
}
