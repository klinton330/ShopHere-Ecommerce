package com.shophere.admin.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.shophere.admin.service.DashboardService;
import com.shophere.admin.utils.CurrentDate;

@Controller
public class MainController {
	private static final Logger LOGGER = LoggerFactory.getLogger(MainController.class);
	@Autowired
	private DashboardService dashboardService;
	@GetMapping("/")
	public String viewHomePage(Model model)
	{
		Long userCount=dashboardService.getUserCount();
		Long categoryCount=dashboardService.getCategoryCount();
		LOGGER.info("User Count:"+userCount);
		LOGGER.info("Category Count:"+categoryCount);
		model.addAttribute("userCount", userCount);
		model.addAttribute("categoryCount", categoryCount);
		return "index";
	}
	
	@GetMapping("/login")
	public String viewloginPage()
	{
		return "login";
	}

}
