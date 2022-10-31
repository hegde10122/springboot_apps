package com.hegde.springbootjdbc.demo.backup;

import com.bedatadriven.jackson.datatype.jts.JtsModule;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Autowired
    private DataSource dataSource;


    @Autowired
    private JobBuilderFactory builderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public FlatFileItemReader<GeometryObjectsModel> flatFileItemReader(){

        FlatFileItemReader<GeometryObjectsModel> flatFileItemReader = new FlatFileItemReader<>();

        flatFileItemReader.setResource(new FileSystemResource("src/main/resources/GEOPOINT_3ROWS.CSV"));

        flatFileItemReader.setLineMapper(getLineMapper());
        flatFileItemReader.setName("CSV reader");
        flatFileItemReader.setLinesToSkip(1);
        return flatFileItemReader;
    }

    private LineMapper<GeometryObjectsModel> getLineMapper() {

        DefaultLineMapper<GeometryObjectsModel> lineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);

        lineTokenizer.setNames("ID","LOCATION_NAME","LOCATION_ADDRESS","LATITUDE","LONGITUDE","SHAPE");

        BeanWrapperFieldSetMapper<GeometryObjectsModel> fieldSetter = new BeanWrapperFieldSetMapper<>();
        fieldSetter.setTargetType(GeometryObjectsModel.class);
        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetter);

        return lineMapper;
    }

    @Bean
    public GeometryItemProcessor processor(){



        return new GeometryItemProcessor();
    }


    @Bean
    //Step 1: Create JOB using builderfactory
    public Job importGeometryJob(JobBuilderFactory jobBuilderFactory,
                                 StepBuilderFactory stepBuilderFactory,
                                 ItemReader<GeometryObjectsModel> itemReader,
                                 ItemProcessor<GeometryObjectsModel,GeometryObjectsModel> itemProcessor,
                                 ItemWriter<GeometryObjectsModel> itemWriter){

        return this.builderFactory.get("GEOMETRY-IMPORT-JOB")
                //sequence of IDs assigned using incrementer during RUN
                //STEP has an reader,processor,writer
                .incrementer(new RunIdIncrementer()).start(step1()).build();
    }

    @Bean
    public Step step1() {
       return this.stepBuilderFactory.get("step1").<GeometryObjectsModel,GeometryObjectsModel>chunk(50)
                .reader(flatFileItemReader()).processor(processor()).writer(writer()).build();
    }

    @Bean
    public JdbcBatchItemWriter<GeometryObjectsModel> writer(){
        JdbcBatchItemWriter<GeometryObjectsModel> writer = new JdbcBatchItemWriter<>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        writer.setSql("CREATE TABLE IF NOT EXISTS new_objects(gid BIGSERIAL PRIMARY KEY,LOCATION_NAME TEXT,LOCATION_ADDRESS TEXT,LATITUDE REAL,LONGITUDE REAL,SHAPE GEOMETRY(POINT,4326)");
        writer.setSql("INSERT INTO new_objects(SHAPE) VALUES(:SHAPE)");
        writer.setDataSource(this.dataSource);
        return writer;
    }

    @Bean
    public JtsModule jtsModule() {
        return new JtsModule();
    }

}

