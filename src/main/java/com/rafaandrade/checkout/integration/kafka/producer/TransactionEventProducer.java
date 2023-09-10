package com.rafaandrade.checkout.integration.kafka.producer;

import com.rafaandrade.checkout.model.enums.TransactionEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import com.rafaandrade.checkout.integration.kafka.dto.TransactionEventMessage;
import com.rafaandrade.checkout.integration.kafka.mapper.TransactionMessageMapper;
import com.rafaandrade.checkout.model.Transaction;

@Component
@RequiredArgsConstructor
public class TransactionEventProducer {

  private final KafkaTemplate<String, TransactionEventMessage> kafkaTemplate;
  private final TransactionMessageMapper transactionMessageMapper;

  @Value("${spring.kafka.topics.transaction-event.name}")
  private String transactionEventTopic;

  public void send(Transaction transaction, TransactionEvent event) {
    kafkaTemplate.send(
        transactionEventTopic,
        transactionMessageMapper.toDTO(transaction, event)
    );
  }
}