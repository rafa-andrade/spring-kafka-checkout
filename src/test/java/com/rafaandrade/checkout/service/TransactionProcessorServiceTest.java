package com.rafaandrade.checkout.service;

import static org.mockito.Mockito.*;

import java.util.Optional;

import com.rafaandrade.checkout.integration.kafka.producer.TransactionEventProducer;
import com.rafaandrade.checkout.model.Transaction;
import com.rafaandrade.checkout.model.enums.TransactionEvent;
import com.rafaandrade.checkout.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class TransactionProcessorServiceTest {

  @Mock
  private TransactionRepository transactionRepository;

  @Mock
  private TransactionEventProducer transactionEventProducer;

  @InjectMocks
  private TransactionProcessorService transactionProcessorService;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  void testProcessTransaction() {
    // Arrange
    long transactionId = 1L;
    Transaction mockTransaction = new Transaction();
    mockTransaction.setTransactionId(transactionId);

    when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(mockTransaction));

    // Act
    transactionProcessorService.processTransaction(transactionId);

    // Assert
    verify(transactionRepository, times(1)).saveAndFlush(mockTransaction);
    verify(transactionEventProducer, times(1)).send(mockTransaction, TransactionEvent.APPROVED);
  }

  @Test
  void testProcessTransactionTransactionNotFound() {
    // Arrange
    long nonExistentTransactionId = 999L;

    when(transactionRepository.findById(nonExistentTransactionId)).thenReturn(Optional.empty());

    // Act
    transactionProcessorService.processTransaction(nonExistentTransactionId);

    // Assert
    verify(transactionRepository, never()).saveAndFlush(any());
    verify(transactionEventProducer, never()).send(any(), any());
  }
}

