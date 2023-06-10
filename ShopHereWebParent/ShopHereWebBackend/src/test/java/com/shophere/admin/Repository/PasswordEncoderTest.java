package com.shophere.admin.Repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderTest {
	
	
	@Test
	public void testEncodePassword()
	{
		BCryptPasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
		String rawPassword="nam2020";
		String encodePassword=passwordEncoder.encode(rawPassword);
		
		System.out.println(encodePassword);
		
		
		boolean matches=passwordEncoder.matches("nam2020", encodePassword);
		assertThat(matches).isTrue();
	}

}
