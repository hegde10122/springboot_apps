package com.hegde.springbootjdbc.demo.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class GeofenceTablesDAOImpl implements DAO {

    private static final Logger log =  LoggerFactory.getLogger(GeofenceTablesDAOImpl.class);
    private JdbcTemplate jdbcTemplate;

    public GeofenceTablesDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List list() {
        return null;
    }

    @Override
    public void create(Object o) {

    }

    @Override
    public Optional get(int id) {
        return Optional.empty();
    }

    @Override
    public void update(Object o, int id) {

    }
}
