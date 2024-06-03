package org.app.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class User {
    private final UUID id;
    private final String username;
    private final List<Relationship> relationships;

    public User(String username){
        this.id = UUID.randomUUID();
        this.username = username;
        this.relationships = new ArrayList<>();
    }

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public List<User> getFriends() {
        return relationships.stream().filter(relationship -> relationship.isFriend() && relationship.getFan().equals(this)).map(Relationship::getFollowing).collect(Collectors.toList());
    }

    public List<User> getFollowings() {
        return relationships.stream().filter(relationship -> relationship.getFan().equals(this)).map(Relationship::getFollowing).collect(Collectors.toList());
    }

    public List<User> getFans() {
        return relationships.stream().filter(relationship -> relationship.getFollowing().equals(this)).map(Relationship::getFan).collect(Collectors.toList());
    }

    public boolean isFollowing(User following){
        return this.relationships.stream().anyMatch(relationship -> relationship.getFollowing().equals(following) && relationship.getFan().equals(this));
    }

    public boolean isFan(User fan){
        return this.relationships.stream().anyMatch(relationship -> relationship.getFollowing().equals(this) && relationship.getFan().equals(fan));
    }

    public void subscribe(User user){
        if (!user.equals(this) && isFollowing(user)){
            return;
        }

        Relationship relationship = new Relationship(user, this);
        user.relationships.add(relationship);
        relationships.add(relationship);
    }

    public Optional<Relationship> getRelationship(User following, User fan){
        return this.relationships.stream().filter(relationship -> relationship.getFollowing().equals(following) && relationship.getFan().equals(fan)).findFirst();
    }

    public void unsubscribe(User user){
        if (!user.equals(this) && !isFollowing(user)){
            return;
        }

        Relationship relationship = getRelationship(user, this).orElseThrow();
        user.relationships.remove(relationship);
        this.relationships.remove(relationship);
        user.getRelationship(this, user).ifPresent(Relationship::unFriend);
    }

    public boolean isFriend(User user){
        return getRelationship(user, this).map(Relationship::isFriend).orElse(false);
    }
}
