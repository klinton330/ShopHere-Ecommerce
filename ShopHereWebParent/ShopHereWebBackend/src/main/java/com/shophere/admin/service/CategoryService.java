package com.shophere.admin.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

	public List<Category> listCategoriesUsedInForm() {
		List<Category> categoriesUsedInForm = new ArrayList<>();
		Iterable<Category> categoriesInDB = catagoryRepository.findAll();
		for (Category category : categoriesInDB) {
			if (category.getParent() == null) {
				categoriesUsedInForm.add(new Category(category.getName()));
				Set<Category> children = category.getChild();
				for (Category subCategory : children) {
					categoriesUsedInForm.add(new Category("--" + subCategory.getName()));
					printChildren(categoriesUsedInForm, subCategory, 1);
				}
			}
		}
		return categoriesUsedInForm;
	}

	private void printChildren(List<Category> categoriesUsedInForm, Category parent, int sublevel) {
		int newLevel = sublevel + 1;
		Set<Category> children = parent.getChild();
		for (Category subCategory : children) {
			String name = "";
			for (int i = 0; i < newLevel; i++)
				name += "--";
			name += subCategory.getName();
			categoriesUsedInForm.add(new Category(name));
			printChildren(categoriesUsedInForm, subCategory, newLevel);
		}
	}
}
