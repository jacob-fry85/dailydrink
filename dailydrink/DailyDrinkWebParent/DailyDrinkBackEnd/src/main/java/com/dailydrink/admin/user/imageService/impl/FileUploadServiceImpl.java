package com.dailydrink.admin.user.imageService.impl;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.aspectj.weaver.bcel.ExtensibleURLClassLoader;
import org.codehaus.groovy.runtime.StringGroovyMethods;
import org.hibernate.internal.build.AllowSysOut;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.dailydrink.admin.user.imageService.FileUploadService;

import ch.qos.logback.classic.Logger;

@Service
public class FileUploadServiceImpl implements FileUploadService{
	
	private org.slf4j.Logger logger = LoggerFactory.getLogger(FileUploadServiceImpl.class);

	@Override
	public File upload(MultipartFile imageFile, Integer id) {
		String uploadDir = "user-photos/" + id;
		String fileName = StringUtils.cleanPath(imageFile.getOriginalFilename());	
		fileName = fileName.replaceAll("\\s+", "");
		cleanDir(uploadDir);
		Path uploadPath = Paths.get(uploadDir);	
		
		try {
			if(!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}					
			
			InputStream inputStream = imageFile.getInputStream();
			Path filePath = uploadPath.resolve(fileName);
			
			Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
			
			return filePath.toFile();
		} catch (IOException e) {
			logger.error(e.getMessage());
			return null;
		}		
	}
	
	public static void cleanDir(String dir) {
		Path dirPath = Paths.get(dir);
		
		try {
			Files.list(dirPath).forEach(file-> {
				if(!Files.isDirectory(file)) {
					try {
						Files.delete(file);
					} catch (Exception e) {
						System.out.println("Could not delete file!" + file);
					}
				}
			});
		} catch (IOException e) {
			System.out.println("Could not list directory" + dirPath);
		}
	}
}
