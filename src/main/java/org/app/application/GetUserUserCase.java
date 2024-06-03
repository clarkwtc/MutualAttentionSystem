package org.app.application;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.app.domain.MutualAttentionSystem;
import org.app.domain.User;
import org.app.infrastructure.repositories.UserRepository;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class GetUserUserCase {
    @Inject
    UserRepository userRepository;

    public User execute(String userId){
        List<User> users = userRepository.findUser(UUID.fromString(userId));
        MutualAttentionSystem system = new MutualAttentionSystem();
        system.setUsers(users);
        return system.getUser(userId);
    }
}
