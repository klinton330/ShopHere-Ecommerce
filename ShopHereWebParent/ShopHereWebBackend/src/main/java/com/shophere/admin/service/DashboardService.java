package com.shophere.admin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shophere.admin.Repository.CatagoryRepository;
import com.shophere.admin.Repository.UserRepository;

@Service
public class DashboardService {

	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CatagoryRepository catagoryRepository;
	
	public Long getUserCount()
	{
		return userRepository.count();
	}
	
	public Long getCategoryCount()
	{
		return catagoryRepository.count();
	}
}
