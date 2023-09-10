package com.rafaandrade.checkout.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.NoSuchElementException;
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

public class TransactionServiceTest {

  @Mock
  private TransactionRepository transactionRepository;

  @Mock
  private TransactionEventProducer transactionEventProducer;

  @InjectMocks
  private TransactionService transactionService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  void testCreateTransaction() {
    // Arrange
    Transaction newTransaction = new Transaction();

    // Act
    transactionService.createTransaction(newTransaction);

    // Assert
    ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
    verify(transactionRepository, times(1)).saveAndFlush(transactionCaptor.capture());

    Transaction capturedTransaction = transactionCaptor.getValue();
    assertNotNull(capturedTransaction);
    assertNotNull(capturedTransaction.getExternalReference());
    assertEquals(TransactionStatus.PROCESSING, capturedTransaction.getStatus());

    verify(transactionEventProducer, times(1)).send(newTransaction, TransactionEvent.CREATED);
  }

  @Test
  void testFindByExternalReference() {
    // Arrange
    UUID externalReference = UUID.randomUUID();
    Transaction mockTransaction = new Transaction();
    when(transactionRepository.findByExternalReference(externalReference)).thenReturn(Optional.of(mockTransaction));

    // Act
    Transaction foundTransaction = transactionService.findByExternalReference(externalReference);

    // Assert
    assertNotNull(foundTransaction);
    assertEquals(mockTransaction, foundTransaction);
  }

  @Test
  void testFindByExternalReferenceNotFound() {
    // Arrange
    UUID nonExistentExternalReference = UUID.randomUUID();
    when(transactionRepository.findByExternalReference(nonExistentExternalReference)).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(NoSuchElementException.class, () -> transactionService.findByExternalReference(nonExistentExternalReference));
  }
}

