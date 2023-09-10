package com.rafaandrade.checkout.controller.v1.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record TransactionRequest(
    @NotNull UUID customerId,
    @NotNull UUID customerAddressId,
    @NotEmpty List<TransactionItemRequest> items,
    @NotNull BigDecimal shippingAmount,
    String discountCode,
    BigDecimal discountAmoun
) {
}
