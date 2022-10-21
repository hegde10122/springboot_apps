package com.hegde.springbootjdbc.demo.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class EmployeeDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void createTable(){

        var query = "CREATE TABLE employee(id SERIAL PRIMARY KEY, name varchar(255) NOT NULL,address varchar(255) NOT NULL,location geometry(POINT,4326))";

        var create = jdbcTemplate.update(query);
        System.out.println("TABLE created "+create);
    }
}
