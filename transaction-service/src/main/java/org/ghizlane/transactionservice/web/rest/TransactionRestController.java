package org.ghizlane.transactionservice.web.rest;

import jakarta.validation.Valid;
import org.ghizlane.transactionservice.domain.BankTransaction;
import org.ghizlane.transactionservice.dto.*;
import org.ghizlane.transactionservice.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class TransactionRestController {

    private final TransactionService service;

    public TransactionRestController(
            TransactionService service
    ) {
        this.service = service;
    }

    @PostMapping("/transactions")
    @ResponseStatus(HttpStatus.CREATED)
    public BankTransaction create(
            @Valid @RequestBody CreateTransactionRequest request
    ) {
        return service.create(request);
    }

    @GetMapping("/transactions")
    public List<BankTransaction> findAll() {
        return service.findAll();
    }

    @GetMapping("/accounts/{accountId}/transactions")
    public List<BankTransaction> findByAccount(
            @PathVariable UUID accountId
    ) {
        return service.findByAccount(accountId);
    }

    @PostMapping("/transfers")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void transfer(
            @Valid @RequestBody TransferRequest request
    ) {
        service.transfer(request);
    }
}