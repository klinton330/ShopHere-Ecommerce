package com.shophere.admin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
	public String listAll(Model model) {
		List<User> listUser = userService.listAll();
		model.addAttribute("listusers", listUser);
		return "user";
	}

	@GetMapping("/users/new")
	private String newUserForm(Model model) {
		List<Role> listRoles = userService.listRoles();
		User userObj = new User();
		model.addAttribute("user", userObj);
		model.addAttribute("listRoles", listRoles);
		model.addAttribute("pageTitle", "Create New User");
		return "user-form";
	}

	@PostMapping("/users/save")
	public String saveUser(User userObj, RedirectAttributes redirectAttributes) {
		boolean isNewUser = false;
		if (userObj.getId() == null)
			isNewUser = true;
		userService.saveUser(userObj);
		if (isNewUser)
			redirectAttributes.addFlashAttribute("message", "The user has been saved Successfully");
		else
			redirectAttributes.addFlashAttribute("message", "The user has been Updated Successfully");
		return "redirect:/users";
	}

	@GetMapping("users/edit/{id}")
	public String editUser(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes, Model model) {
		try {
			User userObj = userService.getUser(id);
			System.out.println(userObj);
			model.addAttribute("user", userObj);
			model.addAttribute("pageTitle", "Edit User:" + id);
			List<Role> listRoles = userService.listRoles();
			model.addAttribute("listRoles", listRoles);
			return "user-form";
		} catch (UsernameNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
			return "redirect:/users";
		}
	}

	@GetMapping("users/delete/{id}")
	public String deleteUser(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
		try {
			userService.delete(id);
			redirectAttributes.addFlashAttribute("message", "The User Id: " + id + " has been deleted Successfully");
			return "redirect:/users";
		} catch (UsernameNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
			return "redirect:/users";
		}
	}

	@GetMapping("/users/{id}/enabled/{status}")
	public String enableAndDiableUser(@PathVariable("id") Integer id, @PathVariable("status") boolean status,
			RedirectAttributes redirectAttributes) {
             userService.updateUserEnabledService(id, status);
             String statusMessage=status?"enabled":"disabled";
             String message="The User ID "+id+" has Been "+statusMessage;
             redirectAttributes.addFlashAttribute("message", message);
             return "redirect:/users";
	}

}
