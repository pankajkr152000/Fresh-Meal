package com.foodies.freshmeal.common.entity;

import java.sql.Timestamp;

public interface ILoginHistory extends IEntity {

	String getIpAddress();

	long getLoginHistoryId();

	String getLoginStatus();

	String getLoginSuccessFlag();

	Timestamp getLoginTime();

	Timestamp getLogoutTime();

	String getSessionId();

	long getUsrUserId();

	String getLoginServerName();

	void setIpAddress(String ipAddress);

	void setLoginStatus(String loginStatus);

	void setLoginSuccessFlag(String loginSuccessFlag);

	void setLoginTime(Timestamp loginTime);

	void setLogoutTime(Timestamp logoutTime);

	void setSessionId(String sessionId);

	void setUsrUserId(long usrUserId);

	void setLoginServerName(String loginServerName);

}
