package com.shophere.admin.controller;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

import com.shophere.admin.export.UserCSVExporter;
import com.shophere.admin.export.UserExcelExporter;
import com.shophere.admin.export.UserPDFExporter;
import com.shophere.admin.service.UserService;
import com.shophere.admin.utils.FileUploadUtil;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class UserController {

	private static final Logger LOGGER=LoggerFactory.getLogger(UserController.class);
	@Autowired
	private UserService userService;

	@GetMapping("users")
	public String displayFirstPage(Model model) {
		return listByPage(1, model, "firstName", "asc", null);
	}

	@GetMapping("/users/new")
	private String newUserForm(Model model) {
		LOGGER.info("Create New User page called");
		List<Role> listRoles = userService.listRoles();
		User userObj = new User();
		model.addAttribute("user", userObj);
		model.addAttribute("listRoles", listRoles);
		model.addAttribute("pageTitle", "Create New User");
		return "users/user-form";
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
		String firstPartOfEmail = userObj.getEmail().split("@")[0];
		return "redirect:/users/page/1?sortField=firstName&sortDir=asc&keyword=" + firstPartOfEmail;
	}

	@GetMapping("users/edit/{id}")
	public String editUser(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes, Model model) {
		try {
			User userObj = userService.getUser(id);
			model.addAttribute("user", userObj);
			model.addAttribute("pageTitle", "Edit User:" + id);
			List<Role> listRoles = userService.listRoles();
			model.addAttribute("listRoles", listRoles);
			return "users/user-form";
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

	// http://localhost:8085/ShopHereAdmin/users/page/1?sortField=email&sortDir=asc
	@GetMapping("/users/page/{pageNum}")
	public String listByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
			@Param("sortField") String sortField, @Param("sortOrder") String sortDir,
			@Param("keyword") String keyword) {
		System.out.println("SortField:" + sortField);
		System.out.println("SortOrder:" + sortDir);
		Page<User> page = userService.listByPage(pageNum, sortField, sortDir, keyword);

		List<User> listUsers = page.getContent();
		long startCount = (pageNum - 1) * userService.USER_PER_PAGE + 1; // 0,5
		long endCount = startCount + userService.USER_PER_PAGE - 1;// 4,9no
		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}
		String reverseSortDir = sortDir.equalsIgnoreCase("asc") ? "desc" : "asc";
		model.addAttribute("totalPage", page.getTotalPages());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("listusers", listUsers);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("SortField", sortField);
		model.addAttribute("SortDir", sortDir);
		model.addAttribute("SortField", sortField);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword", keyword);
		return "users/user";

	}

	@GetMapping("/users/export/csv")
	public void exportToCSV(HttpServletResponse httpServletResponse) throws IOException {
		UserCSVExporter userCsvExporter = new UserCSVExporter();
		List<User> lisUsers = userService.listAll();
		userCsvExporter.export(lisUsers, httpServletResponse);
	}

	@GetMapping("/users/export/excel")
	public void exportToExcel(HttpServletResponse httpServletResponse) throws IOException {
		UserExcelExporter userExcelExporter = new UserExcelExporter();
		List<User> lisUsers = userService.listAll();
		userExcelExporter.export(lisUsers, httpServletResponse);
	}

	@GetMapping("/users/export/pdf")
	public void exportToPDF(HttpServletResponse httpServletResponse) throws IOException {
		List<User> lisUsers = userService.listAll();
		UserPDFExporter pdfExporter = new UserPDFExporter();
		pdfExporter.export(lisUsers, httpServletResponse);
	}

}
