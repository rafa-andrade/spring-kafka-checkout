package com.rafaandrade.checkout.controller.v1;

import com.rafaandrade.checkout.controller.v1.dto.TransactionRequest;
import com.rafaandrade.checkout.controller.v1.dto.TransactionResponse;
import com.rafaandrade.checkout.controller.v1.mapper.TransactionMapper;
import com.rafaandrade.checkout.model.Transaction;
import com.rafaandrade.checkout.service.TransactionCancellationService;
import com.rafaandrade.checkout.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.http.HttpStatus.ACCEPTED;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

  private final TransactionService transactionService;
  private final TransactionCancellationService transactionCancellationService;
  private final TransactionMapper transactionMapper;

  @PostMapping
  @ResponseStatus(ACCEPTED)
  public UUID createTransaction(@RequestBody @Valid TransactionRequest transactionRequest) {
    Transaction transaction = transactionMapper.toModel(transactionRequest);
    transactionService.createTransaction(transaction);
    return transaction.getExternalReference();
  }

  @GetMapping("/{externalReference}")
  public TransactionResponse getTransaction(@PathVariable UUID externalReference) {
    return transactionMapper.toDTO(
        transactionService.findByExternalReference(externalReference)
    );
  }

  @DeleteMapping("/{externalReference}")
  @ResponseStatus(ACCEPTED)
  public void cancelTransaction(@PathVariable UUID externalReference) {
    transactionCancellationService.sentForCancelation(externalReference);
  }

}