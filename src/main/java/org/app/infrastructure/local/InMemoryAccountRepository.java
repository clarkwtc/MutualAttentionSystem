package org.app.infrastructure.local;

import jakarta.enterprise.context.ApplicationScoped;
import org.app.domain.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class InMemoryAccountRepository {
    private List<User> users;

    public InMemoryAccountRepository() {
        this.users = new ArrayList<>();
    }

    public void addAccount(User user) {
        this.users.add(user);
    }

    private Optional<User> filterAccountId(List<User> users, UUID accountId){
        return users.stream().filter(account -> account.getId().equals(accountId)).findFirst();
    }

    public User findAccount(UUID accountId) {
        return filterAccountId(this.users, accountId).orElse(null);
    }

    private Optional<User> filterAccountUsername(List<User> users, String username){
        return users.stream().filter(account -> account.getUsername().equals(username)).findFirst();
    }

    public User findAccountByUsername(String username) {
        return filterAccountUsername(this.users, username).orElse(null);
    }

    public void updateAccounts(List<User> users) {
        this.users = this.users.stream().map(account -> filterAccountId(users, account.getId()).orElse(account)).collect(Collectors.toList());
    }

    public void removeAccounts(List<User> users) {
        this.users = this.users.stream().filter(account -> filterAccountId(users, account.getId()).isEmpty()).collect(Collectors.toList());
    }
}
