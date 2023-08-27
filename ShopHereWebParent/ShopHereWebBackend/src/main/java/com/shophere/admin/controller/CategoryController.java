package com.shophere.admin.controller;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shophere.admin.exception.CategoryNotFoundException;
import com.shophere.admin.service.CategoryService;
import com.shophere.admin.utils.FileUploadUtil;
import com.shopme.common.entity.Category;

@Controller
public class CategoryController {
	private static final Logger LOGGER = LoggerFactory.getLogger(CategoryController.class);
	@Autowired
	private CategoryService categoryService;

	@GetMapping("categories")
	public String listAllCategories(Model model) {
		LOGGER.info("/Categories");
		List<Category> listOfAllCategory = categoryService.listAll();
		model.addAttribute("listCategories", listOfAllCategory);
		LOGGER.info("Total Length of Category:" + listOfAllCategory.size());
		return "category/categories";
	}

	@GetMapping("/categories/new")
	public String newCategory(Model model) {
		LOGGER.info("/categories/new");
		List<Category> listCategories = categoryService.listCategoriesUsedInForm();
		// listCategories.forEach(x->System.out.println(x.getName()));
		model.addAttribute("category", new Category());
		model.addAttribute("listCategoriesForForm", listCategories);
		model.addAttribute("pageTitle", "Create New Category");
		return "category/category_form";
	}

	@PostMapping("/categories/save")
	public String saveCategory(Category category, @RequestParam("fileImage") MultipartFile multipartFile,
			RedirectAttributes redirect) throws IOException {
		LOGGER.info("/categories/save");
		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			LOGGER.info("After CleanPath:" + fileName);
			category.setImage(fileName);
			Category savedCategory = categoryService.save(category);
			LOGGER.info("IF-Category Name:" + savedCategory.getName() + " " + "Category Image:" + savedCategory.getImage()
					+ " " + "Parent Name:" + savedCategory.getParent().getName());
			String uploadCategoryDir = "../category-images/" + savedCategory.getId();
			FileUploadUtil.saveFile(uploadCategoryDir, fileName, multipartFile);
		} else {
			if (category.getImage().isEmpty())
				category.setImage(null);
			Category savedCategory = categoryService.save(category);
			LOGGER.info("ELSE-Category Name:" + savedCategory.getName() + " " + "Category Image:" + savedCategory.getImage()
					+ " " + "Parent Name:" + savedCategory.getParent().getName());
		}
		boolean isNewCategory = false;
		if (category.getId() == null)
			isNewCategory = true;

		if (isNewCategory)
			redirect.addFlashAttribute("message", "Category has been saved Successfully");
		else
			redirect.addFlashAttribute("message", "Category has been Updated Successfully");

		return "redirect:/categories";

	}

	@GetMapping("/categories/edit/{id}")
	public String updateCategory(@PathVariable Integer id, Model model, RedirectAttributes redirect) {
		LOGGER.info("/categories/edit/" + id);
		try {
			Category categoryFromDB = categoryService.findCategoryById(id);
			List<Category> listCategories = categoryService.listCategoriesUsedInForm();
			model.addAttribute("category", categoryFromDB);
			model.addAttribute("listCategoriesForForm", listCategories);
		} catch (CategoryNotFoundException e) {
			LOGGER.error("Exception Occured:" + e.getMessage());
			redirect.addAttribute("message", e.getMessage());
			return "redirect:/categories";
		}
		return "category/category_form";
	}
}
