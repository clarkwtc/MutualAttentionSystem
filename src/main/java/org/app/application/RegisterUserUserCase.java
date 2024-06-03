package org.app.application;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.app.domain.MutualAttentionSystem;
import org.app.domain.User;
import org.app.domain.exceptions.DuplicatedUserException;
import org.app.infrastructure.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@ApplicationScoped
public class RegisterUserUserCase {
    @Inject
    UserRepository userRepository;

    public User execute(String username){
        List<User> users = userRepository.findUserByUsername(username);

        MutualAttentionSystem system = new MutualAttentionSystem();
        system.setUsers(users);
        system.addUser(username);

        User user = system.getUsers().get(0);
        userRepository.registerUser(user);
        return user;
    }
}
