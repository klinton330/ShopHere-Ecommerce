package com.shophere.admin.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.shophere.admin.Repository.CatagoryRepository;
import com.shophere.admin.exception.CategoryNotFoundException;
import com.shopme.common.entity.Category;

@Service
public class CategoryService {
	@Autowired
	private CatagoryRepository catagoryRepository;

	public List<Category> listAll() {
		List<Category> rootCategories = catagoryRepository.listRootCategories();
		return listHierarchicakCategories(rootCategories);
	}

	private List<Category> listHierarchicakCategories(List<Category> rootCategory) {
		List<Category> listHierarchicakCategories = new ArrayList<>();
		for (Category rootcategory :  rootCategory) {
			// Object in listHierarchicakCategories list contains full details of Parent
			listHierarchicakCategories.add(Category.copyFullCategoryObject(rootcategory));
			Set<Category> children = rootcategory.getChild();
			for (Category subCategory : children) {
				String name = "--" + subCategory.getName();
				listHierarchicakCategories.add(Category.copyFullCategoryObject(subCategory, name));
				listSubHierarchichalCategories(subCategory, listHierarchicakCategories, 1);
			}
		}
		
		return listHierarchicakCategories;
	}

	private void listSubHierarchichalCategories(Category category, List<Category> listHierarchicakCategories,
			int sublevel) {
		int newLevel = sublevel + 1;
		Set<Category> children = category.getChild();
		for (Category subCategory : children) {
			String name = "";
			for (int i = 0; i < newLevel; i++)
				name += "--";
			name += subCategory.getName();
			listHierarchicakCategories.add(Category.copyFullCategoryObject(category, name));
			listSubHierarchichalCategories(subCategory, listHierarchicakCategories, newLevel);
		}
	}

	public Category save(Category category) {
		return catagoryRepository.save(category);
	}

	public List<Category> listCategoriesUsedInForm() {
		List<Category> categoriesUsedInForm = new ArrayList<>();
		Iterable<Category> categoriesInDB = catagoryRepository.findAll();
		for (Category category : categoriesInDB) {
			if (category.getParent() == null) {
				categoriesUsedInForm.add(Category.copyIdAndName(category));
				Set<Category> children = category.getChild();
				for (Category subCategory : children) {
					String name = "--" + subCategory.getName();
					categoriesUsedInForm.add(Category.copyIdAndName(subCategory.getId(), name));
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
			categoriesUsedInForm.add(Category.copyIdAndName(subCategory.getId(), name));
			printChildren(categoriesUsedInForm, subCategory, newLevel);
		}
	}
	
	public Category findCategoryById(Integer id) throws CategoryNotFoundException
	{
		try
		{
			 return catagoryRepository.findById(id).get();        
		}
		catch(Exception e){
			throw new CategoryNotFoundException("category Not Found For Id:"+id);
		}
	}
}
