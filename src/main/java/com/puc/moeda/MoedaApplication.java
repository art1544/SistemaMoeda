package com.puc.moeda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MoedaApplication {

	public static void main(String[] args) {
		SpringApplication.run(MoedaApplication.class, args);
	}

}
