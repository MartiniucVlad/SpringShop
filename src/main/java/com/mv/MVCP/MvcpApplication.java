package com.mv.MVCP;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
// scan all MVCP package
@EntityScan(basePackages = "com.mv.MVCP")
public class MvcpApplication {

	public static void main(String[] args) {
		SpringApplication.run(MvcpApplication.class, args);
	}

}
