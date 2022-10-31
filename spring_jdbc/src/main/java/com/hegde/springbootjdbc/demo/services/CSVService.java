package com.hegde.springbootjdbc.demo.services;

import com.hegde.springbootjdbc.demo.dao.GeofenceTablesDAOImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CSVService {

    @Autowired
    GeofenceTablesDAOImpl geofenceTablesDAO;

    public void save(MultipartFile file,String[] tableMetaData) {
        try {

           // geofenceTablesDAO.create();
          //  List<Tutorial> tutorials = CSVHelper.csvMigrate(file.getInputStream(),tableMetaData);
          //  repository.saveAll(tutorials);
        } catch (Exception e) {
            throw new RuntimeException("fail to store csv data: " + e.getMessage());
        }
    }


}
