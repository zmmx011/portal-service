package com.invenia.portalservice.rabbitmq;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


@Data
@ConfigurationProperties("destinations")
public class DestinationsConfig {

  private Map<String, DestinationInfo> queues = new HashMap<>();
  private Map<String, DestinationInfo> topics = new HashMap<>();

  @Data
  public static class DestinationInfo {
    private String exchange;
    private String routingKey;
  }
}
