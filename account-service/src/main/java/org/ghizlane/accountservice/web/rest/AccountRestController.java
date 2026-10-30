package org.ghizlane.accountservice.web.rest;

import jakarta.validation.Valid;
import org.ghizlane.accountservice.dto.*;
import org.ghizlane.accountservice.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/accounts")
public class AccountRestController {

    private final AccountService service;

    public AccountRestController(AccountService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccountResponse create(
            @Valid @RequestBody CreateAccountRequest request
    ) {
        return service.create(request);
    }

    @GetMapping
    public List<AccountResponse> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public AccountResponse findById(
            @PathVariable UUID id
    ) {
        return service.findById(id);
    }

    @PostMapping("/{id}/debit")
    public AccountResponse debit(
            @PathVariable UUID id,
            @Valid @RequestBody BalanceRequest request
    ) {
        return service.debit(id, request.amount());
    }

    @PostMapping("/{id}/credit")
    public AccountResponse credit(
            @PathVariable UUID id,
            @Valid @RequestBody BalanceRequest request
    ) {
        return service.credit(id, request.amount());
    }

    @PatchMapping("/{id}/block")
    public AccountResponse block(@PathVariable UUID id) {
        return service.block(id);
    }

    @PatchMapping("/{id}/activate")
    public AccountResponse activate(@PathVariable UUID id) {
        return service.activate(id);
    }
}