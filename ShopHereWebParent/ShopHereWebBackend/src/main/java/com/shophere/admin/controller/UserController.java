package com.shophere.admin.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shophere.admin.service.UserService;
import com.shopme.common.entity.Role;
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
	
	@GetMapping("/users/new")
	private String newUserForm(Model model)
	{
		List<Role>listRoles=userService.listRoles();
		User userObj=new User();
		model.addAttribute("user",userObj);
		model.addAttribute("listRoles",listRoles);
		return "user-form";
	}
	
	@PostMapping("/users/save")
	public String saveUser(User userObj,RedirectAttributes redirectAttributes)
	{
		System.out.println(userObj);
		userService.saveUser(userObj);
		redirectAttributes.addFlashAttribute("message","The user has been saved Successfully");
		return "redirect:/users";
	}
}
