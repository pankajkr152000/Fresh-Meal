package com.foodies.freshmeal.user.entity.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.foodies.freshmeal.common.entity.IEntity;
import com.foodies.freshmeal.common.entity.IUserEntity;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Document(collection = "users")
public class UserEntity implements IUserEntity {

    /**
	 *
	 */
	private static final long serialVersionUID = 4377183688839034088L;

	/**
	 * =====================================================
	 * Constructor
	 * =====================================================
	 *
	 * Package-private constructor.
	 * Entity creation should happen only through factory.
	 *
	 * =====================================================
	 */
	UserEntity() {

	}

	/**
	 * =====================================================
	 * Factory Method
	 * =====================================================
	 */
	public static IEntity create() {
		return new UserEntity();
	}

	/**
	 * =====================================================
	 * Mongo Primary Key
	 * =====================================================
	 */
	@Id
	private String id;
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private List<String> address;
    private List<String> roles;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;
    private boolean deleted;
    private String createdBy;
    private String createdAt;
    private String updatedBy;
    private String updatedAt;
    private String deletedAt;


    @Override
    public String getId() {
        return id;
    }
    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getEmail() {
        return this.email;
    }

    @Override
    public String getFirstName() {
        return this.firstName;
    }

    @Override
    public String getLastName() {
        return this.lastName;
    }

    @Override
    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    @Override
    public List<String> getAddress() {
    	if(address == null) {
    		return new ArrayList<>();
    	}
        return this.address;
    }

    public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	@Override
    public List<String> getRoles() {
        if(roles == null) {
            return new ArrayList<>();
        }
        return this.roles;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public boolean isDeleted() {
        return this.deleted;
    }

    @Override
    public String createdBy() {
        return this.createdBy;
    }

    @Override
    public void  setUsername(String username) {
        this.username = username;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    @Override
    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    @Override
    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public void setDeletedAt(String deletedAt) {
        this.deletedAt = deletedAt;
    }

    @Override
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

	@Override
	public String getDeletedAt() {
		return this.deletedAt;
	}

	@Override
	public String getCreatedAt() {
		return this.createdAt;
	}

	@Override
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
		
	}

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

	

}
