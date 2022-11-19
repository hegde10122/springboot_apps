package com.hegde.springbootjdbc.demo.services;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
public class CSVHelper {

    private static final Logger logger = LogManager.getLogger(CSVHelper.class);
    static String TYPE = "text/csv";
    String[] HEADERS;
    String TABNAME;

    private static final String SPACE = " ";

     String QUERY;

     StringBuilder DATAERRORS;

     Long totalGoodRecords;
     Long totalBadRecords;

     ArrayList<Object> listRecords;

    public boolean isMetaDataCorrect(String jsonObject) {

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
                HEADERS[i] = key;
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
                    sqlQuery.append("TIMESTAMPTZ").append(space);
                } else if(type.equals("point") || type.equals("polyline") || type.equals("polygon") || type.equals("multipolygon")){
                    continue;
                }
                else if(type.equals("enum")){

                    if(!checkImportantKeys(jsonObject1,key)) return false;

                    String enumValues = jsonObject1.get(key).toString();
                    String[] enumData = enumValues.split(":");

                    if(enumData.length == 0) return false;

                    sqlQuery.append("ENUM (");
                        for(int d = 0; d < enumData.length; d++){
                            sqlQuery.append("'").append(enumData[d]).append("'");

                        if(d != enumData.length - 1)
                            sqlQuery.append(",");
                        sqlQuery.append(space);
                    }
                    sqlQuery.append(")");
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

                    //create the empty table using the sql query
                    QUERY = sqlQuery.toString();

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

    private boolean checkImportantKeys(JSONObject jsonObject1, String key) {

        if(!jsonObject1.has(key)){

            logger.error(HttpStatus.BAD_REQUEST + "Jsonobject table metadata information missing key -- "+key);
           /* throw new InvalidBusinessException(HttpStatus.BAD_REQUEST + " Jsonobject table metadata information missing key -- "+key,
                    ResponseStatusEnum.INVALID_DATA);*/

            System.out.println("Jsonobject table metadata information missing key -- "+key);
            return false;
        }

        return true;
    }

    public boolean hasCSVFormat(MultipartFile file) {

        if (!TYPE.equals(file.getContentType())) {
            return false;
        }
         return true;
    }

    public boolean isFileDataValid(MultipartFile file, String tableInfo) {
        final int[] counter = {0};
        listRecords = new ArrayList<>();
        totalGoodRecords = totalBadRecords = 0L;
        try
        {

            //use this class to read the csv file
            BufferedReader fileReader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));

            //parse the first line with the parser to get the headers
            CSVParser csvParser = new CSVParser(fileReader,
                    CSVFormat.DEFAULT.withHeader(HEADERS).withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());

            //get the table columns
            JSONObject jsonObject1 = new JSONObject(tableInfo.trim());

            DATAERRORS = new StringBuilder();

            //table_columns ---- these are the keys for table columns with their types and nullability defined
            JSONObject tabColumnsJson = (JSONObject) jsonObject1.get("table_columns");

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            Stream<CSVRecord> csvStream = StreamSupport.stream(csvRecords.spliterator(),true);

            csvStream.parallel().forEach(csvRecord ->{
                boolean isRecordOK = true;
                counter[0]++;
                ArrayList recordAdd = new ArrayList<Object>();
                try{
                    for(int i = 0; i < HEADERS.length;i++){

                        String key =  HEADERS[i]; //get all the keys names
                        String value = tabColumnsJson.get(key).toString(); //for every key process

                        //break the values using the : delimiter
                        String data[] = value.split(":");

                        //data type + null or Not null is captured----we use data type only here
                        String type = data[0];
                        type = type.toLowerCase();

                        Object record = csvRecord.get(key);

                        if(type.equals("string")){

                            try{
                                if(record!=null)
                                {
                                    String c = String.valueOf(record);
                                    recordAdd.add(c);
                                }
                            }catch (Exception e){
                                isRecordOK = false;
                                DATAERRORS.append("RECORD ").append(csvRecord.getRecordNumber())
                                        .append(SPACE).append("Error key")
                                        .append(SPACE).append(key).append("String expected");
                            }
                        }else if(type.equals("integer") || type.equals("long")){

                            try{
                                if(record!=null)
                                {
                                    Long c = Long.parseLong(record.toString());
                                    recordAdd.add(c);
                                }
                            }catch (Exception e){
                                isRecordOK = false;
                                DATAERRORS.append("RECORD ").append(csvRecord.getRecordNumber())
                                        .append("Error key").append(SPACE).append(key).append("Long/Integer expected");
                            }

                        }
                        else if(type.equals("double")){
                            try{
                                if(record!=null)
                                {
                                    Double c = Double.parseDouble(record.toString());
                                    recordAdd.add(c);
                                }
                            }catch (Exception e){
                                isRecordOK = false;
                                DATAERRORS.append("RECORD ").append(csvRecord.getRecordNumber())
                                        .append(SPACE)
                                        .append("Error key").append(key).append(SPACE).append("Decimal number expected");
                            }
                        }
                        else if(type.equals("datetime")){

                            try{
                                if(record!=null)
                                {

                                    // Creating date format
                                    DateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");

                                    // Creating date from milliseconds
                                    // using Date() constructor
                                    Date result = new Date(Long.parseLong(record.toString()));
                                    recordAdd.add(formatter1.parse(record.toString()));

                                }
                            }catch (Exception e){
                                isRecordOK = false;
                                DATAERRORS.append("RECORD ").append(csvRecord.getRecordNumber())
                                        .append(SPACE)
                                        .append("Error key").append(SPACE).append(key).append(SPACE)
                                        .append(csvRecord.get(key)).append("Date time found ");
                            }
                        }
                        else if(type.equals("point") || type.equals("polyline") || type.equals("polygon") || type.equals("multipolygon")){

                            if(record!=null && !record.toString().toLowerCase().contains(type)){
                                isRecordOK = false;
                                DATAERRORS.append("RECORD ").append(csvRecord.getRecordNumber())
                                        .append(SPACE)
                                        .append("Error key").append(SPACE)
                                        .append(key).append(SPACE)
                                        .append(type).append(" geometry expected ").append(record);

                            }
                            else
                                recordAdd.add(record);
                        }
                        else if(type.equals("enum")){
                            String enumValues = jsonObject1.get(key).toString();
                            String[] enumData = enumValues.split(":");

                            if(record!=null && !Arrays.asList(enumData).contains(record.toString())){
                                isRecordOK = false;
                                DATAERRORS.append("RECORD ").append(csvRecord.getRecordNumber())
                                        .append(SPACE)
                                        .append("Error key").append(SPACE)
                                        .append(key).append(SPACE)
                                        .append("ENUM expected from values ")
                                        .append(Arrays.toString(enumData));
                            }
                            else
                                recordAdd.add(record);
                        }
                        else if(type.equals("boolean")){
                            try{
                                if(record!=null)
                                {
                                    Boolean b = Boolean.parseBoolean(record.toString());
                                    recordAdd.add(b);
                                }
                            }catch (Exception e){
                                isRecordOK = false;
                                DATAERRORS.append("RECORD ").append(csvRecord.getRecordNumber())
                                        .append(SPACE)
                                        .append("Error key").append(SPACE)
                                        .append(key).append("true or false expected ");
                            }
                        }
                    }
                    if(isRecordOK){
                        totalGoodRecords+= 1;
                        listRecords.add(recordAdd);
                    }else{
                        totalBadRecords+=1;
                    }
                }catch (Exception e){
                  /*  throw new InvalidBusinessException("CSV file parsing failed "+e.getMessage(),
                            ResponseStatusEnum.INVALID_DATA);*/
                    e.printStackTrace();
                }

            });
        } catch (Exception e) {
           /* throw new InvalidBusinessException("CSV file parsing failed "+e.getMessage(),
                    ResponseStatusEnum.INVALID_DATA);*/
            e.printStackTrace();
        }
        System.out.println("TOO "+totalGoodRecords+" "+totalBadRecords);
        return totalGoodRecords == counter[0];
    }

}
