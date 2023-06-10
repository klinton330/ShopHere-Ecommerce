package com.shophere.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shophere.admin.service.UserService;

@RestController
public class UserRestController {
    @Autowired
	private UserService userService;
    @PostMapping("/users/checkemail")
    public String checkDuplicateEmail(@Param("email")String email)
    {
    	return userService.isEmailUnique(email)?"OK":"DUPLICATED";
    			
    }
}
