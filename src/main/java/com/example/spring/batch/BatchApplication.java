package com.example.spring.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class BatchApplication {
  public static void main(String[] args) throws Exception {
    ConfigurableApplicationContext context = SpringApplication.run(BatchApplication.class, args);
    JobLauncher jobLauncher = context.getBean("jobLauncher", JobLauncher.class);
    JobParameters params = new JobParametersBuilder().addString("job_key", "1")
            .toJobParameters();

    Job job = (Job) context.getBean("importUserJob", Job.class);
    JobExecution jobExecution = jobLauncher.run(job, params);
  }

}
