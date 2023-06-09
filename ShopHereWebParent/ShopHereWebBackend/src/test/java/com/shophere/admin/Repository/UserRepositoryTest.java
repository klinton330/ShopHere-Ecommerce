package com.shophere.admin.Repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTest {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private TestEntityManager testEntityManager;
	
	@Test
	public void testCreateUserForOneRole()
	{
		//getting roles from Roles table using the TestEntityManager
		Role admin=testEntityManager.find(Role.class, 1);
		User userHari=new User("ravi@gmail.com", "ravi","ravi", "ravi","JPEG");
		userHari.addRole(admin);
		User savedUser=userRepository.save(userHari);
		assertThat(savedUser.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateUserForTwoRole()
	{
	    User userVirat=new User("rutraj@gmail.com", "rutraj","rutraj", "rutraj","JPEG");
		Role sales=new Role(2);
		Role shipper=new Role(4);
		userVirat.addRole(sales);
		userVirat.addRole(shipper);
		User savedUser=userRepository.save(userVirat);
		assertThat(savedUser.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testListAllUsers()
	{
		Iterable< User>listUsers=userRepository.findAll();
		listUsers.forEach(user->System.out.println(user));
	}
	
	@Test
	public void testById()
	{
		 User userHari=userRepository.findById(1).get();
		 assertThat(userHari).isNotNull();
	}
	@Test
	public void updateUserDetailsById() {
		 User userHari=userRepository.findById(1).get();
		 userHari.setEnabled(true);
		 userHari.setEmail("hari1@gmail.com");
		 userRepository.save(userHari);
	}
	
	@Test
	public void updateRoles()
	{
		User userHari=userRepository.findById(11).get();
		Role roleAdmin=new Role(1);// I
		Role salesMan=new Role(3);
	    userHari.getRoles().remove(roleAdmin);
		userHari.addRole(salesMan);
		userRepository.save(userHari);
	}
	
	@Test
	public void deleteUser()
	{
		Integer userId=11;
		userRepository.deleteById(userId);
	}
}
