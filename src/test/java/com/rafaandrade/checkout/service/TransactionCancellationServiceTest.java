package com.rafaandrade.checkout.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.UUID;

import com.rafaandrade.checkout.integration.kafka.producer.TransactionEventProducer;
import com.rafaandrade.checkout.model.Transaction;
import com.rafaandrade.checkout.model.enums.TransactionEvent;
import com.rafaandrade.checkout.model.enums.TransactionStatus;
import com.rafaandrade.checkout.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class TransactionCancellationServiceTest {

  @Mock
  private TransactionRepository transactionRepository;

  @Mock
  private TransactionEventProducer transactionEventProducer;

  @InjectMocks
  private TransactionCancellationService transactionCancellationService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  void testSentForCancellation() {
    // Arrange
    UUID externalReference = UUID.randomUUID();
    Transaction mockTransaction = new Transaction();
    mockTransaction.setStatus(TransactionStatus.APPROVED);

    when(transactionRepository.findByExternalReference(externalReference)).thenReturn(Optional.of(mockTransaction));

    // Act
    transactionCancellationService.sentForCancellation(externalReference);

    // Assert
    ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
    verify(transactionRepository, times(1)).saveAndFlush(transactionCaptor.capture());

    Transaction capturedTransaction = transactionCaptor.getValue();
    assertNotNull(capturedTransaction);
    assertEquals(TransactionStatus.SENT_FOR_CANCELLATION, capturedTransaction.getStatus());

    verify(transactionEventProducer, times(1)).send(capturedTransaction, TransactionEvent.SENT_FOR_CANCELLATION);
  }

  @Test
  void testSentForCancellationInvalidStatus() {
    // Arrange
    UUID externalReference = UUID.randomUUID();
    Transaction mockTransaction = new Transaction();
    mockTransaction.setStatus(TransactionStatus.PROCESSING);

    when(transactionRepository.findByExternalReference(externalReference)).thenReturn(Optional.of(mockTransaction));

    // Act & Assert sentForCancelation
    assertThrows(IllegalArgumentException.class, () -> transactionCancellationService.sentForCancellation(externalReference));
    verify(transactionRepository, never()).saveAndFlush(any());
    verify(transactionEventProducer, never()).send(any(), any());
  }

  @Test
  void testProcessCancellation() {
    // Arrange
    long transactionId = 1L;
    Transaction mockTransaction = new Transaction();
    mockTransaction.setStatus(TransactionStatus.APPROVED);

    when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(mockTransaction));

    // Act
    transactionCancellationService.processCancellation(transactionId);

    // Assert
    ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
    verify(transactionRepository, times(1)).saveAndFlush(transactionCaptor.capture());

    Transaction capturedTransaction = transactionCaptor.getValue();
    assertNotNull(capturedTransaction);
    assertEquals(TransactionStatus.CANCELED, capturedTransaction.getStatus());

    verify(transactionEventProducer, times(1)).send(capturedTransaction, TransactionEvent.CANCELED);
  }

  @Test
  void testProcessCancellationTransactionNotFound() {
    // Arrange
    long nonExistentTransactionId = 999L;
    when(transactionRepository.findById(nonExistentTransactionId)).thenReturn(Optional.empty());

    // Act & Assert
    transactionCancellationService.processCancellation(nonExistentTransactionId);
    verify(transactionRepository, never()).saveAndFlush(any());
    verify(transactionEventProducer, never()).send(any(), any());
  }
}