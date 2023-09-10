package com.rafaandrade.checkout.controller.v1.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record TransactionResponse(
    UUID externalReference,
    UUID customerId,
    UUID customerAddressId,
    List<TransactionItemResponse> items,
    BigDecimal shippingAmount,
    String discountCode,
    BigDecimal discountAmoun,
    String status
) {
}
