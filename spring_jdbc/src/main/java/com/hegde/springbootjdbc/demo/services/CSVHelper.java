package com.hegde.springbootjdbc.demo.services;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class CSVHelper {

    private static final Logger logger = LogManager.getLogger(CSVHelper.class);
    public static String TYPE = "text/csv";
    static String[] HEADERS;
    static String TABNAME;

    public static boolean isMetaDataCorrect(String jsonObject) {

        boolean isMetaDataCorrect;
        StringBuilder sqlQuery = new StringBuilder();
        //json object input issues --- throw the error message and stop
        if(jsonObject == null || jsonObject.isEmpty()){
            String error = "CSV file table metadata information not found -- jsonObject";
            logger.error(error);
           /* throw new InvalidBusinessException(error,
                    ResponseStatusEnum.INVALID_DATA);*/
        }

        try{
            JSONObject jsonObject1 = new JSONObject(jsonObject.trim());

            if(!checkImportantKeys(jsonObject1,"geofence")) return false;
            if(!checkImportantKeys(jsonObject1,"wkid")) return false;
            if(!checkImportantKeys(jsonObject1,"wktext")) return false;
            if(!checkImportantKeys(jsonObject1,"geometry_type")) return false;
            if(!checkImportantKeys(jsonObject1,"table_columns")) return false;
            if(!checkImportantKeys(jsonObject1,"unique_columns")) return false;

            //this is for error generation
            StringBuilder error = new StringBuilder();

            //get the keys ---- we need geofence key for the table name
            //wkid or wktext ---- spatial reference value....only one of them will be present. both are not required

            String space = " ";
            String spatialReference;

            if(!jsonObject1.get("wkid").toString().trim().isEmpty())
                spatialReference = jsonObject1.get("wkid").toString();
            else
                spatialReference = jsonObject1.get("wktext").toString().trim();

            //this is for query builder--- we create a table from the column information and then add the data from the csv

            sqlQuery.append("CREATE TABLE IF NOT EXISTS ");
            sqlQuery.append(jsonObject1.get("geofence").toString().toLowerCase()).append("("); //append the opening bracket
            sqlQuery.append("gid BIGSERIAL PRIMARY KEY,").append(space); //this is the postgis unique key
            sqlQuery.append("shape GEOMETRY(").append(jsonObject1.get("geometry_type").toString().toUpperCase()); //this is for the shape geometry
            sqlQuery.append(",").append(spatialReference).append("),").append(space);

            //table_columns ---- these are the keys for table columns with their types and nullability defined
            JSONObject tabColumnsJson = (JSONObject) jsonObject1.get("table_columns");

            int size = tabColumnsJson.names().length();
            HEADERS = new String[size]; //column header names for the table
            TABNAME = jsonObject1.get("geofence").toString().toLowerCase(); //table name

            for(int i = 0; i < size;i++){
                String key =  tabColumnsJson.names().getString(i); //get all the keys names and find out the size
                String value = tabColumnsJson.get(key).toString(); //for every key process

                if(key.equalsIgnoreCase("shape"))continue;

                //break the values using the : delimiter
                String data[] = value.split(":");

                //data type + null or Not null is captured
                String type = data[0];
                type = type.toLowerCase();
                String nullability = data[1];

                //add the column name
                sqlQuery.append(key.toLowerCase()).append(space);

                //add the type followed by nullability
                if (type.equals("string")) {
                    sqlQuery.append("VARCHAR(255)").append(space);
                }
                else if(type.equals("integer")){
                    sqlQuery.append("INTEGER").append(space);
                }else if(type.equals("long")){
                    sqlQuery.append("BIGINT").append(space);
                }else if(type.equals("double")){
                    sqlQuery.append("DECIMAL").append(space);
                }else if(type.equals("datetime")){
                    sqlQuery.append("TIMESTAMP").append(space);
                } else if(type.equals("point") || type.equals("polyline") || type.equals("polygon")){
                    continue;
                }
                else if(type.equals("boolean")){
                    sqlQuery.append("BOOLEAN").append(space);
                }else {
                    error.append("\ntable column ").append(type).append(" not known");
                    isMetaDataCorrect = false;
                    logger.error(" JSON metadata incorrect " +error);

                    /*throw new InvalidBusinessException(" JSON metadata incorrect "+error,
                            ResponseStatusEnum.INVALID_DATA);*/
                    return isMetaDataCorrect;
                }
                if (nullability.equalsIgnoreCase("notnull")) sqlQuery.append("NOT NULL");
                if(i < size - 1)
                    sqlQuery.append(",").append(space);
                else{

                    String uniqueCols = jsonObject1.get("unique_columns").toString();
                    String[] tokens = uniqueCols.split(":");

                    sqlQuery.append(", CONSTRAINT "); //add constraint name syntax

                    for(int k = 0; k < tokens.length; k++){

                        sqlQuery.append(tokens[k]);

                        if(k < tokens.length - 1)
                          sqlQuery.append("_");
                    }

                    sqlQuery.append(space).append(",");
                    sqlQuery.append(" UNIQUE("); //adding unique columns syntax

                    for(int k = 0; k < tokens.length; k++){
                        sqlQuery.append(tokens[k]);

                        if(k < tokens.length - 1)
                            sqlQuery.append(",");
                    }

                    sqlQuery.append(")");
                    sqlQuery.append(");"); //end the query generation code syntax here
                }
            }

        } catch (Exception e) {
            logger.error(HttpStatus.BAD_REQUEST + " JSON metadata incorrect "+e.getMessage());
           /* throw new InvalidBusinessException(HttpStatus.BAD_REQUEST + " JSON metadata incorrect "+e.getMessage(),
                    ResponseStatusEnum.INVALID_DATA);*/
        }

        System.out.println(sqlQuery);
        isMetaDataCorrect = true;
        return isMetaDataCorrect;
    }

    private static boolean checkImportantKeys(JSONObject jsonObject1, String key) {

        if(!jsonObject1.has(key)){

            logger.error(HttpStatus.BAD_REQUEST + "Jsonobject table metadata information missing key -- "+key);
           /* throw new InvalidBusinessException(HttpStatus.BAD_REQUEST + " Jsonobject table metadata information missing key -- "+key,
                    ResponseStatusEnum.INVALID_DATA);*/

            System.out.println("Jsonobject table metadata information missing key -- "+key);
            return false;
        }

        return true;
    }

    public static boolean hasCSVFormat(MultipartFile file) {

        if (!TYPE.equals(file.getContentType())) {
            return false;
        }
         return true;
    }

    public static boolean isFileDataValid(MultipartFile file, String tableInfo) {

        try
        {

            JSONObject jsonObject1 = new JSONObject(tableInfo.trim());

            //table_columns ---- these are the keys for table columns with their types and nullability defined
            JSONObject tabColumnsJson = (JSONObject) jsonObject1.get("table_columns");




            BufferedReader fileReader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));


             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withHeader(HEADERS).withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim()))

            //  List<Tutorial> tutorials = new ArrayList<Tutorial>();

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord csvRecord : csvRecords) {



                Tutorial tutorial = new Tutorial(
                        Long.parseLong(csvRecord.get("Id")),
                        csvRecord.get("Title"),
                        csvRecord.get("Description"),
                        Boolean.parseBoolean(csvRecord.get("Published"))
                );

                tutorials.add(tutorial);
            }

            return tutorials;

        } catch (Exception e) {
            throw new InvalidBusinessException("CSV file parsing failed "+e.getMessage(),
                    ResponseStatusEnum.INVALID_DATA);
        }


        return true;
    }

   /* public static List<Tutorial> csvMigrate(InputStream is, String[] tableMetaData) {

        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withHeader(tableMetaData).withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {

          //  List<Tutorial> tutorials = new ArrayList<Tutorial>();

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord csvRecord : csvRecords) {
                Tutorial tutorial = new Tutorial(
                        Long.parseLong(csvRecord.get("Id")),
                        csvRecord.get("Title"),
                        csvRecord.get("Description"),
                        Boolean.parseBoolean(csvRecord.get("Published"))
                );

                tutorials.add(tutorial);
            }

            return tutorials;

        } catch (Exception e) {
            throw new InvalidBusinessException("CSV file parsing failed "+e.getMessage(),
                    ResponseStatusEnum.INVALID_DATA);
        }

      *//*
 *//*
    }*/
}
