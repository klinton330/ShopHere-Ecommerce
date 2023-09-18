package com.shophere.admin.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.support.Repositories;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.shophere.admin.Repository.CatagoryRepository;
import com.shophere.admin.exception.CategoryNotFoundException;
import com.shopme.common.entity.Category;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CategoryService {
	@Autowired
	private CatagoryRepository catagoryRepository;

	public List<Category> listAll(String sortDir) {
		Sort sort = Sort.by("name");
		if (sortDir == null || sortDir.isEmpty())
			sort = sort.ascending();
		else {
			if (sortDir.equals("asc"))
				sort = sort.ascending();
			else if (sortDir.equals("desc"))
				sort = sort.descending();
		}
		List<Category> rootCategories = catagoryRepository.listRootCategories(sort);
		return listHierarchicakCategories(rootCategories, sortDir);
	}

	private List<Category> listHierarchicakCategories(List<Category> rootCategory, String sortDir) {
		List<Category> listHierarchicakCategories = new ArrayList<>();
		for (Category rootcategory : rootCategory) {
			// Object in listHierarchicakCategories list contains full details of Parent
			listHierarchicakCategories.add(Category.copyFullCategoryObject(rootcategory));
			Set<Category> children = sortSubCategories(rootcategory.getChild(), sortDir);
			for (Category subCategory : children) {
				String name = "--" + subCategory.getName();
				listHierarchicakCategories.add(Category.copyFullCategoryObject(subCategory, name));
				listSubHierarchichalCategories(subCategory, listHierarchicakCategories, 1, sortDir);
			}
		}

		return listHierarchicakCategories;
	}

	private void listSubHierarchichalCategories(Category category, List<Category> listHierarchicakCategories,
			int sublevel, String sortDir) {
		int newLevel = sublevel + 1;
		Set<Category> children = sortSubCategories(category.getChild(), sortDir);
		for (Category subCategory : children) {
			String name = "";
			for (int i = 0; i < newLevel; i++)
				name += "--";
			name += subCategory.getName();
			listHierarchicakCategories.add(Category.copyFullCategoryObject(category, name));
			listSubHierarchichalCategories(subCategory, listHierarchicakCategories, newLevel, sortDir);
		}
	}

	public Category save(Category category) {
		return catagoryRepository.save(category);
	}

	public List<Category> listCategoriesUsedInForm() {
		List<Category> categoriesUsedInForm = new ArrayList<>();
		Iterable<Category> categoriesInDB = catagoryRepository.listRootCategories(Sort.by("name").ascending());
		for (Category category : categoriesInDB) {
			if (category.getParent() == null) {
				categoriesUsedInForm.add(Category.copyIdAndName(category));
				Set<Category> children = sortSubCategories(category.getChild());
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
		Set<Category> children = sortSubCategories(parent.getChild());
		for (Category subCategory : children) {
			String name = "";
			for (int i = 0; i < newLevel; i++)
				name += "--";
			name += subCategory.getName();
			categoriesUsedInForm.add(Category.copyIdAndName(subCategory.getId(), name));
			printChildren(categoriesUsedInForm, subCategory, newLevel);
		}
	}

	public Category findCategoryById(Integer id) throws CategoryNotFoundException {
		try {
			return catagoryRepository.findById(id).get();
		} catch (Exception e) {
			throw new CategoryNotFoundException("category Not Found For Id:" + id);
		}
	}

	public String checkUniqueness(Integer id, String name, String alias) {
		// If Parent Category it will return a true
		boolean isCreatingNew = (id == null || id == 0);
		Category categoryByName = catagoryRepository.findByName(name);
		// Condition will pass if its a parent category
		if (isCreatingNew) {
			// Checking if already this parent name exists.
			if (categoryByName != null) {
				return "DupliacteName";
			} else {
				Category categoryByAlias = catagoryRepository.findByAlias(alias);
				if (categoryByAlias != null)
					return "DupliacteAlias";
			}
		}
		// Condition will pass if its a Edit
		else {
			if (categoryByName != null && categoryByName.getId() != id) {
				return "DuplicateName";
			} else {
				Category categoryByAlias = catagoryRepository.findByAlias(alias);
				if (categoryByAlias != null && categoryByAlias.getId() != id)
					return "DuplicateAlias";
			}

		}
		return "OK";
	}

	private SortedSet<Category> sortSubCategories(Set<Category> children) {
		return sortSubCategories(children, "asc");
	}

	private SortedSet<Category> sortSubCategories(Set<Category> children, String sortDir) {
		SortedSet<Category> sortedChildrenSet = new TreeSet<>(new Comparator<Category>() {

			@Override
			public int compare(Category cat1, Category cat2) {
				if (sortDir == null)
					return cat1.getName().compareTo(cat2.getName());
				else {
					if (sortDir.equals("asc"))
						return cat1.getName().compareTo(cat2.getName());
					else
						return cat2.getName().compareTo(cat1.getName());
				}
			}
		});
		sortedChildrenSet.addAll(children);
		return sortedChildrenSet;
	}
	
	public void updateCategoryEnabledStatus(Integer id,boolean enabled) {
		catagoryRepository.updateEnabledStatus(id, enabled);
	}
}
