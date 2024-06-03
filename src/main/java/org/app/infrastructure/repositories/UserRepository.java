package org.app.infrastructure.repositories;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.app.domain.User;
import org.app.domain.IUserRepository;
import org.app.infrastructure.local.InMemoryUserRepository;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class UserRepository implements IUserRepository {
    @Inject
    InMemoryUserRepository inMemoryUserRepository;

    @Override
    public void registerUser(User user) {
        inMemoryUserRepository.addUser(user);
    }

    @Override
    public List<User> findUser(UUID userId) {
        return inMemoryUserRepository.findUser(userId);
    }

    @Override
    public List<User> findUserByUsername(String username) {
        return inMemoryUserRepository.findUserByUsername(username);
    }

    @Override
    public void updateUsers(List<User> users) {
        inMemoryUserRepository.updateUsers(users);
    }
}
