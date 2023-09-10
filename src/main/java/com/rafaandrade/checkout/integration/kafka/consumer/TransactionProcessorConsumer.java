package com.rafaandrade.checkout.integration.kafka.consumer;

import com.rafaandrade.checkout.integration.kafka.dto.TransactionEventMessage;
import com.rafaandrade.checkout.service.TransactionProcessorService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import static com.rafaandrade.checkout.model.enums.TransactionEvent.CREATED;

@Component
@RequiredArgsConstructor
public class TransactionProcessorConsumer {

  private final TransactionProcessorService transactionProcessorService;

  @KafkaListener(
      topics = "${spring.kafka.topics.transaction-event.name}",
      groupId = "transaction-processor"
  )
  public void processTransaction(TransactionEventMessage transactionEventMessage) {
    if (CREATED.toString().equals(transactionEventMessage.event())) {
      transactionProcessorService.processTransaction(transactionEventMessage.transactionId());
    }
  }

}