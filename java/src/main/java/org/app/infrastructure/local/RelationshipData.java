package org.app.infrastructure.local;

import org.app.domain.Relationship;
import org.app.domain.User;

import java.util.Date;
import java.util.UUID;

public class RelationshipData {
    private final String followingId;
    private final String fanId;
    private boolean isFriend;
    private final Date createdAt;

    public RelationshipData(String followingId, String fanId, boolean isFriend) {
        this.followingId = followingId;
        this.fanId = fanId;
        this.isFriend = isFriend;
        this.createdAt = new Date();
    }

    public String getFollowingId() {
        return followingId;
    }

    public String getFanId() {
        return fanId;
    }

    public boolean isFriend() {
        return isFriend;
    }

    public static RelationshipData toData(Relationship relationship){
        return new RelationshipData(relationship.getFollowing().getId().toString(), relationship.getFan().getId().toString(), relationship.isFriend());
    }

    public static Relationship toDomain(RelationshipData relationshipData){
        return new Relationship(new User(UUID.fromString(relationshipData.getFollowingId()), null), new User(UUID.fromString(relationshipData.getFanId()), null), relationshipData.isFriend);
    }
}
