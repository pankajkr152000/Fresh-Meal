package com.foodies.freshmeal.common.entity;

import java.time.LocalDateTime;

public interface ILoginHistory extends IEntity {

	String getIpAddress();

	long getLoginHistoryId();

	String getLoginStatus();

	String getLoginSuccessFlag();

	LocalDateTime getLoginTime();

	LocalDateTime getLogoutTime();

	String getSessionId();

	long getUsrUserId();

	String getLoginServerName();

	void setIpAddress(String ipAddress);

	void setLoginStatus(String loginStatus);

	void setLoginSuccessFlag(String loginSuccessFlag);

	void setLoginTime(LocalDateTime loginTime);

	void setLogoutTime(LocalDateTime logoutTime);

	void setSessionId(String sessionId);

	void setUsrUserId(long usrUserId);

	void setLoginServerName(String loginServerName);

}
