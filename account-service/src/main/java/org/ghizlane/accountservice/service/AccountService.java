package org.ghizlane.accountservice.service;

import org.ghizlane.accountservice.domain.Account;
import org.ghizlane.accountservice.domain.AccountStatus;
import org.ghizlane.accountservice.dto.AccountResponse;
import org.ghizlane.accountservice.dto.CreateAccountRequest;
import org.ghizlane.accountservice.exception.AccountNotFoundException;
import org.ghizlane.accountservice.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class AccountService {

    private final AccountRepository repository;

    public AccountService(AccountRepository repository) {
        this.repository = repository;
    }

    public AccountResponse create(CreateAccountRequest request) {
        Account account = new Account();
        account.setCustomerReference(request.customerReference());
        account.setCurrency(request.currency());
        account.setBalance(request.initialBalance());
        account.setStatus(AccountStatus.ACTIVE);

        return toResponse(repository.save(account));
    }

    @Transactional(readOnly = true)
    public List<AccountResponse> findAll() {
        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public AccountResponse findById(UUID id) {
        return toResponse(getAccount(id));
    }

    public AccountResponse debit(UUID id, BigDecimal amount) {
        Account account = getAccount(id);
        validateActive(account);

        if (account.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException(
                    "Insufficient balance"
            );
        }

        account.setBalance(
                account.getBalance().subtract(amount)
        );

        return toResponse(repository.save(account));
    }

    public AccountResponse credit(UUID id, BigDecimal amount) {
        Account account = getAccount(id);
        validateActive(account);

        account.setBalance(
                account.getBalance().add(amount)
        );

        return toResponse(repository.save(account));
    }

    public AccountResponse block(UUID id) {
        Account account = getAccount(id);
        account.setStatus(AccountStatus.BLOCKED);
        return toResponse(repository.save(account));
    }

    public AccountResponse activate(UUID id) {
        Account account = getAccount(id);
        account.setStatus(AccountStatus.ACTIVE);
        return toResponse(repository.save(account));
    }

    private Account getAccount(UUID id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new AccountNotFoundException(
                                "Account not found: " + id
                        )
                );
    }

    private void validateActive(Account account) {
        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new IllegalArgumentException(
                    "Account is blocked"
            );
        }
    }

    private AccountResponse toResponse(Account account) {
        return new AccountResponse(
                account.getId(),
                account.getCustomerReference(),
                account.getStatus(),
                account.getCurrency(),
                account.getBalance()
        );
    }
}