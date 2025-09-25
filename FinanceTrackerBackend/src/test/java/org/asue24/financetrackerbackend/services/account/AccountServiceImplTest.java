package org.asue24.financetrackerbackend.services.account;

import org.asue24.financetrackerbackend.entities.Account;
import org.asue24.financetrackerbackend.entities.User;
import org.asue24.financetrackerbackend.repositories.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    User user = new User(22L, "lahcenhhh", "ahtat", "hereweare");
    Account account = new Account(1L, "lahcen", 22.2, user);

    @InjectMocks
    private AccountServiceImpl accountService;
    @Mock
    private AccountRepository accountRepository;

    @Test
    void addAccount() {
        when(accountRepository.save(account)).thenReturn(account);
        var result = accountService.addAccount(account);
        assertNotNull(result);
        assertEquals(account, result);
    }

    @Test
    void deleteAccount() {
        when(accountRepository.existsById(account.getId())).thenReturn(true);
        var result = accountService.deleteAccount(1L);
        assertNotNull(result);
        assertEquals(true, result);
    }

    @Test
    void updateAccount() {
        when(accountRepository.findById(account.getId())).thenReturn(Optional.of(account));
        when(accountRepository.save(account)).thenReturn(account);
        var result = accountService.updateAccount(1L, account);
        assertNotNull(result);
        assertEquals(account, result);
    }

    @Test
    void getAccountByAccountId() {
        when(accountRepository.findById(account.getId())).thenReturn(Optional.of(account));
        var result = accountService.getAccountByAccountId(account.getId());
        assertNotNull(result);
        assertEquals(result, account);
    }

    @Test
    void getAccounts() {
        var accounts = List.of(account, new Account(4L, "laen", 223.2, user));
        when(accountRepository.findAll()).thenReturn(accounts);
        var result = accountService.getAccounts();
        assertNotNull(result);
        assertEquals(result, accounts);
    }
}