package org.ghizlane.transactionservice.service;

import org.ghizlane.transactionservice.client.AccountClient;
import org.ghizlane.transactionservice.domain.BankTransaction;
import org.ghizlane.transactionservice.dto.*;
import org.ghizlane.transactionservice.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class TransactionService {

    private final TransactionRepository repository;
    private final AccountClient accountClient;

    @Value("${bank.transaction.max-amount}")
    private BigDecimal maximumAmount;

    public TransactionService(
            TransactionRepository repository,
            AccountClient accountClient
    ) {
        this.repository = repository;
        this.accountClient = accountClient;
    }

    public BankTransaction create(
            CreateTransactionRequest request
    ) {
        validateMaximumAmount(request.amount());

        AccountResponse account =
                accountClient.findById(request.accountId());

        validateAccount(account, request.currency());

        if (account.balance().compareTo(request.amount()) < 0) {
            throw new IllegalArgumentException(
                    "Insufficient balance"
            );
        }

        accountClient.debit(
                request.accountId(),
                new BalanceRequest(request.amount())
        );

        BankTransaction transaction =
                new BankTransaction();

        transaction.setAccountId(request.accountId());
        transaction.setAmount(request.amount());
        transaction.setCurrency(request.currency());
        transaction.setMerchantCategory(
                request.merchantCategory()
        );
        transaction.setCountry(request.country());

        return repository.save(transaction);
    }

    public void transfer(TransferRequest request) {
        if (request.sourceAccountId()
                .equals(request.destinationAccountId())) {
            throw new IllegalArgumentException(
                    "Source and destination must be different"
            );
        }

        validateMaximumAmount(request.amount());

        AccountResponse source =
                accountClient.findById(
                        request.sourceAccountId()
                );

        AccountResponse destination =
                accountClient.findById(
                        request.destinationAccountId()
                );

        validateAccount(source, source.currency());
        validateAccount(destination, destination.currency());

        if (!source.currency()
                .equalsIgnoreCase(destination.currency())) {
            throw new IllegalArgumentException(
                    "Accounts must use the same currency"
            );
        }

        if (source.balance().compareTo(request.amount()) < 0) {
            throw new IllegalArgumentException(
                    "Insufficient balance"
            );
        }

        accountClient.debit(
                source.id(),
                new BalanceRequest(request.amount())
        );

        try {
            accountClient.credit(
                    destination.id(),
                    new BalanceRequest(request.amount())
            );
        } catch (RuntimeException exception) {
            accountClient.credit(
                    source.id(),
                    new BalanceRequest(request.amount())
            );

            throw exception;
        }
    }

    @Transactional(readOnly = true)
    public List<BankTransaction> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public List<BankTransaction> findByAccount(
            UUID accountId
    ) {
        return repository.findByAccountId(accountId);
    }

    private void validateMaximumAmount(BigDecimal amount) {
        if (amount.compareTo(maximumAmount) > 0) {
            throw new IllegalArgumentException(
                    "Maximum transaction amount is "
                            + maximumAmount
            );
        }
    }

    private void validateAccount(
            AccountResponse account,
            String currency
    ) {
        if (!"ACTIVE".equals(account.status())) {
            throw new IllegalArgumentException(
                    "Account is blocked"
            );
        }

        if (!account.currency().equalsIgnoreCase(currency)) {
            throw new IllegalArgumentException(
                    "Currency does not match account currency"
            );
        }
    }
}