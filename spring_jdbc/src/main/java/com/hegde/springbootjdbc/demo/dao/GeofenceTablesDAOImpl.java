package com.hegde.springbootjdbc.demo.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.*;

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
        jdbcTemplate.execute((String) o);
    }

    @Override
    public Optional get(int id) {
        return Optional.empty();
    }

    @Override
    public void addRows(Object o,Object tableName) {

        List<HashMap<String, Object>> features = (List<HashMap<String, Object>>) o;

        //process parallel all features in the geojson
        features.parallelStream().forEach(
                row -> {

                    //insert query to be built by this string builder
                    StringBuilder insertQuery = new StringBuilder();

                    //column names and their respective values will be built by these strings
                    StringBuilder columnNamesString = new StringBuilder();
                    StringBuilder columnValuesString = new StringBuilder();

                    insertQuery.append("INSERT INTO ").append(tableName.toString()).append(" ");
                    insertQuery.append("(");
                    columnValuesString.append("(");

                    int counter = 0;
                    //get the set of all column names and the values for each row
                    for (Map.Entry<String, Object> setColumns :
                            row.entrySet()) {

                        counter++; //counter to identify last column and the corresponding value. append comma or bracket accordingly

                        if(counter != row.entrySet().size()){

                            columnNamesString.append(setColumns.getKey()).append(", "); //append comma
                            columnValuesString.append(setColumns.getValue()).append(", "); //append comma

                        }else {
                            columnNamesString.append(setColumns.getKey()).append(")"); //append bracket to end
                            columnValuesString.append(setColumns.getValue()).append(");"); //append bracket and semicolon to end the statement
                        }
                    }

                    //insert query is ready for use here
                   insertQuery.append(columnNamesString).append(" VALUES ").append(columnValuesString);
                    jdbcTemplate.execute(insertQuery.toString()); //fire the query using jdbc
                }
        );
    }

    @Override
    public void updateRows(Object o, Object whereClause) {

    }
}
