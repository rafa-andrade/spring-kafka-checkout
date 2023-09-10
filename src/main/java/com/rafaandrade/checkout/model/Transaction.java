package com.rafaandrade.checkout.model;

import com.rafaandrade.checkout.model.enums.TransactionStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Data
public class Transaction {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long transactionId;

  @Column(unique = true)
  private UUID externalReference;

  private UUID customerId;
  private UUID customerAddressId;

  @OneToMany(cascade = ALL)
  private List<TransactionItem> items;

  private BigDecimal shippingAmount;

  private String discountCode;

  private BigDecimal discountAmount;

  @Enumerated(STRING)
  private TransactionStatus status;

  private LocalDateTime creationDate;

  private LocalDateTime lastUpdate;
}
