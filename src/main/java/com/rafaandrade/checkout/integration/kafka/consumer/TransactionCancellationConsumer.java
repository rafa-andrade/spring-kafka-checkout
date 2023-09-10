package com.rafaandrade.checkout.integration.kafka.consumer;

import com.rafaandrade.checkout.integration.kafka.dto.TransactionEventMessage;
import com.rafaandrade.checkout.service.TransactionCancellationService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import static com.rafaandrade.checkout.model.enums.TransactionEvent.SENT_FOR_CANCELLATION;

@Component
@RequiredArgsConstructor
public class TransactionCancellationConsumer {

  private final TransactionCancellationService transactionCancellationService;

  @KafkaListener(
      topics = "${spring.kafka.topics.transaction-event.name}",
      groupId = "transaction-cancellation"
  )
  public void cancelTransaction(TransactionEventMessage transactionEventMessage) {
    if (SENT_FOR_CANCELLATION.toString().equals(transactionEventMessage.event())) {
      transactionCancellationService.processCancellation(transactionEventMessage.transactionId());
    }
  }

}