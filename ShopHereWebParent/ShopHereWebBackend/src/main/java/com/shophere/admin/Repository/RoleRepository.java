package com.shophere.admin.Repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.shopme.common.entity.Role;

public interface RoleRepository extends CrudRepository<Role, Integer>{

}
