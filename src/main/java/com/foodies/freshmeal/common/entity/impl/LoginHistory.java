package com.foodies.freshmeal.common.entity.impl;

import java.sql.Timestamp;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.foodies.freshmeal.common.entity.IEntity;
import com.foodies.freshmeal.common.entity.ILoginHistory;

@Document(collection = "fm_login_history")
public class LoginHistory implements ILoginHistory {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2782146075825494749L;

	@Id
	String id;
	String ipAddress;
	long loginHistoryId;
	String loginStatus;
	String loginSuccessFlag;
	Timestamp loginTime;
	Timestamp logoutTime;
	String sessionId;
	long usrUserId;
	String loginServerName;

	public LoginHistory() {

	}
	
	public static IEntity create() {
		return new LoginHistory();
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}	

	@Override
	public String getIpAddress() {
		return this.ipAddress;
	}

	@Override
	public long getLoginHistoryId() {
		return this.loginHistoryId;
	}

	@Override
	public String getLoginStatus() {
		return this.loginStatus;
	}

	@Override
	public String getLoginSuccessFlag() {
		return this.loginSuccessFlag;
	}

	@Override
	public Timestamp getLoginTime() {
		return this.loginTime;
	}

	@Override
	public Timestamp getLogoutTime() {
		return this.logoutTime;
	}

	@Override
	public String getSessionId() {
		return this.sessionId;
	}

	@Override
	public long getUsrUserId() {
		return this.usrUserId;
	}

	@Override
	public String getLoginServerName() {
		return this.loginServerName;
	}

	@Override
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	@Override
	public void setLoginStatus(String loginStatus) {
		this.loginStatus = loginStatus;
	}

	@Override
	public void setLoginSuccessFlag(String loginSuccessFlag) {
		this.loginSuccessFlag = loginSuccessFlag;
	}

	@Override
	public void setLoginTime(Timestamp loginTime) {
		this.loginTime = loginTime;
	}

	@Override
	public void setLogoutTime(Timestamp logoutTime) {
		this.logoutTime = logoutTime;
	}

	@Override
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	@Override
	public void setUsrUserId(long usrUserId) {
		this.usrUserId = usrUserId;
	}

	@Override
	public void setLoginServerName(String loginServerName) {
		this.loginServerName = loginServerName;
	}

}
