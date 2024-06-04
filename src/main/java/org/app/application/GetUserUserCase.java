package org.app.application;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.app.domain.MutualAttentionSystem;
import org.app.domain.Relationship;
import org.app.domain.User;
import org.app.infrastructure.repositories.RelationshipRepository;
import org.app.infrastructure.repositories.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@ApplicationScoped
public class GetUserUserCase {
    @Inject
    UserRepository userRepository;
    @Inject
    RelationshipRepository relationshipRepository;

    public User execute(String userId){
        List<User> users = userRepository.find(UUID.fromString(userId));
        MutualAttentionSystem system = new MutualAttentionSystem();

        List<Relationship> relationships = relationshipRepository.findByUserId(UUID.fromString(userId));
        setUserInfoInRelationship(userRepository.find(toUUIDList(relationships, userId)), relationships);

        system.setUsers(users);
        User user = system.getUser(userId);
        user.setRelationships(relationships);
        return system.getUser(userId);
    }

    private List<UUID> toUUIDList(List<Relationship> relationships, String userId){
        Set<UUID> userIds = new HashSet<>();
        for (Relationship relationship: relationships){
            if(!relationship.getFan().getId().toString().equals(userId)){
                userIds.add(relationship.getFan().getId());
            }
            if(!relationship.getFollowing().getId().toString().equals(userId)){
                userIds.add(relationship.getFollowing().getId());
            }
        }

        return userIds.stream().toList();
    }

    private void setUserInfoInRelationship(List<User> users, List<Relationship> relationships){
        for (Relationship relationship: relationships){
            users.stream().filter(user -> user.getId().equals(relationship.getFan().getId())).findFirst().ifPresent(relationship::setFan);
            users.stream().filter(user -> user.getId().equals(relationship.getFollowing().getId())).findFirst().ifPresent(relationship::setFollowing);
        }
    }
}
