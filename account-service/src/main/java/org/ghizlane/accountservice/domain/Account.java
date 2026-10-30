package org.ghizlane.accountservice.domain;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "bank_accounts")
public class Account {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String customerReference;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountStatus status;

    @Column(nullable = false, length = 3)
    private String currency;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal balance;

    @Version
    private Long version;

    public Account() {
    }

    @PrePersist
    public void beforeInsert() {
        if (id == null) {
            id = UUID.randomUUID();
        }

        if (status == null) {
            status = AccountStatus.ACTIVE;
        }

        if (balance == null) {
            balance = BigDecimal.ZERO;
        }

        if (currency != null) {
            currency = currency.toUpperCase();
        }
    }

    public UUID getId() {
        return id;
    }

    public String getCustomerReference() {
        return customerReference;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public String getCurrency() {
        return currency;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public Long getVersion() {
        return version;
    }

    public void setCustomerReference(String customerReference) {
        this.customerReference = customerReference;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}