package org.app.application.account;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.app.domain.Account;
import org.app.infrastructure.repositories.AccountRepository;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class RemoveFriendUserCase {
    @Inject
    AccountRepository accountRepository;

    public void execute(String accountId, String friendId){
        Account account = accountRepository.findAccount(UUID.fromString(accountId));
        Account friend = accountRepository.findAccount(UUID.fromString(friendId));
        account.removeFriend(friend);

        List<Account> accounts = List.of(account, friend);
        accountRepository.updateAccounts(accounts);
    }
}
