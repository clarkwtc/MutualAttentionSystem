package org.app.domain;

import org.app.domain.exceptions.DuplicatedUserException;
import org.app.domain.exceptions.NotExistUserException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MutualAttentionSystem {
    private List<User> users;

    public MutualAttentionSystem() {
        this.users = new ArrayList<>();
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public void addUser(String username){
        if (users.stream().anyMatch(user -> user.getUsername().equals(username))){
            throw new DuplicatedUserException();
        }
        users.add(new User(username));
    }

    public void addUserAll(List<User> users){
        List<User> newUsers = users.stream().filter(user -> !this.users.contains(user)).collect(Collectors.toList());
        this.users.addAll(newUsers);
    }

    public User getUser(String userId){
        Optional<User> user = users.stream().filter(tempUser -> tempUser.getId().toString().equals(userId)).findFirst();
        if (user.isEmpty()){
            throw new NotExistUserException();
        }
        return user.get();
    }
}
