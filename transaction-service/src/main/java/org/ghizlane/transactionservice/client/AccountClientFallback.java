package org.ghizlane.transactionservice.client;

import org.ghizlane.transactionservice.dto.AccountResponse;
import org.ghizlane.transactionservice.dto.BalanceRequest;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AccountClientFallback implements AccountClient {

    @Override
    public AccountResponse findById(UUID id) {
        throw new IllegalStateException(
                "Account service is unavailable"
        );
    }

    @Override
    public AccountResponse debit(
            UUID id,
            BalanceRequest request
    ) {
        throw new IllegalStateException(
                "Debit operation is unavailable"
        );
    }

    @Override
    public AccountResponse credit(
            UUID id,
            BalanceRequest request
    ) {
        throw new IllegalStateException(
                "Credit operation is unavailable"
        );
    }
}