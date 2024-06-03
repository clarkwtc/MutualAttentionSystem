package org.app.infrastructure.local;

import jakarta.enterprise.context.ApplicationScoped;
import org.app.domain.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ApplicationScoped
public class InMemoryUserRepository {
    private List<User> users;

    public InMemoryUserRepository() {
        this.users = new ArrayList<>();
    }

    public void addUser(User user) {
        this.users.add(user);
    }

    private Stream<User> filterUserId(List<User> users, UUID userId){
        return users.stream().filter(account -> account.getId().equals(userId));
    }

    public List<User> findUser(UUID userId) {
        return filterUserId(this.users, userId).collect(Collectors.toList());
    }

    private List<User> filterUserUsername(List<User> users, String username){
        return users.stream().filter(account -> account.getUsername().equals(username)).collect(Collectors.toList());
    }

    public List<User> findUserByUsername(String username) {
        return filterUserUsername(this.users, username);
    }

    public void updateUsers(List<User> users) {
        this.users = this.users.stream().map(account -> filterUserId(users, account.getId()).findFirst().orElse(account)).collect(Collectors.toList());
    }

    public void removeUsers(List<User> users) {
        this.users = this.users.stream().filter(account -> filterUserId(users, account.getId()).findFirst().isEmpty()).collect(Collectors.toList());
    }
}
