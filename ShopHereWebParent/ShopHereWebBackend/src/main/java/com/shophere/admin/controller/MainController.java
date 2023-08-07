package com.shophere.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.shophere.admin.utils.CurrentDate;

@Controller
public class MainController {
	@GetMapping("")
	public String viewHomePage(Model model)
	{
		return "index";
	}
	
	@GetMapping("/login")
	public String viewloginPage()
	{
		return "login";
	}

}
