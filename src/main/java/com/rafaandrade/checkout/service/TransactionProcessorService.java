package com.rafaandrade.checkout.service;

import com.rafaandrade.checkout.integration.kafka.producer.TransactionEventProducer;
import com.rafaandrade.checkout.model.enums.TransactionEvent;
import com.rafaandrade.checkout.model.enums.TransactionStatus;
import com.rafaandrade.checkout.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static java.time.LocalDateTime.now;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionProcessorService {

  private final TransactionRepository transactionRepository;
  private final TransactionEventProducer transactionEventProducer;

  public void processTransaction(long transactionId) {
    transactionRepository.findById(transactionId).ifPresent(transaction -> {
      log.info("Processing transaction {}", transaction.getTransactionId());

      transaction.setStatus(TransactionStatus.APPROVED);
      transaction.setLastUpdate(now());
      transactionRepository.saveAndFlush(transaction);

      transactionEventProducer.send(transaction, TransactionEvent.APPROVED);
    });
  }
}
