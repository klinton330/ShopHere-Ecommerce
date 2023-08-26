package com.shophere.admin.config;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.shophere.admin.controller.CategoryController;

@Configuration
public class MvcConfig implements WebMvcConfigurer{
	private static final Logger LOGGER=LoggerFactory.getLogger( WebMvcConfigurer.class);
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		String dirName="user-photos";
		Path uploadPath = Paths.get(dirName);
		String userPhotoPath=uploadPath.toFile().getAbsolutePath();
		LOGGER.info("User Photo Path:"+userPhotoPath);
		registry.addResourceHandler("/"+dirName+"/**").addResourceLocations("file:/"+userPhotoPath+"/");
		
		
		String categoryImageDir="../category-images";
		Path categoryUploadPath = Paths.get(categoryImageDir);
		String categoryPhotoPath= categoryUploadPath.toFile().getAbsolutePath();
		LOGGER.info("User Category Photo Path:"+categoryPhotoPath);
		registry.addResourceHandler("/category-images/**").addResourceLocations("file:/"+categoryPhotoPath+"/");
	}
	
	

}
