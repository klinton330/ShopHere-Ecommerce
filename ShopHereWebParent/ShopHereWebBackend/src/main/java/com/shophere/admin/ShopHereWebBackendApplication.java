package com.shophere.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan({"com.shopme.common.entity","com.shophere.admin."})
public class ShopHereWebBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopHereWebBackendApplication.class, args);
	}

}
