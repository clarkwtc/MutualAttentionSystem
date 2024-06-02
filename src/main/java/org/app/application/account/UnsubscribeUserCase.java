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
public class UnsubscribeUserCase {
    @Inject
    AccountRepository accountRepository;

    public void execute(String accountId, String subscriptionId){
        Account account = accountRepository.findAccount(UUID.fromString(accountId));
        Account subscription = accountRepository.findAccount(UUID.fromString(subscriptionId));

        if (Objects.isNull(account)){
            throw new NotExistAccountException();
        }

        if (Objects.isNull(subscription)){
            return;
        }
        account.unsubscribe(subscription);

        List<Account> accounts = List.of(account, subscription);
        accountRepository.updateAccounts(accounts);
    }
}
