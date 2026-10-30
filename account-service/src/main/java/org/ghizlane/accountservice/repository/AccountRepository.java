package org.ghizlane.accountservice.repository;

import org.ghizlane.accountservice.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AccountRepository
        extends JpaRepository<Account, UUID> {
}