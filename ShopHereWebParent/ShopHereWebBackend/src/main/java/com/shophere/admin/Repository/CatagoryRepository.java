package com.shophere.admin.Repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shopme.common.entity.Category;

@Repository
public interface CatagoryRepository extends JpaRepository<Category, Integer> {
	
	@Query(value = "SELECT c from Category c Where c.parent.id is NULL")
	public List<Category> listRootCategories(Sort sort);
	
	public Category findByName(String categoryName);
	
	public Category findByAlias(String alias);
	
	@Query("update Category  c set c.enabled=?2 Where c.id=?1")
	@Modifying
	public void updateEnabledStatus(Integer id, boolean enabled);
	
	public Long countById(Integer id);
	

}
