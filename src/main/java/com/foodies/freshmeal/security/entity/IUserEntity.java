package com.foodies.freshmeal.security.entity;

import java.io.Serializable;
import java.util.List;

public interface IUserEntity extends Serializable {
    
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

    boolean isDeleted();

    String createdBy();

    String setUsername(String username);

    String setPassword(String password);

    String setEmail(String email);

    String setFirstName(String firstName);

    String setLastName(String lastName);

    boolean setAccountNonExpired(boolean accountNonExpired);

    boolean setAccountNonLocked(boolean accountNonLocked);

    boolean setCredentialsNonExpired(boolean credentialsNonExpired);

    boolean setEnabled(boolean enabled);

    boolean setDeleted(boolean deleted);

    String setCreatedBy(String createdBy);



}
