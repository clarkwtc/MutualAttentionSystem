package org.app.infrastructure.local;

import jakarta.enterprise.context.ApplicationScoped;
import org.app.domain.Account;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class InMemoryAccountRepository {
    private List<Account> accounts;

    public InMemoryAccountRepository() {
        this.accounts = new ArrayList<>();
    }

    public void addAccount(Account account) {
        this.accounts.add(account);
    }

    private Optional<Account> filterAccountId(List<Account> accounts, UUID accountId){
        return accounts.stream().filter(account -> account.getId().equals(accountId)).findFirst();
    }

    public Account findAccount(UUID accountId) {
        return filterAccountId(this.accounts, accountId).orElse(null);
    }

    private Optional<Account> filterAccountUsername(List<Account> accounts, String username){
        return accounts.stream().filter(account -> account.getUsername().equals(username)).findFirst();
    }

    public Account findAccountByUsername(String username) {
        return filterAccountUsername(this.accounts, username).orElse(null);
    }

    public void updateAccounts(List<Account> accounts) {
        this.accounts = this.accounts.stream().map(account -> filterAccountId(accounts, account.getId()).orElse(account)).collect(Collectors.toList());
    }

    public void removeAccounts(List<Account> accounts) {
        this.accounts = this.accounts.stream().filter(account -> filterAccountId(accounts, account.getId()).isEmpty()).collect(Collectors.toList());
    }
}
