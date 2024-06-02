package org.app.domain;

public class Relationship {
    private final User following;
    private final User fan;
    private boolean isFriend;

    public Relationship(User following, User fan) {
        this.following = following;
        this.fan = fan;
        makeFriend();
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
