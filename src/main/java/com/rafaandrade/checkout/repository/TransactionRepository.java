package com.rafaandrade.checkout.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.rafaandrade.checkout.model.Transaction;

import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
  Optional<Transaction> findByExternalReference(UUID externalReference);
}
