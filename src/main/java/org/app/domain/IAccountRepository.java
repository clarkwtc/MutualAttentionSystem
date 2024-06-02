package org.app.domain;

import java.util.List;
import java.util.UUID;

public interface IAccountRepository {
    void registerAccount(Account account);

    Account findAccount(UUID accountId);

    void updateAccounts(List<Account> accounts);
}
