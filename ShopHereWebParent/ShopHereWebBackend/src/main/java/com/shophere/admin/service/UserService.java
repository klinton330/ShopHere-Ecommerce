package com.shophere.admin.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shophere.admin.Repository.RoleRepository;
import com.shophere.admin.Repository.UserRepository;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public List<User>listAll()
	{
		return (List<User>) userRepository.findAll();
	}
	
	public List<Role>listRoles()
	{
		return (List<Role>) roleRepository.findAll();
	}
	
	public void saveUser(User user)
	{
		encodePassword(user);
		userRepository.save(user);
	}
	
	private void encodePassword(User user)
	{
		String encodePassword=passwordEncoder.encode(user.getPassword());
		user.setPassword(encodePassword);
	}

	
	public boolean isEmailUnique(String email)
	{
		User userByEmail=userRepository.getUserByEmail(email);
		return userByEmail==null;
	}
}
