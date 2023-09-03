package com.shophere.admin.Category;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.shophere.admin.Repository.CatagoryRepository;
import com.shopme.common.entity.Category;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
//@Rollback(false)
public class CatagoryRepositoryTest {

	@Autowired
	private CatagoryRepository repo;

	// Test Creating top-level(root) Catagories
	@Test
	public void testCreateRootCategory() {
		Category category = new Category("Electronics");
		Category savedCategory = repo.save(category);
		assertThat(savedCategory.getId()).isGreaterThan(0);
	}

	// Test creating a sub catagory
	@Test
	public void testCreateSubCategory() {
		Category parent = new Category(12);
		// Category subCategory=new Category("Desktop", parent);
		Category subCategory1 = new Category("Laptop", parent);
		Category subCategory2 = new Category("Bluethooth", parent);

		// Category savedCatagory=repo.save(subCategory);
		List<Category> listOf = repo.saveAll(List.of(subCategory1, subCategory2));
		System.out.println(listOf.size());
		assertThat(listOf.size()).isGreaterThan(0);
	}

	@Test
	public void testCreateOneSubCategory() {
		Category parent = new Category(16);
		Category subCategory = new Category("headphone", parent);
		Category savedCatagory = repo.save(subCategory);
		assertThat(savedCatagory.getId()).isGreaterThan(1);
	}

	@Test
	public void testGetCategory() {
		Category category = repo.findById(2).get();
		System.out.println(category.getName());
		Set<Category> children = category.getChild();
		for (Category subCategory : children) {
			System.out.println(subCategory.getName());
		}
		assertThat(children.size()).isGreaterThan(0);
	}

	@Test
	public void testPrintHierarchical() {
		Iterable<Category> categories = repo.findAll();
		for (Category category : categories) {
			if (category.getParent() == null) {
				System.out.println(category.getName());
				Set<Category> children = category.getChild();
				for (Category subCategory : children) {
					System.out.println("-" + subCategory.getName());
					printChildren(subCategory, 1);
				}
			}
		}
	}

	private void printChildren(Category parent, int sublevel) {
		int newLevel = sublevel + 1;
		Set<Category> children = parent.getChild();
		for (Category subCategory : children) {
			for (int i = 0; i < newLevel; i++)
				System.out.print("-");
			System.out.println(subCategory.getName());
			printChildren(subCategory, newLevel);
		}
	}

	@Test
	public void testListRootCategories() {
		List<Category> rootCategories = repo.listRootCategories();
		rootCategories.forEach(x -> System.out.println(x.getName()));
	}

	@Test
	public void testCategoryName() {
		String name = "Computer";
		Category category = repo.findByName(name);
		assertThat(category).isNotNull();
		assertThat(category.getName()).isEqualTo(name);
	}

	@Test
	public void testFindByAlias() {
		String alias = "Computer world";
		Category category = repo.findByAlias(alias);
		assertThat(category).isNotNull();
		assertThat(category.getAlias()).isEqualTo(alias);
	}
}
