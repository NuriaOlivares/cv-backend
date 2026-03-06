package com.nuria.cvplatform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class CvplatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(CvplatformApplication.class, args);
	}

}
