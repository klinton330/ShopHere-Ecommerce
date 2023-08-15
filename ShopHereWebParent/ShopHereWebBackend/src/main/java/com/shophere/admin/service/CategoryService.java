package com.shophere.admin.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shophere.admin.Repository.CatagoryRepository;
import com.shopme.common.entity.Category;

@Service
public class CategoryService {
	@Autowired
	private CatagoryRepository catagoryRepository;

	public List<Category> listAll() {
		return catagoryRepository.findAll();
	}

}
