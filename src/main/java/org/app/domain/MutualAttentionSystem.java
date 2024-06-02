package org.app.domain;

import java.util.ArrayList;
import java.util.List;

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
            return;
        }
        users.add(new User(username));
    }
}
