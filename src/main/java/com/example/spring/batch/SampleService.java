package com.example.spring.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SampleService {

  private static final Logger log = LoggerFactory.getLogger(SampleService.class);
  public void execute() {
    //log.info(">>> exec() ");
  }
}
