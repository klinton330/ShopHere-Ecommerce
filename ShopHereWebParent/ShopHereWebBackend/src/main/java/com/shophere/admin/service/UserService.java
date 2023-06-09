package com.shophere.admin.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shophere.admin.Repository.UserRepository;
import com.shopme.common.entity.User;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	public List<User>listAll()
	{
		return (List<User>) userRepository.findAll();
	}

}
