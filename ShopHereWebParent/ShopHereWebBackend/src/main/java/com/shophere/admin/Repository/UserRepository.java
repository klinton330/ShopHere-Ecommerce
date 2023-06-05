package com.shophere.admin.Repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.shopme.common.entity.User;

public interface UserRepository extends CrudRepository<User, Integer> {

}
