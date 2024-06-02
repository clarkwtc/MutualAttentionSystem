package org.app.infrastructure.repositories;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.app.domain.Account;
import org.app.domain.IAccountRepository;
import org.app.infrastructure.local.InMemoryAccountRepository;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class AccountRepository implements IAccountRepository {
    @Inject
    InMemoryAccountRepository inMemoryAccountRepository;

    @Override
    public void registerAccount(Account account) {
        inMemoryAccountRepository.addAccount(account);
    }

    @Override
    public Account findAccount(UUID accountId) {
        return inMemoryAccountRepository.findAccount(accountId);
    }

    @Override
    public void updateAccounts(List<Account> accounts) {
        inMemoryAccountRepository.updateAccounts(accounts);
    }
}
