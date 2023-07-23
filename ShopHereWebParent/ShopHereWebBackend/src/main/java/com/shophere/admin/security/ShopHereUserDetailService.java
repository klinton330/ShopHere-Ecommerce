package com.shophere.admin.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.shophere.admin.Repository.UserRepository;
import com.shopme.common.entity.User;

public class ShopHereUserDetailService implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user=userRepository.getUserByEmail(username);
		if(user!=null)
		{
			return new ShopHereUserDetails(user);
		}
		throw new UsernameNotFoundException("Could not find user with email:"+username);
	}

}
