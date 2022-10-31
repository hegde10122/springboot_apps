package com.hegde.springbootjdbc.demo;



import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@EnableJpaRepositories("com.hegde.springbootjdbc.demo.repo")
public class SpringbootjbdcApplication { // implements CommandLineRunner

	public static void main(String[] args)  {
		SpringApplication.run(SpringbootjbdcApplication.class, args);


	}

}
