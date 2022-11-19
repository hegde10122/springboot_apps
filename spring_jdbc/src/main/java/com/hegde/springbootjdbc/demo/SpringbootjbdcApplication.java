package com.hegde.springbootjdbc.demo;

import com.bedatadriven.jackson.datatype.jts.JtsModule;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.DeserializationFeature;
import org.geojson.FeatureCollection;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;

import com.bedatadriven.jackson.datatype.jts.parsers.*;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.locationtech.jts.geom.*;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


@SpringBootApplication
@EnableJpaRepositories("com.hegde.springbootjdbc.demo.repo")
public class SpringbootjbdcApplication { // implements CommandLineRunner

	public static void main(String[] args) throws IOException {
		SpringApplication.run(SpringbootjbdcApplication.class, args);

		//readGeometryCollection();

		//checkTime();

		readJsonJackson();

	}

	private static void readJsonJackson() {

		ObjectMapper objectMapper = new ObjectMapper();

		//objectMapper.readValue()

		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
		objectMapper.configure(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS, false);

	}

	private static JsonFactory factory = new JsonFactory();
	private static void readGeometryCollection() throws IOException {

		/*GeometryFactory gf = new GeometryFactory();
		File f = new File("/resources/rome.geojson");


		GenericGeometryParser parser = new GenericGeometryParser(gf);
		try (JsonParser lParser = factory.createParser(new FileInputStream(f))) {
			ObjectMapper mapper = new ObjectMapper();
			mapper.registerModule(new JtsModule());
			ObjectNode node = mapper.readTree(lParser);
			GeometryParser<Geometry> gParser = new GenericGeometryParser(gf);
			Geometry g = gParser.geometryFromJson(node);
			System.out.println(g);
		} catch (IOException e) {
			throw new RuntimeException("problem parsing Geometry", e);
		}*/
	}


	private static void checkTime() {

		// Creating date format
		DateFormat simple = new SimpleDateFormat("dd-MMMM-yyyy HH:mm:ss:SSS Z");

		// Creating date from milliseconds
		// using Date() constructor
		Date result = new Date(1424364422000L);

		// Formatting Date according to the
		// given format
		System.out.println(simple.format(result));

	}

}
