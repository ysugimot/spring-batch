package com.example.spring.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

  @Autowired
  public JobBuilderFactory jobBuilderFactory;

  @Autowired
  public StepBuilderFactory stepBuilderFactory;

//  @Bean
//  public FlatFileItemReader<Person> reader() {
//    return new FlatFileItemReaderBuilder<Person>()
//            .name("personItemReader")
//            .resource(new ClassPathResource("sample-data.csv"))
//            .delimited()
//            .names(new String[]{"personId", "firstName", "lastName"})
//            .fieldSetMapper(new BeanWrapperFieldSetMapper<Person>() {{
//              setTargetType(Person.class);
//            }})
//            .build();
//  }

  @Bean
  public JdbcCursorItemReader<Person> reader(DataSource dataSource) {
    return new JdbcCursorItemReaderBuilder<Person>()
            .dataSource(dataSource)
            .name("jdbcCursorItemReader")
            .sql("SELECT person_id, first_name, last_name FROM people1 ORDER BY person_id;")
            .rowMapper(new PersonRowMapper())
            .build();
  }

  @Bean
  public PersonItemProcessor processor() {
    return new PersonItemProcessor();
  }

  @Bean
  public JdbcBatchItemWriter<Person> writer(DataSource dataSource) {
    return new JdbcBatchItemWriterBuilder<Person>()
            .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
            .sql("INSERT INTO people2 (person_id, first_name, last_name) VALUES (:personId, :firstName, :lastName)")
            .dataSource(dataSource)
            .build();
  }

  @Bean
  public Job importUserJob(JobCompletionNotificationListener listener, Step step1) {
    return jobBuilderFactory.get("importUserJob")
            .incrementer(new RunIdIncrementer())
            .listener(listener)
            .flow(step1)
            .end()
            .build();
  }

  @Autowired
  DataSource datasource;

  @Bean
  public Step step1(JdbcBatchItemWriter<Person> writer) {
    return stepBuilderFactory.get("step1")
            .<Person, Person>chunk(5)
            .reader(reader(datasource))
            .processor(processor())
            .writer(writer)
            .build();
  }
}
