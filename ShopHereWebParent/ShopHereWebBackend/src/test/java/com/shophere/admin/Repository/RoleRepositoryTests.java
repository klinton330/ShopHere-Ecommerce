package com.shophere.admin.Repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Role;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class RoleRepositoryTests {

	@Autowired
	private RoleRepository role;
	@Test
	public void testCreateFirstRole()
	{
		Role roleAdmin=new Role("Admin","Manage Everything");
		Role savedRole=role.save(roleAdmin);
		assertThat(savedRole.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testRestRole()
	{
		Role roleAdmin=new Role(
				"Admin","Manages Everything");
		Role roleSalesparson=new Role("Salesperson","Manage Products price,Customers,Shipping,orders and sales Report");
		Role roleEditor=new Role("Editor","Manage Catagories,Brands,products,article and menus");
		Role roleShipper=new Role("Shipper","View Products,view orders and update order status");
		Role roleAssistant=new Role("Assistant","Manage Questions and reviews");
		
		role.saveAll(List.of(roleAdmin,roleSalesparson,roleEditor,roleShipper,roleAssistant));
		
	}
}
