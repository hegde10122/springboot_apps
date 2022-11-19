package com.hegde.springbootjdbc.demo.controllers;

import com.hegde.springbootjdbc.demo.services.CSVHelper;
import com.hegde.springbootjdbc.demo.services.CSVService;
import com.hegde.springbootjdbc.demo.services.GeoJsonHelper;
import com.hegde.springbootjdbc.demo.services.GeoJsonService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;



@RestController
public class GeoJsonController {


    private static final Logger logger = LogManager.getLogger(CSVController.class);

    @Autowired
    GeoJsonService geoJsonService; //service class that interacts with repository class to save geojson data to the DB

    @PostMapping(value = "/uploadjson",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody String uploadGeoJson(@RequestPart("file") MultipartFile file, @RequestPart String tableInfo) {

        String message;

        //call the service class and pass on the file and json information for table creation
        geoJsonService.gsonHelper(file,tableInfo);

        return "";
        }

    }
