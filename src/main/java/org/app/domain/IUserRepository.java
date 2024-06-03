package org.app.domain;

import org.app.infrastructure.local.UserData;

import java.util.List;
import java.util.UUID;

public interface IUserRepository {
    void register(User user);
    List<User> find(UUID userId);
    List<User> find(List<UUID> userIds);
    List<User> findByUsername(String username);
    void update(List<User> users);
}
