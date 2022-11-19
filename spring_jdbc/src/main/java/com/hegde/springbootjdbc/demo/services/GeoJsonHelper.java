package com.hegde.springbootjdbc.demo.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.geojson.*;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GeoJsonHelper {

    String[] HEADERS;

    String TABNAME;

    static final String SPACE  = " ";

    public String getTABNAME() {
        return TABNAME;
    }

    public void setTABNAME(String TABNAME) {
        this.TABNAME = TABNAME;
    }

    public String getCREATE_QUERY() {
        return CREATE_QUERY;
    }

    public List<HashMap<String, Object>> getTableFeatures() {
        return tableFeatures;
    }

    public void setTableFeatures(List<HashMap<String, Object>> tableFeatures) {
        this.tableFeatures = tableFeatures;
    }

    public void setCREATE_QUERY(String CREATE_QUERY) {
        this.CREATE_QUERY = CREATE_QUERY;
    }

    String CREATE_QUERY;

    StringBuilder DATAERRORS;

    List<HashMap<String,Object>> tableFeatures;

    Long totalGoodRecords;
    Long totalBadRecords;

    private static final Logger logger = LogManager.getLogger(GeoJsonHelper.class);

    static String TYPE = "application/geo+json";
    public boolean jsonFromFile(MultipartFile file)
    {

        if (!TYPE.equals(file.getContentType())) {
            return false;
        }
        return true;
    }


    public boolean readJsonFile(MultipartFile geoJsonfile, String tableInfo) {

        long start = System.currentTimeMillis();

        System.out.println("");

        ObjectMapper om = new ObjectMapper();
        totalGoodRecords = totalBadRecords = 0L;
        try{
            JSONObject jsonObject1 = new JSONObject(tableInfo.trim());
            String tabColumnsString = jsonObject1.get("table_columns").toString().trim();
            JSONObject tabColumnsJson = new JSONObject(tabColumnsString);

            BufferedReader fileReader = new BufferedReader(new InputStreamReader(geoJsonfile.getInputStream(), StandardCharsets.UTF_8));
            String result = fileReader
                    .lines().parallel().collect(Collectors.joining("\n"));

            Map<String, Object> resultMap = om.readValue(result,
                    new TypeReference<Map<String, Object>>() {});
            List<Feature> features = om.convertValue(resultMap.get("features"), new TypeReference<List<Feature>>() {});

            List<HashMap<String,Object>> tableFeatures = new ArrayList<>();

            String spatialReference;

            if(!jsonObject1.get("wkid").toString().trim().isEmpty())
                spatialReference = jsonObject1.get("wkid").toString();
            else
                spatialReference = jsonObject1.get("wktext").toString().trim();

            features.stream().parallel().forEachOrdered(
                    feature ->
                    {
                        HashMap<String,Object> properties = (HashMap<String, Object>) feature.getProperties();

                        StringBuilder geometryBuilder = new StringBuilder(); //create the geometry string to be inserted into the table of postgis
                        boolean isRecordOK = true;
                        DATAERRORS = new StringBuilder();

                        GeoJsonObject geometry = feature.getGeometry();
                        StringBuilder makeString;
                        try{

                            for(int i = 0; i < HEADERS.length;i++) {

                                //Drop table columns not required or not mentioned in the table information json input string --- using continue
                               properties =  dropColumns(tabColumnsJson,properties);

                                String key =  HEADERS[i]; //get all the keys names

                                String value = tabColumnsJson.opt(key).toString(); //for every key process

                                //break the values using the : delimiter
                                String data[] = value.split(":");

                                //data type + null or Not null is captured----we use data type only here
                                String type = data[0];
                                type = type.toLowerCase();

                                Object record = properties.get(key);

                                if(type.equals("string")){

                                    try{
                                        if(properties.get(key)!=null)
                                        {
                                            String c = String.valueOf(record);
                                            makeString = new StringBuilder();
                                            makeString.append("'");
                                            properties.put(key,makeString.append(c).append("'"));

                                        }
                                    }catch (Exception e){
                                        isRecordOK = false;
                                        DATAERRORS.append("RECORD ").append(properties.get("OBJECTID"))
                                                .append(SPACE).append("Error key")
                                                .append(SPACE).append(key).append("String expected");
                                    }
                                }else if(type.equals("integer") || type.equals("long")){

                                    try{
                                        if(record!=null)
                                        {
                                            Long c = Long.valueOf(String.valueOf(record));
                                            properties.put(key,c);
                                        }
                                    }catch (Exception e){
                                        isRecordOK = false;
                                        DATAERRORS.append("RECORD ").append(properties.get("OBJECTID"))
                                                .append("Error key").append(SPACE).append(key).append("Long/Integer expected");
                                    }

                                }
                                else if(type.equals("double")){
                                    try{
                                        if(record!=null)
                                        {
                                            Double c = Double.valueOf(String.valueOf(record));
                                            properties.put(key,c);
                                        }
                                    }catch (Exception e){
                                        isRecordOK = false;
                                        DATAERRORS.append("RECORD ").append(properties.get("OBJECTID"))
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
                                            Date result2 = new Date(Long.parseLong(String.valueOf(record)));
                                            // Date dates = formatter1.parse(record.toString());
                                            makeString = new StringBuilder();
                                            makeString.append("'");
                                            properties.put(key, makeString.append(formatter1.format(result2)).append("'"));

                                        }
                                    }catch (Exception e){
                                        isRecordOK = false;
                                        DATAERRORS.append("RECORD ").append(properties.get("OBJECTID"))
                                                .append(SPACE)
                                                .append("Error key").append(SPACE).append(key).append(SPACE)
                                                .append(key).append("Date time issues ");
                                    }
                                }
                                else if(type.equals("point") || type.equals("polyline") || type.equals("polygon") || type.equals("multipolygon")){

                                    if(record!=null && !record.toString().toLowerCase().contains(type)){
                                        isRecordOK = false;
                                        DATAERRORS.append("RECORD ").append(properties.get("OBJECTID"))
                                                .append(SPACE)
                                                .append("Error key").append(SPACE)
                                                .append(key).append(SPACE)
                                                .append(type).append(" geometry expected ").append(record);
                                    }

                                }
                                else if(type.equals("enum")){
                                    String enumValues = jsonObject1.get(key).toString();
                                    String[] enumData = enumValues.split(":");

                                    if(record!=null && !Arrays.asList(enumData).contains(record.toString())){
                                        isRecordOK = false;
                                        DATAERRORS.append("RECORD ").append(properties.get("OBJECTID"))
                                                .append(SPACE)
                                                .append("Error key").append(SPACE)
                                                .append(key).append(SPACE)
                                                .append("ENUM expected from values ")
                                                .append(Arrays.toString(enumData));
                                    }
                                    else{
                                        properties.put(key,record);
                                    }

                                }
                                else if(type.equals("boolean")){
                                    try{
                                        if(record!=null)
                                        {
                                            Boolean b = (Boolean) record;
                                            properties.put(key,b);
                                        }
                                    }catch (Exception e){
                                        isRecordOK = false;
                                        DATAERRORS.append("RECORD ").append(properties.get("OBJECTID"))
                                                .append(SPACE)
                                                .append("Error key").append(SPACE)
                                                .append(key).append("true or false expected ");
                                    }
                                }

                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        if(geometry instanceof MultiPolygon) {
                            MultiPolygon multiPolygon = (MultiPolygon) geometry;

                            // MULTIPOLYGON(( (0 0,4 0,4 4,0 4,0 0),(1 1,2 1,2 2,1 2,1 1)), ((-1 -1,-1 -2,-2 -2,-2 -1,-1 -1)))

                          //reading the array of polygons with the key "coordinates"
                          List<List<List<LngLatAlt>>> coordinatesArray =  multiPolygon.getCoordinates();

                            //System.out.println("HALWA total "+coordinatesArray.size());

                           //we will process each polygon sequentially
                            geometryBuilder.append("MULTIPOLYGON(");

                            for(int polygons = 0; polygons < coordinatesArray.size();polygons++){

                                //get the polygon and now scan every linestring
                                //System.out.println("POLY "+(polygons+1));

                                geometryBuilder.append("("); //beginning of each polygon 1,2,3.......

                                //get the linestrings
                                for(int linestring = 0; linestring < coordinatesArray.get(polygons).size();linestring++){

                                   // System.out.print("POLY "+(polygons+1)+" linestring "+(linestring+1) +"\t");

                                    geometryBuilder.append("("); //start the linestring
                                    for(int points = 0; points < coordinatesArray.get(polygons).get(linestring).size();points++){

                                      //  System.out.println("POLY "+(polygons+1)+" linestring "+(linestring+1)+" POINT "+(points +1)+"\t");

                                        //append the lat,lon coordinates---longitude followed by latitude--separated by space
                                         geometryBuilder.append(coordinatesArray.get(polygons).get(linestring).get(points).getLongitude()).append(" ");
                                         geometryBuilder.append(coordinatesArray.get(polygons).get(linestring).get(points).getLatitude());

                                        if(points !=  coordinatesArray.get(polygons).get(linestring).size() - 1)
                                            geometryBuilder.append(","); //append the point with a comma
                                        else
                                            geometryBuilder.append(")"); //complete the point list for linestring
                                    }

                                    if(linestring !=  coordinatesArray.get(polygons).size() - 1)
                                        geometryBuilder.append(","); //append the linestring with a comma
                                    else
                                        geometryBuilder.append(")"); //complete the linestring
                                    }

                                if(polygons != coordinatesArray.size() - 1)
                                    geometryBuilder.append(",");
                                else
                                    geometryBuilder.append(")");
                            }
                            //end the multi polygon
                          //  geometryBuilder.append(")");

                        }else

                        if(geometry instanceof Polygon){
                            Polygon polygon = (Polygon) geometry;

                            //reading the array of polygons with the key "coordinates"
                             List<List<LngLatAlt>> coordinatesArray =  polygon.getCoordinates();

                            //we will process each polygon sequentially
                            geometryBuilder.append("POLYGON(");

                            for(int linestring = 0; linestring < coordinatesArray.size();linestring++) {

                                geometryBuilder.append("("); //beginning of each linestring

                                for(int points = 0; points < coordinatesArray.get(linestring).size();points++){

                                    //append the lat,lon coordinates---longitude followed by latitude--separated by space
                                    geometryBuilder.append(coordinatesArray.get(linestring).get(points).getLongitude()).append(" ");
                                    geometryBuilder.append(coordinatesArray.get(linestring).get(points).getLatitude());

                                    if(points !=  coordinatesArray.get(linestring).size() - 1)
                                        geometryBuilder.append(","); //append the point with a comma
                                    else
                                        geometryBuilder.append(")"); //complete the linestring
                                }

                                if(linestring != coordinatesArray.size() - 1)
                                    geometryBuilder.append(","); //append the point with a comma
                                else
                                    geometryBuilder.append(")"); //complete the linestring and end the polygon
                            }

                            //end the polygon
                            //geometryBuilder.append(")");

                            //POLYGON((0 0, 10 0, 10 10, 0 10, 0 0),(1 1, 1 2, 2 2, 2 1, 1 1))
                            //MULTIPOLYGON(( (0 0,4 0,4 4,0 4,0 0),(1 1,2 1,2 2,1 2,1 1)),((-1 -1,-1 -2,-2 -2,-2 -1,-1 -1)))
                        }

                        if(isRecordOK){
                            totalGoodRecords+= 1;
                            //append the shape geometry and add to the arraylist
                            properties.put("SHAPE","ST_GeomFromText('" +geometryBuilder+"',"+spatialReference+")");
                            tableFeatures.add(properties);
                            setTableFeatures(tableFeatures);
                        }else{
                            totalBadRecords+=1;
                        }
                    }
            );

long end = System.currentTimeMillis();
            System.out.println((end - start)+" time to read");


        }catch (Exception e){
            e.printStackTrace();
            e.getMessage();
        }

        //create the insert statement here
        return true;
    }

    private HashMap<String, Object> dropColumns(JSONObject tabColumnsJson, HashMap<String, Object> properties) throws JSONException {

        if(tabColumnsJson == null || properties == null) return null;

        // Get the iterator over the HashMap
        Iterator<Map.Entry<String, Object>> iterator = properties.entrySet().iterator();

        ArrayList<String> columnKeys = new ArrayList<>(tabColumnsJson.names().length());

        for (int i = 0; i < tabColumnsJson.names().length();i++){
            columnKeys.add(tabColumnsJson.names().getString(i));
            }


        // Iterate over the HashMap
        while (iterator.hasNext()) {

            Map.Entry<String , Object> entry = iterator.next();

            String keyCheck = entry.getKey();

            if(!columnKeys.contains(keyCheck)){
               // System.out.println("removed "+keyCheck);
                iterator.remove();
            }
        }

        return  properties;
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

    //check metadata json and if valid then create the query for table creation...else return error
    public boolean isMetaDataCorrect(String jsonObject) {

        boolean isMetaDataCorrect;
        StringBuilder sqlQuery = new StringBuilder();
        //json object input issues --- throw the error message and stop
        if(jsonObject == null || jsonObject.isEmpty()){
            String error = "geojson file table metadata information not found -- jsonObject";
            logger.error(error);
            return false;
           /* throw new InvalidBusinessException(error,
                    ResponseStatusEnum.INVALID_DATA);*/
        }

        try {
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
            sqlQuery.append("shape GEOMETRY"); //this is for the shape geometry
            sqlQuery.append(",").append(space);//Geometry type (Polygon) does not match column type (GeometryCollection)

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

                //unique columns --- add UNIQUE keyword
                String uniqueCols = jsonObject1.get("unique_columns").toString();
                String[] tokens = uniqueCols.split(":");


                //add the column name
                sqlQuery.append(key.toUpperCase()).append(space);

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

                if(Arrays.asList(tokens).contains(key))sqlQuery.append(" UNIQUE");

                if(i < size - 1)
                    sqlQuery.append(",").append(space);
                else{


                   // sqlQuery.append(", CONSTRAINT "); //add constraint name syntax

                    /*for(int k = 0; k < tokens.length; k++){

                        if(tokens[k].equalsIgnoreCase("shape"))continue;

                        sqlQuery.append(tokens[k]);
                        if(k < tokens.length - 1)
                            sqlQuery.append("_");
                    }

                    sqlQuery.append(space);*/

                    sqlQuery.append(");"); //end the query generation code syntax here

                    //create the empty table using the sql query
                    CREATE_QUERY = sqlQuery.toString();
                }
            }
        }catch (Exception e){
            logger.error(HttpStatus.BAD_REQUEST + " JSON metadata incorrect for geojson file"+e.getMessage());
           /* throw new InvalidBusinessException(HttpStatus.BAD_REQUEST + " JSON metadata incorrect for geojson file"+e.getMessage(),
                    ResponseStatusEnum.INVALID_DATA);*/
        }
        return true;
    }
}
