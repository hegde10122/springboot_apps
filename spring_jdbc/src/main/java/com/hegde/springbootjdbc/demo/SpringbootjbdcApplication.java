package com.hegde.springbootjdbc.demo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.hegde.springbootjdbc.demo.dao.EmployeeDao;
import com.hegde.springbootjdbc.demo.json.Employee;
import com.hegde.springbootjdbc.demo.json.ObjectsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@SpringBootApplication
public class SpringbootjbdcApplication { // implements CommandLineRunner

	@Autowired
	private EmployeeDao employeeDao;

	@Autowired
	static
	ResourceLoader resourceLoader;



	private static final Logger logger = LoggerFactory.getLogger(SpringbootjbdcApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(SpringbootjbdcApplication.class, args);
	//	readFile();
		//readJsonWithObjectMapper();
		//readjsonWithSimpleBinding();

	//	readJsontreeNode();

		writeJsonJackson(readJsonWithObjectMapper());
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







	/*@Override
	public void run(String... args) throws Exception {
		employeeDao.createTable();
	}*/
}
