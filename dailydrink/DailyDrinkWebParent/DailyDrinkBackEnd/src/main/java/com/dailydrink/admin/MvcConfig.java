package com.dailydrink.admin;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer{
	Logger logger = LoggerFactory.getLogger(MvcConfig.class);
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		String dirName = "user-photos";
		Path userPhotos = Paths.get(dirName);		
		
		String userPhotoPath = userPhotos.toFile().getAbsolutePath();			
		
		registry.addResourceHandler("/" + dirName + "/**")
		.addResourceLocations("file:/" + userPhotoPath + "/");
	}

	

}
