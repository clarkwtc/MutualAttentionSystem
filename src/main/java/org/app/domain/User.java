package org.app.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class User {
    private final UUID id;
    private final String username;
    private List<Relationship> relationships;

    public User(UUID id,String username) {
        this.id = id;
        this.username = username;
        this.relationships = new ArrayList<>();
    }

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

    public void setRelationships(List<Relationship> relationships) {
        this.relationships.addAll(relationships);
    }

    public List<Relationship> getRelationships() {
        return relationships;
    }

    public List<User> getFriends() {
        return relationships.stream().filter(relationship -> relationship.isFriend() && relationship.getFan().getId().equals(this.id)).map(Relationship::getFollowing).collect(Collectors.toList());
    }

    public List<User> getFollowings() {
        return relationships.stream().filter(relationship -> relationship.getFan().getId().equals(this.id)).map(Relationship::getFollowing).collect(Collectors.toList());
    }

    public List<User> getFans() {
        return relationships.stream().filter(relationship -> relationship.getFollowing().getId().equals(this.id)).map(Relationship::getFan).collect(Collectors.toList());
    }

    public boolean isFollowing(User following){
        return this.relationships.stream().anyMatch(relationship -> relationship.getFollowing().getId().equals(following.getId()) && relationship.getFan().getId().equals(this.id));
    }

    public boolean isFan(User fan){
        return this.relationships.stream().anyMatch(relationship -> relationship.getFollowing().getId().equals(this.id) && relationship.getFan().getId().equals(fan.getId()));
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
        return this.relationships.stream().filter(relationship -> relationship.getFollowing().getId().equals(following.getId()) && relationship.getFan().getId().equals(fan.getId())).findFirst();
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
