package com.rafaandrade.checkout.service;

import static org.mockito.Mockito.*;

import java.util.Optional;

import com.rafaandrade.checkout.model.Transaction;
import com.rafaandrade.checkout.model.enums.TransactionEvent;
import com.rafaandrade.checkout.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class TransactionNotifierServiceTest {

  @Mock
  private TransactionRepository transactionRepository;

  @InjectMocks
  private TransactionNotifierService transactionNotifierService;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  void testNotifyTransactionStatus() {
    // Arrange
    long transactionId = 1L;
    TransactionEvent transactionEvent = TransactionEvent.APPROVED;
    Transaction mockTransaction = new Transaction();
    mockTransaction.setTransactionId(transactionId);

    when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(mockTransaction));

    // Act
    transactionNotifierService.notifyTransactionStatus(transactionId, transactionEvent);

    // Assert
    verify(transactionRepository, times(1)).findById(transactionId);
  }

  @Test
  void testNotifyTransactionStatusTransactionNotFound() {
    // Arrange
    long nonExistentTransactionId = 999L;

    when(transactionRepository.findById(nonExistentTransactionId)).thenReturn(Optional.empty());

    // Act
    transactionNotifierService.notifyTransactionStatus(nonExistentTransactionId, TransactionEvent.APPROVED);

    // Assert
    verify(transactionRepository, times(1)).findById(nonExistentTransactionId);
  }
}
