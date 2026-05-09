package com.foodies.freshmeal.common.entity;


import java.util.List;



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

    boolean isDeleted();

    String createdBy();

    String getDeletedAt();

    String getCreatedAt();

    void setUsername(String username);

    void setPassword(String password);

    void setEmail(String email);

    void setFirstName(String firstName);

    void setLastName(String lastName);

    void setAccountNonExpired(boolean accountNonExpired);

    void setAccountNonLocked(boolean accountNonLocked);

    void setCredentialsNonExpired(boolean credentialsNonExpired);

    void setEnabled(boolean enabled);

    void setDeleted(boolean deleted);

    void setCreatedBy(String createdBy);

    void setPhoneNumber(String phoneNumber);

    void setDeletedAt(String deletedAt);

    void setCreatedAt(String createdAt);


}
