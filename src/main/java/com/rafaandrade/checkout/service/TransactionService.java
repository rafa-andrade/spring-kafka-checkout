package com.rafaandrade.checkout.service;

import com.rafaandrade.checkout.integration.kafka.producer.TransactionEventProducer;
import com.rafaandrade.checkout.model.Transaction;
import com.rafaandrade.checkout.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.UUID;

import static com.rafaandrade.checkout.model.enums.TransactionEvent.CREATED;
import static com.rafaandrade.checkout.model.enums.TransactionStatus.PROCESSING;
import static java.time.LocalDateTime.now;
import static java.util.UUID.randomUUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {

  private final TransactionRepository transactionRepository;
  private final TransactionEventProducer transactionEventProducer;

  public void createTransaction(Transaction newTransaction) {
    newTransaction.setExternalReference(randomUUID());
    newTransaction.setStatus(PROCESSING);
    newTransaction.setCreationDate(now());
    newTransaction.setLastUpdate(now());

    transactionRepository.saveAndFlush(newTransaction);

    log.info("Sending new transaction {}", newTransaction.getTransactionId());
    transactionEventProducer.send(newTransaction, CREATED);
  }

  public Transaction findByExternalReference(UUID externalReference) {
    return transactionRepository.findByExternalReference(externalReference)
        .orElseThrow(NoSuchElementException::new);
  }
}