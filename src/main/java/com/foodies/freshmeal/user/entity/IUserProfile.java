package com.foodies.freshmeal.user.entity;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.userdetails.UserDetails;

import com.foodies.freshmeal.common.entity.IEntity;
import com.foodies.freshmeal.common.entity.ILoginHistory;

public interface IUserProfile extends IEntity, Authentication, CredentialsContainer, UserDetails {
	
	public String getId();

	public ILoginHistory getLoginHistory();
	
	public IUserEntity getUserEntity();

	public void setId(String id);
	
	public void setLoginHistory(ILoginHistory loginHistory);

	public void setUserEntity(IUserEntity userEntity);

}
