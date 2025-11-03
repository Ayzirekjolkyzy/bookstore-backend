package com.okuylu_back;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



@SpringBootApplication(scanBasePackages = "com.okuylu_back")
public class OkuyluApplication {

	public static void main(String[] args) {
		SpringApplication.run(OkuyluApplication.class, args);
	}

}
