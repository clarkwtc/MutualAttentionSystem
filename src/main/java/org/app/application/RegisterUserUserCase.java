package org.app.application;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.app.domain.MutualAttentionSystem;
import org.app.domain.User;
import org.app.infrastructure.repositories.UserRepository;

import java.util.List;

@ApplicationScoped
public class RegisterUserUserCase {
    @Inject
    UserRepository userRepository;

    public User execute(String username){
        List<User> users = userRepository.findByUsername(username);

        MutualAttentionSystem system = new MutualAttentionSystem();
        system.setUsers(users);
        system.addUser(username);

        User user = system.getUsers().get(0);
        userRepository.register(user);
        return user;
    }
}
