package com.rafaandrade.checkout.controller.v1.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record TransactionItemRequest(
    @NotNull UUID productId,
    @NotNull BigDecimal quantity,
    @NotNull BigDecimal totalAmount
) {
}
