package com.dailydrink.admin.user.imageService.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;
import org.apache.commons.io.FilenameUtils;

import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.dailydrink.admin.user.imageService.ImageService;

import nonapi.io.github.classgraph.utils.FileUtils;

@Service
public class imageServiceImpl  implements ImageService{
	
	private Logger log = LoggerFactory.getLogger(imageServiceImpl.class);
		
	@Override
	public boolean resizeImage(File sourceFile) {
		try {
			BufferedImage bufferedImage = ImageIO.read(sourceFile);			
			
			Path path = sourceFile.toPath();		
			
			BufferedImage resizedFile = resizeImage(bufferedImage);

			String extension = FilenameUtils.getExtension(sourceFile.getName());			
					
			File newImageFile = path.toFile();	
			
			ImageIO.write(resizedFile, extension, newImageFile);
			resizedFile.flush();
			
		} catch (Exception e) {
			log.debug(e.getMessage());
			return false;
		}
		return true;
	}
	
	public BufferedImage resizeImage(BufferedImage originalImage) throws Exception {
	    return Scalr.resize(originalImage, Scalr.Method.AUTOMATIC, Scalr.Mode.AUTOMATIC, 150, 150, Scalr.OP_ANTIALIAS);
	}

}
