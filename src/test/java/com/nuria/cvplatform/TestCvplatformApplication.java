package com.nuria.cvplatform;

import org.springframework.boot.SpringApplication;

public class TestCvplatformApplication {

	public static void main(String[] args) {
		SpringApplication.from(CvplatformApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
