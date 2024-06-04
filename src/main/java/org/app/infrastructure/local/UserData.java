package org.app.infrastructure.local;

import org.app.domain.User;

import java.util.Date;
import java.util.UUID;

public class UserData{
    private final String id;
    private final String username;
    private final Date createdAt;

    public UserData(String id, String username) {
        this.id = id;
        this.username = username;
        this.createdAt = new Date();
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public static UserData toData(User user){
        return new UserData(user.getId().toString(), user.getUsername());
    }

    public static User toDomain(UserData userData){
        return new User(UUID.fromString(userData.id), userData.username);
    }
}
