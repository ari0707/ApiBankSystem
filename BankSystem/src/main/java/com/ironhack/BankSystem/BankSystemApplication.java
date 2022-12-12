package com.ironhack.BankSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication//(scanBasePackages = {"com.*"})
public class BankSystemApplication {

	public static void main(String[] args) {

		SpringApplication.run(BankSystemApplication.class, args);
	}

}
