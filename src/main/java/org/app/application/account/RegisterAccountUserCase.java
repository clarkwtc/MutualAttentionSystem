package org.app.application.account;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.app.domain.User;
import org.app.domain.exceptions.DuplicatedAccountException;
import org.app.infrastructure.repositories.AccountRepository;

import java.util.Objects;

@ApplicationScoped
public class RegisterAccountUserCase {
    @Inject
    AccountRepository accountRepository;

    public User execute(String username, String password, String nickname){
        User user = new User(username);
        if(Objects.nonNull(accountRepository.findAccountByUsername(user.getUsername()))){
            throw new DuplicatedAccountException();
        }
        accountRepository.registerAccount(user);
        return user;
    }
}
