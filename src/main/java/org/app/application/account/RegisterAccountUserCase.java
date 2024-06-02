package org.app.application.account;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.app.domain.Account;
import org.app.domain.exceptions.DuplicatedAccountException;
import org.app.infrastructure.repositories.AccountRepository;

import java.util.Objects;

@ApplicationScoped
public class RegisterAccountUserCase {
    @Inject
    AccountRepository accountRepository;

    public Account execute(String username, String password, String nickname){
        Account account = new Account(username, password, nickname);
        if(Objects.nonNull(accountRepository.findAccountByUsername(account.getUsername()))){
            throw new DuplicatedAccountException();
        }
        accountRepository.registerAccount(account);
        return account;
    }
}
