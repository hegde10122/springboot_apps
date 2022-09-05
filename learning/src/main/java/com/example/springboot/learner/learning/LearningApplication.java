package com.example.springboot.learner.learning;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
// Since we are using different package to place in our controllers - we define
// basePackages in @ComponentScan
@ComponentScan(basePackages = "com.example.springboot.learner")

// define the package where the db repository will search for the db entities
@EnableJpaRepositories(basePackages = "com.example.springboot.learner")

// define the base package where the entity is placed
@EntityScan(basePackages = "com.example.springboot.learner")
public class LearningApplication {

	public static void main(String[] args) {
		SpringApplication.run(LearningApplication.class, args);
		System.out.println("Hello world!!");
	}
}
