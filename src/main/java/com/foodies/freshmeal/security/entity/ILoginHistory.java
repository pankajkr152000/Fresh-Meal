package com.foodies.freshmeal.security.entity;

import java.io.Serializable;
import java.sql.Timestamp;

public interface ILoginHistory extends Serializable {
	
	String getIpAddress();
	
	long getLoginHistoryId();

	String getLoginStatus();

	String getLoginSuccessFlag();

	Timestamp getLoginTime();

	Timestamp getLogoutTime();

	String getSessionId();

	long getUsrUserId();

	String getLoginServerName();

	String setIpAddress(String ipAddress);

	String setLoginStatus(String loginStatus);

	String setLoginSuccessFlag(String loginSuccessFlag);

	Timestamp setLoginTime(Timestamp loginTime);

	Timestamp setLogoutTime(Timestamp logoutTime);

	String setSessionId(String sessionId);

	long setUsrUserId(long usrUserId);
	
	String setLoginServerName(String loginServerName);

	
}
