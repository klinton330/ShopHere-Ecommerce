package com.shophere.admin.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.shophere.admin.service.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;
	@GetMapping("users")
	public  String listAll()
	{
		System.out.println("Inside");
		return "user";
	}
}
