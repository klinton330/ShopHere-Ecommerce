package com.shophere.admin.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shopme.common.entity.Category;

@Repository
public interface CatagoryRepository extends JpaRepository<Category, Integer> {

}
