package com.hegde.springbootjdbc.demo.json;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.hegde.springbootjdbc.demo.SpringbootjbdcApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.hegde.springbootjdbc.demo.dao.EmployeeDao;
import com.hegde.springbootjdbc.demo.json.Employee;
import com.hegde.springbootjdbc.demo.json.ObjectsMapper;
import com.hegde.springbootjdbc.demo.json.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;


public class JacksonRunner {

    public static void main(String[] args) {

        //	readFile();
        //readJsonWithObjectMapper();
        //readjsonWithSimpleBinding();

        //	readJsontreeNode();

        //	writeJsonJackson(readJsonWithObjectMapper());

        //writeJacksonJsonGenerator();
        //parseJsonJackson();

        //parseJsonArray();
        //jsonArrayToListMap();
        //configureSerializer();
    }


    private static void configureSerializer() {

        ObjectMapper objectMapper = new ObjectMapper();

        String jsonString = "{\"name\" : \"Hegde\", \"gender\" : \"Male\",\"address\" : \"Mumbai\" }";

        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS, false);

        try {
            Student student = objectMapper.readValue(jsonString, Student.class);
            JsonNode jsonNodeRoot = objectMapper.readTree(jsonString);
            JsonNode jsonNodeYear = jsonNodeRoot.get("address");
            String address = jsonNodeYear.asText();
            System.out.println(address);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private static final Logger logger = LoggerFactory.getLogger(JacksonRunner.class);

    public class CustomStudentSerializer extends StdSerializer<Student> {

        protected CustomStudentSerializer() {
            this( null);
        }
        protected CustomStudentSerializer(Class<Student> t) {
            super(t);
        }

        @Override
        public void serialize(Student student, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeStringField("student_surname", student.getName());
            jsonGenerator.writeEndObject();
        }
    }
    private static void jsonArrayToListMap() throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonStudentArray =
                "[{\"name\" : \"Hegde\", \"gender\" : \"Male\" }, { \"name\" : \"Kala\", \"gender\" : \"Female\" }]";
        List<Student> studentList = objectMapper.readValue(jsonStudentArray, new TypeReference<>() {
        });

        String json = "{ \"color\" : \"Red\", \"type\" : \"BMW\" }";

        Map<String, Object> map
                = objectMapper.readValue(json, new TypeReference<Map<String,Object>>(){});

        System.out.println(map);

        studentList.forEach(System.out::println);


    }

    private static void parseJsonArray() {

        String json = "{ \"name\" : \"Chandan\", \"gender\" : \"Male\" }";

        ObjectMapper objectMapper = new ObjectMapper();

        try{
            JsonNode jsonNode = objectMapper.readTree(json);
            String gender = jsonNode.get("gender").asText();
            System.out.println(gender);
        }catch (Exception e){
            e.printStackTrace();
        }



    }

    private static void parseJsonJackson() {

        String json
                = "{\"name\":\"hegde\",\"age\":25,\"address\":[\"Kailash\",\"Lily road\"]}";
        JsonFactory jfactory = new JsonFactory();

        try{
            JsonParser jParser = jfactory.createParser(json);

            String parsedName = null;
            Integer parsedAge = null;
            List<String> addresses = new LinkedList<>();

            while (jParser.nextToken() != JsonToken.END_OBJECT) {
                String fieldname = jParser.getCurrentName();
                if ("name".equals(fieldname)) {
                    jParser.nextToken();
                    parsedName = jParser.getText();
                }

                if ("age".equals(fieldname)) {
                    jParser.nextToken();
                    parsedAge = jParser.getIntValue();
                }

                if ("address".equals(fieldname)) {
                    jParser.nextToken();
                    while (jParser.nextToken() != JsonToken.END_ARRAY) {
                        addresses.add(jParser.getText());
                    }
                }
            }
            jParser.close();
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    private static void writeJacksonJsonGenerator() {

		/*
		 {
	   "name":"hegde",
  		 "age":25,
 	  "address":[
      "Kailash",
      "Beautiful Road"
  		 ]
		}
		  */



        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        JsonFactory jfactory = new JsonFactory();

        try{
            JsonGenerator jGenerator = jfactory
                    .createGenerator(stream, JsonEncoding.UTF8);

            jGenerator.writeStartObject();
            jGenerator.writeStringField("name", "hegde");
            jGenerator.writeNumberField("age", 25);
            jGenerator.writeFieldName("address");
            jGenerator.writeStartArray();
            jGenerator.writeString("Kailash");
            jGenerator.writeString("Beautiful Road");
            jGenerator.writeEndArray();
            jGenerator.writeEndObject();
            jGenerator.close();

            String json = new String(stream.toByteArray(), "UTF-8");
            System.out.println(json);
        }catch (Exception e){

        }


    }

    private static void writeJsonJackson(Employee employee) {

        try{
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonInString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(employee);
            logger.info("Employee JSON is\n" + jsonInString);
            objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
            objectMapper.writeValue(new File("src/main/resources/write_employee.json"), employee);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void readJsontreeNode() {

        try{
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(new File("src/main/resources/employee.json"));

            String prettyPrintEmployee = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
            logger.info(prettyPrintEmployee+"\n");

            JsonNode nameNode=rootNode.path("name");
            String name=nameNode.asText();
            logger.info("\n----------------------------\nEmployee Name: "+name+"\n");

            JsonNode personalInformationNode = rootNode.get("personalInformation");
            HashMap<String, String> personalInformationMap = objectMapper.convertValue(personalInformationNode, HashMap.class);

            Iterator hmIterator = personalInformationMap.entrySet().iterator();

            while(hmIterator.hasNext())
            {
                Map.Entry mapElement
                        = (Map.Entry) hmIterator.next();

                //logger.info("\n"+mapElement.getKey() + "=" + mapElement.getValue());

                System.out.println(mapElement.getKey() + "=" + mapElement.getValue());

                if(mapElement.getValue() instanceof String){
                    System.out.println(mapElement.getValue());
                }
            }

            JsonNode phoneNumbersNode = rootNode.path("phoneNumbers");
            Iterator<JsonNode> elements = phoneNumbersNode.elements();
            while(elements.hasNext()){
                JsonNode phoneNode = elements.next();
                logger.info("\n----------------------------\nPhone Numbers = "+phoneNode.asLong());
            }

        }catch (Exception e){

        }

    }

    private static void readjsonWithSimpleBinding() {

        try{
            ObjectMapper objectMapper = new ObjectMapper();
            HashMap empMap = objectMapper.readValue(new File("src/main/resources/employee.json"), HashMap.class);


            // Getting an iterator
            Iterator hmIterator = empMap.entrySet().iterator();

            while (hmIterator.hasNext())
            {
                Map.Entry mapElement
                        = (Map.Entry) hmIterator.next();

                //logger.info("\n"+mapElement.getKey() + "=" + mapElement.getValue());

                System.out.println(mapElement.getKey() + "=" + mapElement.getValue());

                if(mapElement.getValue() instanceof String){
                    System.out.println(mapElement.getValue());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }



    public static Employee readJsonWithObjectMapper()  {
        Employee emp = new Employee();
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            emp = objectMapper.readValue(new File("src/main/resources/employee.json"), Employee.class);
            logger.info(emp.toString());
        }catch (Exception e){
            e.printStackTrace();
        }

        return emp;
    }

    public static void readFile()  {

        Resource resource = new ClassPathResource("/employee.json", ObjectsMapper.class);

        InputStream inputStream = null;
        try {
            inputStream = resource.getInputStream();
            String string = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            System.out.println(string);
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage()+" KAY RE!!");
        }
    }




}
