package com.dailydrink.admin.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.useRepresentation;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import com.dailydrink.common.entity.Role;
import com.dailydrink.common.entity.User;



@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTest {

	@Autowired
	private UserRepository repo;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	public void testCreateNewUserWithOneRole() {
		Role roleAdmin = entityManager.find(Role.class, 1);
		User user = new User("Michael", "Jordan", "mj@gmail.com", "123456");
		user.addRole(roleAdmin);
		
		User savedUser = repo.save(user);
		
		assertThat(savedUser.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateNewUserWithTwoRoles() {
		User user = new User("Kobe", "Bryant", "bryant@yahoo.com", "123456");
		Role roleEditor = new Role(4);
		Role roleAssistant = new Role(5);
		
		user.addRole(roleEditor);
		user.addRole(roleAssistant);
		
		User savedUser = repo.save(user);
		assertThat(savedUser.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateNewUserWithAllRoles() {
		Iterable<User> listUsers =  repo.findAll();
		listUsers.forEach(user -> System.out.println(user.toString()));
	}
	
	@Test
	public void testGetUserById() {
		User user = repo.findById(1).get();
		System.out.println(user);
		assertThat(user).isNotNull();		
	}
	
	@Test
	public void testUpdateUserDetails() { 
		User user = repo.findById(1).get();
		user.setEmail("testUpdate@gmail.com");
		user.setEnabled(true);
		
		repo.save(user);
	}
	
	@Test
	public void testUpdateUserRoles() { 
		User user = repo.findById(3).get();
		Role roleEditor = new Role(4);
		user.getRoles().remove(roleEditor);
		
		Role roleSales = new Role(5);
		user.getRoles().remove(roleSales);
		
		repo.save(user);
	}
	
	@Test
	public void testDeleteUsers() {
		Integer userId = 2;
		repo.deleteById(userId);
	}
	
	@Test
	public void testGetUserByEmail() {
		String emailString = "bonjovi@live.com";
		User user = repo.getUserByEmail(emailString);
		assertThat(user).isNotNull();
	}
	
	@Test 
	public void testCountById() {
		Integer id = 10;
		Long count= repo.countById(id);
		assertThat(count).isGreaterThan(0);
	}
	
	@Test
	public void testDisableUser() { 
		Integer idInteger = 1;
		repo.updateEnabledStatus(idInteger, false);	
	}
	
	@Test
	public void testEnabledUser() {
		Integer idInteger = 1;
		repo.updateEnabledStatus(idInteger, true);
	}
	
	@Test
	public void testListFirstPage() {
		int pageNumber = 1;
		int pageSize = 4;
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<User> page = repo.findAll(pageable);
		
		List<User> listUsers = page.getContent();
		
		listUsers.forEach(user -> System.out.println(user));
		
		assertThat(listUsers.size()).isEqualTo(pageSize);		
	}
	
	@Test
	public void jsonTest() {		
		try {
		    JSONObject jo2 = new JSONObject();
		    jo2.put("personName", "John Doe");
		    jo2.put("Age", "55");
		
		    JSONObject jo3 = new JSONObject();
		    jo3.put("personName", "Mary Rose");
		    jo3.put("Age", "22");
		
		    JSONObject jo4 = new JSONObject();
		    jo4.put("personName", "Edward");
		    jo4.put("Age", "19");
		
		    JSONArray ja1 = new JSONArray();
		    ja1.put(jo2);
		    ja1.put(jo3);
		    ja1.put(jo4);
		
		    JSONObject jo5 = new JSONObject();
		    jo5.put("young", "19");
		    jo5.put("middleage", "30");
		    jo5.put("old", "60");
		
		    JSONObject mObj = new JSONObject();
		    mObj.put("status", "ok");
		    mObj.put("person", ja1);
		    mObj.put("category", jo5);
		    
		    System.out.println(mObj);
		} catch(Exception e){
		
		}	
	}
}
