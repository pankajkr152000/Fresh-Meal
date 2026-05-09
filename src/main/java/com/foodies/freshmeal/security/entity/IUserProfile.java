package com.foodies.freshmeal.security.entity;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.userdetails.UserDetails;

public interface IUserProfile extends Authentication, CredentialsContainer, UserDetails {
	
	public ILoginHistory getLoginHistory();
	
	public IUserEntity getUserEntity();
	
	public void setLoginHistory(ILoginHistory loginHistory);

	public void setUserEntity(IUserEntity userEntity);


}
