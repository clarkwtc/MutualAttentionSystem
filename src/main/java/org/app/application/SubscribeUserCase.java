package org.app.application;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.app.domain.MutualAttentionSystem;
import org.app.domain.Relationship;
import org.app.domain.User;
import org.app.infrastructure.repositories.RelationshipRepository;
import org.app.infrastructure.repositories.UserRepository;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class SubscribeUserCase {
    @Inject
    UserRepository userRepository;
    @Inject
    RelationshipRepository relationshipRepository;

    public void execute(String userId, String followingId){
        List<User> users = userRepository.find(UUID.fromString(userId));
        List<User> followings = userRepository.find(UUID.fromString(followingId));

        MutualAttentionSystem system = new MutualAttentionSystem();
        system.addUserAll(users);
        system.addUserAll(followings);

        User user = system.getUser(userId);
        User following = system.getUser(followingId);
        List<Relationship> relationships = relationshipRepository.find(UUID.fromString(userId), UUID.fromString(userId));
        user.setRelationships(relationships);
        following.setRelationships(relationships);

        user.subscribe(following);
        relationshipRepository.addAll(user.getRelationships());
    }
}
