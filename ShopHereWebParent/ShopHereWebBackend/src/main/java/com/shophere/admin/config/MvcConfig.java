package com.shophere.admin.config;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer{

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		String dirName="user-photos";
		Path uploadPath = Paths.get(dirName);
		String userPhotoPath=uploadPath.toFile().getAbsolutePath();
		registry.addResourceHandler("/"+dirName+"/**").addResourceLocations("file:/"+userPhotoPath+"/");
	}
	
	

}
