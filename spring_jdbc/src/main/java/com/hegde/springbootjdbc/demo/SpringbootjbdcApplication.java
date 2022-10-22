package com.hegde.springbootjdbc.demo;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.hegde.springbootjdbc.demo.dao.EmployeeDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ResourceLoader;

@SpringBootApplication
public class SpringbootjbdcApplication { // implements CommandLineRunner

	public static void main(String[] args)  {
		SpringApplication.run(SpringbootjbdcApplication.class, args);


	}

}
