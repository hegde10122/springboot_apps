package com.hegde.springbootlearning.config;

import com.hegde.springbootlearning.model.User;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SpringBatchConfig {

    public Job jobCSV(JobBuilderFactory jobBuilderFactory,
                      StepBuilderFactory stepBuilderFactory,
                      ItemReader<User> itemReader, ItemProcessor<User,User> itemProcessor, ItemWriter<User> itemWriter){

        Step step = stepBuilderFactory.get("ETL-file-load").
                <User,User>chunk(100).
                reader(itemReader).processor(itemProcessor).writer(itemWriter).build();


       return jobBuilderFactory.get("ETL-Load").incrementer(new RunIdIncrementer()).start(step).build();

    }

    @Bean
    public FlatFileItemReader<User> fileItemReader(@Value())
}
