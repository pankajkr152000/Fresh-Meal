package com.foodies.freshmeal.user.entity;


import java.util.List;

import com.foodies.freshmeal.common.entity.IEntity;



public interface IUserEntity extends IEntity {
    
    String getId();

    void setId(String id);

    String getUsername();
    
    String getPassword();

    String getEmail();

    String getFirstName();

    String getLastName();

    String getPhoneNumber();

    List<String> getAddress();

    List<String> getRoles();

    boolean isAccountNonExpired();

    boolean isAccountNonLocked();

    boolean isCredentialsNonExpired();

    boolean isEnabled();

    void setUsername(String username);

    void setPassword(String password);

    void setEmail(String email);

    void setFirstName(String firstName);

    void setLastName(String lastName);

    void setAccountNonExpired(boolean accountNonExpired);

    void setAccountNonLocked(boolean accountNonLocked);

    void setCredentialsNonExpired(boolean credentialsNonExpired);

    void setEnabled(boolean enabled);

    void setPhoneNumber(String phoneNumber);

}
