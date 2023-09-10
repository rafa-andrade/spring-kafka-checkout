package com.rafaandrade.checkout.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Data
public class TransactionItem {
  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long transactionItemId;

  @ManyToOne
  @JoinColumn
  private Transaction transaction;

  private UUID productId;
  private BigDecimal quantity;
  private BigDecimal totalAmount;
}
