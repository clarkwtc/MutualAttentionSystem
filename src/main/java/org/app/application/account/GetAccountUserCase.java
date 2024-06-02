package org.app.application.account;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.app.domain.User;
import org.app.domain.exceptions.NotExistAccountException;
import org.app.infrastructure.repositories.AccountRepository;

import java.util.Objects;
import java.util.UUID;

@ApplicationScoped
public class GetAccountUserCase {
    @Inject
    AccountRepository accountRepository;

    public User execute(String accountId){
        User user = accountRepository.findAccount(UUID.fromString(accountId));
        if (Objects.isNull(user)){
            throw new NotExistAccountException();
        }
        return user;
    }
}
