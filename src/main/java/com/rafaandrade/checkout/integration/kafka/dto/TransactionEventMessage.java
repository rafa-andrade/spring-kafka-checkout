package com.rafaandrade.checkout.integration.kafka.dto;

public record TransactionEventMessage(
    Long transactionId,
    String event
) {
}
