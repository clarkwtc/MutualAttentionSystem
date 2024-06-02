package org.app.application.account;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.app.domain.Account;
import org.app.infrastructure.repositories.AccountRepository;

import java.util.UUID;

@ApplicationScoped
public class GetAccountUserCase {
    @Inject
    AccountRepository accountRepository;

    public Account execute(String accountId){
        return accountRepository.findAccount(UUID.fromString(accountId));
    }
}
