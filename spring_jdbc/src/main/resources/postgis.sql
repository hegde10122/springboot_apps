-- Database: geometry_objects

-- DROP DATABASE IF EXISTS geometry_objects;

CREATE DATABASE geometry_objects
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'C'
    LC_CTYPE = 'C'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1
    IS_TEMPLATE = False;

COMMENT ON DATABASE geometry_objects
    IS 'this db is used for postgis geometry objects POC';
 
 CREATE EXTENSION postgis;


 CREATE TABLE animals(gid BIGSERIAL PRIMARY KEY, name VARCHAR(255) NOT NULL,points GEOMETRY(POINT,4326) NOT NULL);
 
 INSERT INTO animals(name,points) VALUES('GIS_DEPT6','SRID=4326;POINT(19.52 73.11)');
 
 SELECT * FROM animals;
 
 SELECT ST_AsText(points)
  FROM animals
  WHERE name LIKE '%GIS_DEPT%';
 
 CREATE INDEX idx_geometry 
ON animals(points);

