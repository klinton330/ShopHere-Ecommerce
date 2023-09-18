package com.shophere.admin.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shophere.admin.Repository.CatagoryRepository;
import com.shophere.admin.Repository.UserRepository;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.User;

@Service
public class DashboardService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CatagoryRepository catagoryRepository;

	public Long getUserCount() {
		return userRepository.count();
	}

	public Integer getEnabledUserCount() {
		Integer enableUser = 0;
		List<User> listOfUser = userRepository.findAll();
		for (User user : listOfUser) {
			if (user.isEnabled())
				enableUser++;
		}
		return enableUser;
	}

	public Integer getDisabledUserCount() {
		Integer disableUser = 0;
		List<User> listOfUser = userRepository.findAll();
		for (User user : listOfUser) {
			if (!user.isEnabled())
				disableUser++;
		}
		return disableUser;
	}

	public Integer getEnabledCategory() {
		Integer enabledCategory = 0;
		List<Category> listOfCategory = catagoryRepository.findAll();
		for (Category category : listOfCategory) {
			if (category.isEnabled())
				enabledCategory++;
		}
		return enabledCategory;
	}
	
	public Integer getDisabledCategory() {
		Integer disabledCategory = 0;
		List<Category> listOfCategory = catagoryRepository.findAll();
		for (Category category : listOfCategory) {
			if (!category.isEnabled())
				disabledCategory++;
		}
		return disabledCategory;
	}

	public Long getCategoryCount() {
		return catagoryRepository.count();
	}

}
