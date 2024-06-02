package org.app.domain;

import java.util.List;
import java.util.UUID;

public interface IAccountRepository {
    void registerAccount(User user);

    User findAccount(UUID accountId);

    User findAccountByUsername(String username);

    void updateAccounts(List<User> users);
}
