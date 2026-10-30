package org.ghizlane.transactionservice.client;

import org.ghizlane.transactionservice.dto.AccountResponse;
import org.ghizlane.transactionservice.dto.BalanceRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@FeignClient(
        name = "account-service",
        fallback = AccountClientFallback.class
)
public interface AccountClient {

    @GetMapping("/api/accounts/{id}")
    AccountResponse findById(@PathVariable UUID id);

    @PostMapping("/api/accounts/{id}/debit")
    AccountResponse debit(
            @PathVariable UUID id,
            @RequestBody BalanceRequest request
    );

    @PostMapping("/api/accounts/{id}/credit")
    AccountResponse credit(
            @PathVariable UUID id,
            @RequestBody BalanceRequest request
    );
}