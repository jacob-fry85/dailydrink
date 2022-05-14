package com.dailydrink.admin.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dailydrink.admin.security.WebSecurityConfig;
import com.dailydrink.common.entity.Role;
import com.dailydrink.common.entity.User;

import java.util.List;

import javax.transaction.Transactional;

@Service
@Transactional
public class UserService {
	public static final int USERS_PER_PAGE = 5; 
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private RoleRepository roleRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public List<User> listAll(){ 
		return (List<User>) userRepo.findAll();
	}
	
	public Page<User> listByPage(int pageNumber) {
		Pageable pageable = PageRequest.of(pageNumber -1, USERS_PER_PAGE);
		return userRepo.findAll(pageable);
	}
	
	public List<Role> listRoles(){  
		return (List<Role>) roleRepo.findAll();
	}
	
	public User saveUser(User user) {
		boolean isNewUser = (user.getId() == null);
		
		if(isNewUser) {
			encodePassword(user);								
		} else {
			User findByIdUser = userRepo.findById(user.getId()).get();
			
			if(user.getPassword() == null) { 
				user.setPassword(findByIdUser.getPassword());
			} else {
				encodePassword(user);								
			}
		}
		return userRepo.save(user);		
	}
	
	private void encodePassword(User user) {
		String encodePasswordString = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodePasswordString);
	}
	
	public boolean isEmailUnique(Integer id, String email) {
		User userByEmail = userRepo.getUserByEmail(email);
		if(userByEmail == null) return true;
		
		boolean isCreatingNew = (id == null);
		if(isCreatingNew) {
			if(userByEmail != null) return false;			
		} else {
			if(userByEmail.getId() != id) {
				return false;
			}
		}
		
		return true;
	}
	
	public User getUserById(Integer id) throws UserNotFoundException{  
		try {
			User userByIdUser = userRepo.findById(id).get();
			return userByIdUser;
		} catch (Exception e) {
			throw new UserNotFoundException("Could not find any user with ID " + id);
		}
	}
	
	public void deleteById(Integer id) throws UserNotFoundException { 
		Long countById = userRepo.countById(id);
		
		if(countById == 0 || countById == null) throw new UserNotFoundException("Could not find any user with ID " + id);
		
		userRepo.deleteById(id);
		
	}
	
	public void updateEnabledStatus(Integer id, boolean status) {
		userRepo.updateEnabledStatus(id, status);
	}
}
