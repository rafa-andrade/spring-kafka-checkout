package com.rafaandrade.checkout.service;

import com.rafaandrade.checkout.model.enums.TransactionEvent;
import com.rafaandrade.checkout.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionNotifierService {

  private final TransactionRepository transactionRepository;

  public void notifyTransactionStatus(long transactionId, TransactionEvent transactionEvent) {
    transactionRepository.findById(transactionId).ifPresent(transaction ->
      log.info("Transaction id {} has been updated to status {}", transactionId, transactionEvent)
    );
  }

}
