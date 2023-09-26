package com.shophere.admin.controller;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.method.P;
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
import com.shophere.admin.export.CategoryCSVExporter;
import com.shophere.admin.service.CategoryService;
import com.shophere.admin.utils.FileUploadUtil;
import com.shopme.common.entity.Category;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class CategoryController {
	private static final Logger LOGGER = LoggerFactory.getLogger(CategoryController.class);
	@Autowired
	private CategoryService categoryService;

	// Get Mapping controller
	@GetMapping("categories")
	public String listAllCategories(@Param("sortDir") String sortDir, Model model) {
		return listByPage(1, sortDir, null, model);
	}

	@GetMapping("/categories/page/{pageNum}")
	public String listByPage(@PathVariable(name = "pageNum") int pageNum, @Param("sortDir") String sortDir,
			@Param("keyword") String keyword, Model model) {
		if (sortDir == null || sortDir.isEmpty())
			sortDir = "asc";
		LOGGER.info("GET:/Categories:Sorting Direction:" + sortDir);
		CategoryPageInfo pageInfo = new CategoryPageInfo();
		List<Category> listOfAllCategory = categoryService.listAll(pageInfo, pageNum, sortDir, keyword);
		long startCount = (pageNum - 1) * categoryService.ROOT_CATEGORIES_PER_PAGE + 1; // 0,5
		long endCount = startCount + categoryService.ROOT_CATEGORIES_PER_PAGE - 1;// 4,9no
		if (endCount > pageInfo.getTotalElements()) {
			endCount = pageInfo.getTotalElements();
		}
		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		model.addAttribute("totalPage", pageInfo.getTotalPages());
		model.addAttribute("totalItems", pageInfo.getTotalElements());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("listCategories", listOfAllCategory);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("SortField", "name");
		model.addAttribute("SortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword", keyword);
		LOGGER.info("Total Pages:" + pageInfo.getTotalPages());
		LOGGER.info("Current Page:" + pageNum);
		LOGGER.info("Total Categories in eachPage:" + pageInfo.getTotalElements());
		LOGGER.info("Total Length of Category:" + listOfAllCategory.size());
		LOGGER.info("Search Keyword:" + keyword);
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
		Category savedCategory;
		boolean isNewCategory = false;

		if (category.getId() == null) {
			isNewCategory = true;
		}
		LOGGER.info("/categories/save");
		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			LOGGER.info("After CleanPath:" + fileName);
			category.setImage(fileName);
			savedCategory = categoryService.save(category);
			String uploadCategoryDir = "../category-images/" + savedCategory.getId();
			FileUploadUtil.saveFile(uploadCategoryDir, fileName, multipartFile);
		} else {
			if (category.getImage().isEmpty())
				category.setImage(null);
			savedCategory = categoryService.save(category);
		}
		try {
			LOGGER.info("IF-Category Name:" + savedCategory.getName() + " " + "Category Image:"
					+ savedCategory.getImage() + " " + "Parent Name:" + savedCategory.getParent().getName());
		} catch (Exception ex) {
			LOGGER.info("IF-Category Name:" + savedCategory.getName() + " " + "Category Id:" + savedCategory.getId()
					+ "Category Image:" + savedCategory.getImage() + " " + "Parent Name:No Parent");
		}

		LOGGER.info("Adding Type:" + isNewCategory);
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
			model.addAttribute("pageTitle", "Edit Category:" + id);
			model.addAttribute("category", categoryFromDB);
			model.addAttribute("listCategoriesForForm", listCategories);
		} catch (CategoryNotFoundException e) {
			LOGGER.error("Exception Occured:" + e.getMessage());
			redirect.addAttribute("message", e.getMessage());
			return "redirect:/categories";
		}
		return "category/category_form";
	}

	@GetMapping("/categories/{id}/enabled/{status}")
	public String updateCategoryEnabledStatus(@PathVariable("id") Integer id, @PathVariable("status") boolean enabled,
			RedirectAttributes redirect) {
		categoryService.updateCategoryEnabledStatus(id, enabled);
		LOGGER.info("/categories/" + id + "/enabled/" + enabled);
		String statusMessage = enabled ? "enabled" : "disabled";
		String message = "The Category ID " + id + " has Been " + statusMessage;
		LOGGER.info(message);
		redirect.addFlashAttribute("message", message);
		return "redirect:/categories";
	}

	@GetMapping("/categories/delete/{id}")
	public String deleteCategory(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
		try {
			categoryService.deleteCategory(id);
			String categoryDir = "../category-images/" + id;
			FileUploadUtil.removeDir(categoryDir);
			LOGGER.info("The Category id:" + id + "+has been deleted Successfully");
			redirectAttributes.addAttribute("message", "The Category id:" + id + "+has been deleted Successfully");
			return "redirect:/categories";
		} catch (CategoryNotFoundException e) {
			LOGGER.error(e.getMessage());
			redirectAttributes.addAttribute("message", e.getMessage());
			return "redirect:/categories";
		}

	}

	@GetMapping("/categories/export/csv")
	public void exportCSV(HttpServletResponse response) throws IOException {
		List<Category> listCategories = categoryService.listCategoriesUsedInForm();
		CategoryCSVExporter categoryCSVExporter = new CategoryCSVExporter();
		categoryCSVExporter.export(listCategories, response);
	}
}
