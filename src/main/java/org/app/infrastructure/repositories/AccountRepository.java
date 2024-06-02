package org.app.infrastructure.repositories;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.app.domain.User;
import org.app.domain.IAccountRepository;
import org.app.infrastructure.local.InMemoryAccountRepository;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class AccountRepository implements IAccountRepository {
    @Inject
    InMemoryAccountRepository inMemoryAccountRepository;

    @Override
    public void registerAccount(User user) {
        inMemoryAccountRepository.addAccount(user);
    }

    @Override
    public User findAccount(UUID accountId) {
        return inMemoryAccountRepository.findAccount(accountId);
    }

    @Override
    public User findAccountByUsername(String username) {
        return inMemoryAccountRepository.findAccountByUsername(username);
    }

    @Override
    public void updateAccounts(List<User> users) {
        inMemoryAccountRepository.updateAccounts(users);
    }
}
