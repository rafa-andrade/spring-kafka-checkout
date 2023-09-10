package com.rafaandrade.checkout.integration.kafka.consumer;

import com.rafaandrade.checkout.integration.kafka.dto.TransactionEventMessage;
import com.rafaandrade.checkout.model.enums.TransactionEvent;
import com.rafaandrade.checkout.service.TransactionNotifierService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import static com.rafaandrade.checkout.model.enums.TransactionEvent.APPROVED;
import static com.rafaandrade.checkout.model.enums.TransactionEvent.CANCELED;
import static java.util.Arrays.asList;

@Component
@RequiredArgsConstructor
public class TransactionNotifierConsumer {

  private final TransactionNotifierService transactionNotifierService;

  @KafkaListener(
      topics = "${spring.kafka.topics.transaction-event.name}",
      groupId = "transaction-notifier"
  )
  public void notify(TransactionEventMessage transactionEventMessage) {
    if (asList(APPROVED, CANCELED).contains(
        TransactionEvent.valueOf(transactionEventMessage.event())
    )) {
      transactionNotifierService.notifyTransactionStatus(
          transactionEventMessage.transactionId(),
          TransactionEvent.valueOf(transactionEventMessage.event())
      );
    }
  }

}