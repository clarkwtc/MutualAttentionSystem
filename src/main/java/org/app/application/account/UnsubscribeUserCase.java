package org.app.application.account;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.app.domain.User;
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
        User user = accountRepository.findAccount(UUID.fromString(accountId));
        User subscription = accountRepository.findAccount(UUID.fromString(subscriptionId));

        if (Objects.isNull(user)){
            throw new NotExistAccountException();
        }

        if (Objects.isNull(subscription)){
            return;
        }
        user.unsubscribe(subscription);

        List<User> users = List.of(user, subscription);
        accountRepository.updateAccounts(users);
    }
}
