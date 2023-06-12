package com.shophere.admin.Repository;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.shopme.common.entity.User;

public interface UserRepository extends CrudRepository<User, Integer> {
	
    @Query(value="SELECT * FROM users u WHERE u.email= :email",nativeQuery = true )
	 public User getUserByEmail(@Param("email")String email);

}
