package com.invenia.portalservice;

import com.invenia.portalservice.rabbitmq.DestinationsConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(DestinationsConfig.class)
public class PortalServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(PortalServiceApplication.class, args);
  }

}
