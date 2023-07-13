package com.shophere.admin.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.shophere.admin.service.UserService;
import com.shophere.admin.utils.FileUploadUtil;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@Controller
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping("users")
	public String displayFirstPage(Model model) {
		return listByPage(1, model,"firstName","asc",null);
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
	public String saveUser(User userObj, RedirectAttributes redirectAttributes,
			@RequestParam("image") MultipartFile multipartFile) throws IOException {

		if (!multipartFile.isEmpty()) {
			// To get the File Name
			String fileName = org.springframework.util.StringUtils.cleanPath(multipartFile.getOriginalFilename());
			userObj.setPhotos(fileName);
			User savedUser = userService.saveUser(userObj);
			String upload = "user-photos/" + savedUser.getId();
			// Saving the image inside project Directory
			FileUploadUtil.cleanDir(upload);
			FileUploadUtil.saveFile(upload, fileName, multipartFile);

		} else {
			if (userObj.getPhotos().isEmpty())
				userObj.setPhotos(null);
		}

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
		String statusMessage = status ? "enabled" : "disabled";
		String message = "The User ID " + id + " has Been " + statusMessage;
		redirectAttributes.addFlashAttribute("message", message);
		return "redirect:/users";
	}
   
	//http://localhost:8085/ShopHereAdmin/users/page/1?sortField=email&sortDir=asc
	@GetMapping("/users/page/{pageNum}")
	public String listByPage(@PathVariable(name="pageNum")int pageNum,Model model,@Param("sortField")
	String sortField,@Param("sortOrder") String sortDir,@Param("keyword")String keyword)
	{
		  System.out.println("SortField:"+sortField);
		  System.out.println("SortOrder:"+sortDir);
		Page<User> page=userService.listByPage(pageNum,sortField,sortDir,keyword);
	  
		List<User>listUsers=page.getContent();
		long startCount=(pageNum-1)*userService.USER_PER_PAGE+1; //0,5
		long endCount=startCount+ userService.USER_PER_PAGE-1;//4,9no
		if(endCount> page.getTotalElements())
		{
			endCount=page.getTotalElements();
		}
		String reverseSortDir=sortDir.equalsIgnoreCase("asc")?"desc":"asc";
		model.addAttribute("totalPage", page.getTotalPages());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount",endCount);
		model.addAttribute("listusers", listUsers);
		model.addAttribute("totalItems",page.getTotalElements());
		model.addAttribute("SortField", sortField);
		model.addAttribute("SortDir", sortDir);
		model.addAttribute("SortField", sortField);
		model.addAttribute("reverseSortDir",  reverseSortDir);
		model.addAttribute("keyword",  keyword);
		return "user";
		
		
	}
}
