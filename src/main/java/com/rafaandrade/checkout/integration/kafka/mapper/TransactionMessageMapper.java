package com.rafaandrade.checkout.integration.kafka.mapper;

import com.rafaandrade.checkout.integration.kafka.dto.TransactionEventMessage;
import com.rafaandrade.checkout.model.Transaction;
import com.rafaandrade.checkout.model.enums.TransactionEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionMessageMapper {

  public TransactionEventMessage toDTO(Transaction transaction, TransactionEvent event) {
    return new TransactionEventMessage(
        transaction.getTransactionId(),
        event.toString()
    );
  }

}
