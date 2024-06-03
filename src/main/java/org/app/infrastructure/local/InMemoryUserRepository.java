package org.app.infrastructure.local;

import jakarta.enterprise.context.ApplicationScoped;
import org.app.domain.IUserRepository;
import org.app.domain.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ApplicationScoped
public class InMemoryUserRepository implements IUserRepository {
    private List<UserData> users;

    public InMemoryUserRepository() {
        this.users = new ArrayList<>();
    }

    @Override
    public void register(User user) {
        UserData userData = UserData.toData(user);
        this.users.add(userData);
    }

    private Stream<UserData> filterUserId(List<UserData> users, String userId){
        return users.stream().filter(user -> user.getId().equals(userId));
    }

    @Override
    public List<User> find(UUID userId) {
        List<UserData> userData = filterUserId(this.users, userId.toString()).collect(Collectors.toList());
        return userData.stream().map(UserData::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<User> find(List<UUID> userIds) {
        return users.stream().filter(user -> userIds.stream().anyMatch(userId -> user.getId().equals(userId.toString()))).map(UserData::toDomain).collect(Collectors.toList());
    }

    private List<UserData> filterUserUsername(List<UserData> users, String username){
        return users.stream().filter(user -> user.getUsername().equals(username)).collect(Collectors.toList());
    }

    @Override
    public List<User> findByUsername(String username) {
        List<UserData> userData = filterUserUsername(this.users, username);
        return userData.stream().map(UserData::toDomain).collect(Collectors.toList());
    }

    @Override
    public void update(List<User> users) {
        List<UserData> userData = users.stream().map(UserData::toData).collect(Collectors.toList());
        this.users = this.users.stream().map(user -> filterUserId(userData, user.getId()).findFirst().orElse(user)).collect(Collectors.toList());
    }

    public void removeUsers(List<User> users) {
        List<UserData> userData = users.stream().map(UserData::toData).collect(Collectors.toList());
        this.users = this.users.stream().filter(user -> filterUserId(userData, user.getId()).findFirst().isEmpty()).collect(Collectors.toList());
    }
}
