package com.cayzlh.jwt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class JwtTestApplicationTests {

	public static void main(String[] args) {
		SpringApplication.run(JwtTestApplicationTests.class, args);
	}
}