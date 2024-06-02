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
public class AddFriendUserCase {
    @Inject
    AccountRepository accountRepository;

    public void execute(String accountId, String friendId){
        User user = accountRepository.findAccount(UUID.fromString(accountId));
        User friend = accountRepository.findAccount(UUID.fromString(friendId));

        if (Objects.isNull(user) || Objects.isNull(friend)){
            throw new NotExistAccountException();
        }
//        user.addFriend(friend);

        List<User> users = List.of(user, friend);
        accountRepository.updateAccounts(users);
    }
}
