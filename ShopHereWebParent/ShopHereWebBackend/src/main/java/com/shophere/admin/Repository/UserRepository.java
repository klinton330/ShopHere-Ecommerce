package com.shophere.admin.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.shopme.common.entity.User;

public interface UserRepository extends JpaRepository<User, Integer>  {

	@Query(value = "SELECT * FROM users u WHERE u.email= :email", nativeQuery = true)
	public User getUserByEmail(@Param("email") String email);

	public Long countById(Integer id);

	// Update Query using Positional Parameters
	/* @Modifying for update and delete query */
	@Query("update User u set u.enabled=?2 Where u.id=?1")
	@Modifying
	public void updateEnabledStatus(Integer id, boolean enabled);
	
	@Query(value = "SELECT u from User u Where concat(u.id,' ',u.email,' ',u.firstName,' ',"+" u.lastname) Like %?1%")
	public Page<User>findAll(String keyword,Pageable pageable);

}
