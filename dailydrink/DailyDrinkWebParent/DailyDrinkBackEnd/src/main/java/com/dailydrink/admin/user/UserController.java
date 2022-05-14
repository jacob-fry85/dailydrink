package com.dailydrink.admin.user;



import static org.mockito.ArgumentMatchers.intThat;
import static org.mockito.ArgumentMatchers.longThat;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dailydrink.admin.MvcConfig;
import com.dailydrink.admin.user.imageService.FileUploadService;
import com.dailydrink.admin.user.imageService.ImageService;
import com.dailydrink.common.entity.Role;
import com.dailydrink.common.entity.User;

@Controller
public class UserController {
	private Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private FileUploadService fileUploadService;
	
	@Autowired
	private ImageService imageService;

	@GetMapping("/users")
	public String listFirstPage(Model model){ 
		return listByPage(1, model);
	}
	
	@GetMapping("/users/page/{pageNumber}")
	public String listByPage(@PathVariable(name= "pageNumber") int pageNumber, Model model) {
		Page<User> page = userService.listByPage(pageNumber);
		List<User> listUsers = page.getContent();
		
		long startCount = (pageNumber - 1) * userService.USERS_PER_PAGE + 1;
		long endCount = startCount + userService.USERS_PER_PAGE - 1;
		if(endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}
		
		long totalPages = page.getTotalElements() % userService.USERS_PER_PAGE;
		if(totalPages != 0) {
			totalPages = (page.getTotalElements() / userService.USERS_PER_PAGE) + 1;
		}
		
//		int total  =(int)totalPages;
		
		model.addAttribute("listUsers", listUsers);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("currentPage", pageNumber);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalPages", totalPages);
		
		return "users";
	}
	
	@GetMapping("/user/new")
	public String newUser(Model model) { 
		List<Role> listRoles = userService.listRoles();	
		User user = new User();
		user.setEnabled(true);		
		
		model.addAttribute("pageTitle", "Create New User");		
		model.addAttribute("user", user);
		model.addAttribute("listRoles", listRoles);		
		
		return "user_form";
	}
	
	@PostMapping("/users/save")
	public String saveUser(User user, RedirectAttributes redirectAttributes,
			@RequestParam("image") MultipartFile multipartFile) {
		 String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename()); 
		 fileName = fileName.replaceAll("\\s+", "");
		 
        if(!multipartFile.isEmpty()) {
        	user.setPhoto(fileName);
        	
        	User savedUser = userService.saveUser(user);
        	File file = fileUploadService.upload(multipartFile, savedUser.getId());
        	
        	
     	    if(file == null) {
     	    	redirectAttributes.addFlashAttribute("message", "Upload failed.");
     	    	return "redirect:/users_form";
     	    }
     		
     	    boolean resizeResult = imageService.resizeImage(file);
     	    if(!resizeResult) {
     	    	redirectAttributes.addFlashAttribute("message", "Resize failed!");
     	    	return "redirect:/users_form";
     	    }
        } else {
        	if(user.getPhoto().isEmpty()) user.setPhoto(null);
        	userService.saveUser(user);
        }
		
		redirectAttributes.addFlashAttribute("message", "Data successfully saved!");
		
		return "redirect:/users";
	}
	
	
	
	@GetMapping("/users/edit/{id}")
	public String getUserById(@PathVariable Integer id, Model model,
			RedirectAttributes redirectAttributes) { 
		try {
			User userById = userService.getUserById(id);
			model.addAttribute("user", userById);
			
			List<Role> listRoles = userService.listRoles();
			model.addAttribute("listRoles", listRoles);
			
			model.addAttribute("pageTitle", "Edit User (ID : " + id + ")");
			
			return "user_form";
		} catch (UserNotFoundException e) {
			redirectAttributes.addFlashAttribute("message" , e.getMessage());
						
			return "redirect:/users";
		}				
	}
	
	@GetMapping("/users/delete/{id}")
	public String deleteUserByStringId(@PathVariable("id") Integer id,
			RedirectAttributes redirectAttributes) {
		try {
			userService.deleteById(id);
			redirectAttributes.addFlashAttribute("message", "ID " + id + " has successfully deleted!");
		} catch (UserNotFoundException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		
		return "redirect:/users";
	}
	
	@GetMapping("/users/{id}/update/{status}")
	public String updateEnabledStatus(@PathVariable("id") Integer id, @PathVariable("status") Boolean status,
			RedirectAttributes redirectAttributes) {
		
		userService.updateEnabledStatus(id, status);
		String enabled = status? "Enabled" : "Disabled";
		redirectAttributes.addFlashAttribute("message" , enabled + " ID "  + id + "!");
			
					 
		return "redirect:/users";
	}
	
	
} 
