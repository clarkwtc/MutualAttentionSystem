package org.app.application.account;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.app.domain.Account;
import org.app.infrastructure.repositories.AccountRepository;

@ApplicationScoped
public class RegisterAccountUserCase {
    @Inject
    AccountRepository accountRepository;

    public Account execute(String username, String password, String nickname){
        Account account = new Account(username, password, nickname);
        accountRepository.registerAccount(account);
        return account;
    }
}
