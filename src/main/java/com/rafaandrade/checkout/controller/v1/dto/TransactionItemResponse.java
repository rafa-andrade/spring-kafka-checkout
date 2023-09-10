package com.rafaandrade.checkout.controller.v1.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record TransactionItemResponse(
    UUID productId,
    BigDecimal quantity,
    BigDecimal totalAmount
) {
}
