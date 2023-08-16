package com.shophere.admin.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.shophere.admin.service.CategoryService;
import com.shopme.common.entity.Category;

@Controller
public class CategoryController {
	private static final Logger LOGGER=LoggerFactory.getLogger(CategoryController.class);
	@Autowired
	private CategoryService categoryService;
	
	@GetMapping("categories")
	public String listAllCategories(Model model)
	{
		LOGGER.info("/Categories");
		List<Category>listOfAllCategory=categoryService.listAll();
		model.addAttribute("listCategories",listOfAllCategory);
		LOGGER.info("Total Length of Category:"+listOfAllCategory.size());
		return"category/categories";
	}

}
