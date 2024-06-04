package org.app.domain;

import java.util.UUID;

public class Relationship {
    private final UUID id;
    private User following;
    private User fan;
    private boolean isFriend;

    public Relationship(User following, User fan) {
        this.id = UUID.randomUUID();
        this.following = following;
        this.fan = fan;
        makeFriend();
    }

    public Relationship(User following, User fan, boolean isFriend) {
        this.id = UUID.randomUUID();
        this.following = following;
        this.fan = fan;
        this.isFriend = isFriend;
    }

    public UUID getId() {
        return id;
    }

    public User getFollowing() {
        return following;
    }

    public User getFan() {
        return fan;
    }

    public boolean isFriend() {
        return isFriend;
    }

    public void setFollowing(User following) {
        this.following = following;
    }

    public void setFan(User fan) {
        this.fan = fan;
    }

    private void makeFriend(){
        boolean isFollowing = following.isFollowing(fan);
        if (isFollowing){
            following.getRelationship(fan, following).ifPresent(relationship -> relationship.isFriend = true);
        }
        this.isFriend = isFollowing;
    }

    public void unFriend(){
        this.isFriend = false;
    }
}
