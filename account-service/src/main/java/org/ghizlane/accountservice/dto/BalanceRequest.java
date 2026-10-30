package org.ghizlane.accountservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record BalanceRequest(
        @NotNull @Positive BigDecimal amount
) {
}