package org.ghizlane.accountservice.mcp;

import org.ghizlane.accountservice.dto.AccountResponse;
import org.ghizlane.accountservice.service.AccountService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AccountMcpTools {

    private final AccountService service;

    public AccountMcpTools(AccountService service) {
        this.service = service;
    }

    @Tool(description = "List all available bank accounts")
    public List<AccountResponse> listAccounts() {
        return service.findAll();
    }

    @Tool(description = "Find a bank account using its UUID")
    public AccountResponse findAccount(
            @ToolParam(description = "Bank account UUID")
            String accountId
    ) {
        return service.findById(
                UUID.fromString(accountId)
        );
    }
}