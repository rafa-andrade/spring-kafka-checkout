package com.rafaandrade.checkout.controller.v1.mapper;

import com.rafaandrade.checkout.controller.v1.dto.TransactionItemResponse;
import com.rafaandrade.checkout.controller.v1.dto.TransactionRequest;
import com.rafaandrade.checkout.controller.v1.dto.TransactionResponse;
import com.rafaandrade.checkout.model.Transaction;
import com.rafaandrade.checkout.model.TransactionItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionMapper {

  public Transaction toModel(TransactionRequest transactionRequest) {
    Transaction transaction = new Transaction();
    transaction.setCustomerId(transactionRequest.customerId());
    transaction.setCustomerAddressId(transactionRequest.customerAddressId());
    transaction.setShippingAmount(transactionRequest.shippingAmount());
    transaction.setDiscountCode(transactionRequest.discountCode());
    transaction.setDiscountAmount(transactionRequest.discountAmoun());
    transaction.setItems(
        transactionRequest.items().stream().map(item -> {
          TransactionItem transactionItem = new TransactionItem();
          transactionItem.setQuantity(item.quantity());
          transactionItem.setProductId(item.productId());
          transactionItem.setTotalAmount(item.totalAmount());
          return transactionItem;
        }).toList()
    );
    return transaction;
  }

  public TransactionResponse toDTO(Transaction transaction) {
    return new TransactionResponse(
        transaction.getExternalReference(),
        transaction.getCustomerId(),
        transaction.getCustomerAddressId(),
        transaction.getItems().stream().map(item ->
          new TransactionItemResponse(item.getProductId(), item.getQuantity(), item.getTotalAmount())
        ).toList(),
        transaction.getShippingAmount(),
        transaction.getDiscountCode(),
        transaction.getDiscountAmount(),
        transaction.getStatus().toString()
    );
  }

}
