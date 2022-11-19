package com.hegde.springbootjdbc.demo.services;

import com.hegde.springbootjdbc.demo.dao.GeofenceTablesDAOImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Service
public class GeoJsonService {

    @Autowired
    GeofenceTablesDAOImpl geofenceTablesDAO;


    @Autowired
    GeoJsonHelper geoJsonHelper;


    public String gsonHelper( MultipartFile file, String tableInfo) {

        boolean isFileValid = geoJsonHelper.jsonFromFile(file);

        if (!isFileValid)
            return "File does not have geojson data";

        else {

            boolean isMetaDataCorrect = geoJsonHelper.isMetaDataCorrect(tableInfo);


            if (isMetaDataCorrect) {

                geofenceTablesDAO.create(geoJsonHelper.getCREATE_QUERY());//create the table
                geoJsonHelper.readJsonFile(file, tableInfo); //read the geojson file and check all features for errors

                //if no errors then insert those records ---- partially insert also allowed
                geofenceTablesDAO.addRows(geoJsonHelper.getTableFeatures(),geoJsonHelper.getTABNAME());


            }

        }

        return "";
    }

    public void save(MultipartFile file, String[] tableMetaData) {
        try {

           // geofenceTablesDAO.create();
            //  List<Tutorial> tutorials = CSVHelper.csvMigrate(file.getInputStream(),tableMetaData);
            //  repository.saveAll(tutorials);
        } catch (Exception e) {
            throw new RuntimeException("fail to store csv data: " + e.getMessage());
        }
    }
}
