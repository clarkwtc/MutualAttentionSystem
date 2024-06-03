package org.app.domain;

import java.util.List;
import java.util.UUID;

public interface IUserRepository {
    void registerUser(User user);

    List<User> findUser(UUID userId);

    List<User> findUserByUsername(String username);

    void updateUsers(List<User> users);
}
