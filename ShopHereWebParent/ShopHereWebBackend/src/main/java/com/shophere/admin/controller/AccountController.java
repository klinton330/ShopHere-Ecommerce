package com.shophere.admin.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shophere.admin.security.ShopHereUserDetailService;
import com.shophere.admin.security.ShopHereUserDetails;
import com.shophere.admin.service.UserService;
import com.shophere.admin.utils.FileUploadUtil;
import com.shopme.common.entity.User;

@Controller
public class AccountController {

	@Autowired
	private UserService service;

	// @AuthenticationPrincipal annotation to get the currently authenticated user
	// details:
	@GetMapping("/account")
	public String viewDetails(@AuthenticationPrincipal ShopHereUserDetails loggedInUser, Model model) {
		String email = loggedInUser.getUsername();
		User user = service.getByEmail(email);
		model.addAttribute("user", user);
		return "account_form";

	}
	
	@PostMapping("/account/update")
	public String saveUser(User userObj, RedirectAttributes redirectAttributes,
			@RequestParam("image") MultipartFile multipartFile, @AuthenticationPrincipal ShopHereUserDetails loggedUser) throws IOException {

		if (!multipartFile.isEmpty()) {
			// To get the File Name
			String fileName = org.springframework.util.StringUtils.cleanPath(multipartFile.getOriginalFilename());
			userObj.setPhotos(fileName);
			User savedUser = service.updateAccount(userObj);
			String upload = "user-photos/" + savedUser.getId();
			// Saving the image inside project Directory
			FileUploadUtil.cleanDir(upload);
			FileUploadUtil.saveFile(upload, fileName, multipartFile);

		} else {
			if (userObj.getPhotos().isEmpty())
				userObj.setPhotos(null);
			service.updateAccount(userObj);
		}
		loggedUser.setFirstName(userObj.getFirstName());
		loggedUser.setLastName(userObj.getLastname());
		redirectAttributes.addFlashAttribute("message", "Your Account Details has been Updated");
		
		return "redirect:/account";
	}

}
