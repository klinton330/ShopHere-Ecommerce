package com.shophere.admin.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.shophere.admin.controller.UserController;
import com.shophere.admin.utils.CurrentDate;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

public class ShopHereUserDetails implements UserDetails {

	/**
	 * 
	 */
	private static final Logger LOGGER=LoggerFactory.getLogger(ShopHereUserDetails.class);
	private static final long serialVersionUID = 1L;
	private User user;

	public ShopHereUserDetails(User user) {
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<Role> roles = user.getRoles();
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		for (Role role : roles) {
			authorities.add(new SimpleGrantedAuthority(role.getName()));
		}
		return authorities;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return user.isEnabled();
	}
	
	public String getFullname() {
		return this.user.getFirstName()+" "+this.user.getLastname();
	}
	
	public void setFirstName(String firstName)
	{
		this.user.setFirstName(firstName);
	}
	public void setLastName(String lastName)
	{
		this.user.setLastname(lastName);
	}
	
	public String getCurrentDate()
	{
		return CurrentDate.getCurrentDate();
	}

}
