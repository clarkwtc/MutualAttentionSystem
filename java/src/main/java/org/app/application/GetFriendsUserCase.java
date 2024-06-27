package org.app.application;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.app.domain.MutualAttentionSystem;
import org.app.domain.Relationship;
import org.app.domain.User;
import org.app.domain.events.FanEvent;
import org.app.domain.events.FriendEvent;
import org.app.infrastructure.repositories.RelationshipRepository;
import org.app.infrastructure.repositories.UserRepository;
import org.app.infrastructure.utils.Pageable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@ApplicationScoped
public class GetFriendsUserCase {
    @Inject
    UserRepository userRepository;
    @Inject
    RelationshipRepository relationshipRepository;

    public FriendEvent execute(String userId, int page, int limit){
        List<User> users = userRepository.find(UUID.fromString(userId));
        MutualAttentionSystem system = new MutualAttentionSystem();
        system.setUsers(users);

        List<Relationship> relationships = relationshipRepository.findByUserId(UUID.fromString(userId));
        setUserInfoInRelationship(userRepository.find(toUUIDList(relationships, userId)), relationships);

        User user = system.getUser(userId);
        user.setRelationships(relationships);

        List<User> friends = user.getFriends();
        Pageable pageable = new Pageable(page, limit, friends.size());
        friends = friends.isEmpty() ? friends: friends.subList(pageable.getOffset(), pageable.getEnd());

        return new FriendEvent(friends, page, limit, pageable.getTotalPage());
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
