package com.rafaandrade.checkout.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

  @Bean
  public NewTopic transactionEventTopic(
      @Value("${spring.kafka.topics.transaction-event.name}") String name,
      @Value("${spring.kafka.topics.transaction-event.partitions}") int partitions,
      @Value("${spring.kafka.topics.transaction-event.replicas}") int replicas
  ) {
    return TopicBuilder.name(name)
        .partitions(partitions)
        .replicas(replicas)
        .build();
  }
}
