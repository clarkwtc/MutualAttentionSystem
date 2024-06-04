package org.app.infrastructure.repositories;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.app.domain.User;
import org.app.domain.IUserRepository;
import org.app.infrastructure.local.inmemory.InMemoryUserRepository;
import org.app.infrastructure.local.mongodb.MongoDBUserRepository;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class UserRepository implements IUserRepository {
    @Inject
    InMemoryUserRepository userRepository;

    @Override
    public void register(User user) {
        userRepository.register(user);
    }

    @Override
    public List<User> find(UUID userId) {
        return userRepository.find(userId);
    }

    @Override
    public List<User> find(List<UUID> userIds) {
        return userRepository.find(userIds);
    }

    @Override
    public List<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
