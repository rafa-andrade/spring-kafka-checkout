package com.rafaandrade.checkout.service;

import com.rafaandrade.checkout.integration.kafka.producer.TransactionEventProducer;
import com.rafaandrade.checkout.model.Transaction;
import com.rafaandrade.checkout.model.enums.TransactionEvent;
import com.rafaandrade.checkout.model.enums.TransactionStatus;
import com.rafaandrade.checkout.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.UUID;

import static com.rafaandrade.checkout.model.enums.TransactionStatus.APPROVED;
import static com.rafaandrade.checkout.model.enums.TransactionStatus.SENT_FOR_CANCELLATION;
import static java.time.LocalDateTime.now;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionCancellationService {

  private final TransactionRepository transactionRepository;
  private final TransactionEventProducer transactionEventProducer;

  public void sentForCancelation(UUID externalReference) {
    Transaction transaction = transactionRepository.findByExternalReference(externalReference)
        .orElseThrow(NoSuchElementException::new);

    if (!APPROVED.equals(transaction.getStatus())) {
      throw new IllegalArgumentException("Invalid transaction status");
    }

    transaction.setStatus(SENT_FOR_CANCELLATION);
    transaction.setLastUpdate(now());
    transactionRepository.saveAndFlush(transaction);

    log.info("Sending transaction cancellation {}", transaction.getTransactionId());
    transactionEventProducer.send(transaction, TransactionEvent.SENT_FOR_CANCELLATION);
  }

  public void processCancellation(long transactionId) {
    transactionRepository.findById(transactionId).ifPresent(transaction -> {
      log.info("Processing transaction cancellation {}", transaction.getTransactionId());

      transaction.setStatus(TransactionStatus.CANCELED);
      transaction.setLastUpdate(now());
      transactionRepository.saveAndFlush(transaction);

      transactionEventProducer.send(transaction, TransactionEvent.CANCELED);
    });
  }
}