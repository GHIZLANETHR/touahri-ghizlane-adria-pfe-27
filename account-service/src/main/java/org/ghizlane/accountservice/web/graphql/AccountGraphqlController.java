package org.ghizlane.accountservice.web.graphql;

import org.ghizlane.accountservice.dto.AccountResponse;
import org.ghizlane.accountservice.dto.CreateAccountRequest;
import org.ghizlane.accountservice.service.AccountService;
import org.springframework.graphql.data.method.annotation.*;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Controller
public class AccountGraphqlController {

    private final AccountService service;

    public AccountGraphqlController(AccountService service) {
        this.service = service;
    }

    @QueryMapping
    public List<AccountResponse> accounts() {
        return service.findAll();
    }

    @QueryMapping
    public AccountResponse accountById(
            @Argument UUID id
    ) {
        return service.findById(id);
    }

    @MutationMapping
    public AccountResponse createAccount(
            @Argument String customerReference,
            @Argument String currency,
            @Argument BigDecimal initialBalance
    ) {
        return service.create(
                new CreateAccountRequest(
                        customerReference,
                        currency,
                        initialBalance
                )
        );
    }

    @MutationMapping
    public AccountResponse blockAccount(
            @Argument UUID id
    ) {
        return service.block(id);
    }

    @MutationMapping
    public AccountResponse activateAccount(
            @Argument UUID id
    ) {
        return service.activate(id);
    }
}