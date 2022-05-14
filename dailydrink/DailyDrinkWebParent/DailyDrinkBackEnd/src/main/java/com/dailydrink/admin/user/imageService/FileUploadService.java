package com.dailydrink.admin.user.imageService;

import java.io.File;

import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {
	File upload(MultipartFile imageFile, Integer id);
}
