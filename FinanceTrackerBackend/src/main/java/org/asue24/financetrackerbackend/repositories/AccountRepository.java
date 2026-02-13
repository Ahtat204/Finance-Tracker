package org.asue24.financetrackerbackend.repositories;

import org.asue24.financetrackerbackend.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findAccountByAccountName(String accountName);
    Optional<Account> getAccountById(Long id);

}
