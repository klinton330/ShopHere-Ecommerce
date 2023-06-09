package com.shophere.admin.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.shophere.admin.service.UserService;
import com.shopme.common.entity.User;



@Controller
public class UserController {

	@Autowired
	private UserService userService;
	@GetMapping("users")
	public  String listAll(Model model)
	{
		List<User>listUser=userService.listAll();
		model.addAttribute("listusers",listUser);
		return "user";
	}
}
