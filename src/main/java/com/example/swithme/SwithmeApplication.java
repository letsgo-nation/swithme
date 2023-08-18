package com.example.swithme;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SwithmeApplication {

	public static void main(String[] args) {
		SpringApplication.run(SwithmeApplication.class, args);
	}

}
