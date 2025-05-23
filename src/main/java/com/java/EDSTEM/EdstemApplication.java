package com.java.EDSTEM;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "com")
@EnableScheduling
public class EdstemApplication {

	public static void main(String[] args) {
		SpringApplication.run(EdstemApplication.class, args);
	}

}
