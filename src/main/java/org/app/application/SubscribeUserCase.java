package org.app.application;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.app.domain.MutualAttentionSystem;
import org.app.domain.User;
import org.app.domain.exceptions.NotExistUserException;
import org.app.infrastructure.repositories.UserRepository;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@ApplicationScoped
public class SubscribeUserCase {
    @Inject
    UserRepository userRepository;

    public void execute(String userId, String followingId){
        List<User> users = userRepository.findUser(UUID.fromString(userId));
        List<User> followings = userRepository.findUser(UUID.fromString(followingId));

        MutualAttentionSystem system = new MutualAttentionSystem();
        system.addUserAll(users);
        system.addUserAll(followings);

        User user = system.getUser(userId);
        User following = system.getUser(followingId);
        user.subscribe(following);

        userRepository.updateUsers(system.getUsers());
    }
}
