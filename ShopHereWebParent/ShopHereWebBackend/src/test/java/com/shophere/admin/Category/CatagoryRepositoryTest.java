package com.shophere.admin.Category;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shophere.admin.Repository.CatagoryRepository;
import com.shopme.common.entity.Category;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
//@Rollback(false)
public class CatagoryRepositoryTest {

	@Autowired
	private CatagoryRepository repo;

	//Test Creating top-level(root) Catagories
	@Test
	public void testCreateRootCategory() {
		Category category = new Category("Electronics");
		Category savedCategory = repo.save(category);
	     assertThat(savedCategory.getId()).isGreaterThan(0);
	}
	
	//Test creating a sub catagory
	@Test
	public void testCreateSubCategory() {
		Category parent=new Category(12);
	//	Category subCategory=new Category("Desktop", parent);
		Category subCategory1=new Category("Laptop", parent);
		Category subCategory2=new Category("Bluethooth", parent);
		
	//	Category savedCatagory=repo.save(subCategory);
	   List<Category>listOf= repo.saveAll(List.of(subCategory1,subCategory2));
	   System.out.println(listOf.size());
        assertThat(listOf.size()).isGreaterThan(0);
	}
	@Test
	public void testCreateOneSubCategory()
	{
		Category parent=new Category(16);
		Category subCategory=new Category("headphone", parent);
		Category savedCatagory=repo.save(subCategory);
		assertThat(savedCatagory.getId()).isGreaterThan(1);
	}
}
