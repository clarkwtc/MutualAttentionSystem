package org.app.application.account;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.app.domain.Account;
import org.app.domain.exceptions.NotExistAccountException;
import org.app.infrastructure.repositories.AccountRepository;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@ApplicationScoped
public class AddFriendUserCase {
    @Inject
    AccountRepository accountRepository;

    public void execute(String accountId, String friendId){
        Account account = accountRepository.findAccount(UUID.fromString(accountId));
        Account friend = accountRepository.findAccount(UUID.fromString(friendId));

        if (Objects.isNull(account) || Objects.isNull(friend)){
            throw new NotExistAccountException();
        }
        account.addFriend(friend);

        List<Account> accounts = List.of(account, friend);
        accountRepository.updateAccounts(accounts);
    }
}
