package org.ghizlane.accountservice.dto;

import org.ghizlane.accountservice.domain.AccountStatus;

import java.math.BigDecimal;
import java.util.UUID;

public record AccountResponse(
        UUID id,
        String customerReference,
        AccountStatus status,
        String currency,
        BigDecimal balance
) {
}