package com.example.orctest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.security.autoconfigure.SecurityAutoConfiguration;
import org.springframework.boot.security.autoconfigure.actuate.web.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(exclude = {
		  SecurityAutoConfiguration.class,
  ManagementWebSecurityAutoConfiguration.class
})
@EnableAsync
// @SpringBootApplication
public class OrcTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrcTestApplication.class, args);
	}

}
