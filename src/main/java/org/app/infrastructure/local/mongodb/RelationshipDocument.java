package org.app.infrastructure.local.mongodb;

import org.app.domain.Relationship;
import org.app.domain.User;
import org.bson.Document;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class RelationshipDocument {
    private final Document document;
    private static final String _ID = "_id";
    private static final String FOLLOWING_ID = "followingId";
    private static final String FAN_ID = "fanId";
    private static final String IS_FRIEND = "isFriend";
    private static final String CREATED_AT = "createdAt";

    public RelationshipDocument(Relationship relationship) {
        this.document = new Document();
        setId(relationship.getId());
        setFollowingId(relationship.getFollowing().getId());
        setFanId(relationship.getFan().getId());
        setIsFriend(relationship.isFriend());
    }

    public RelationshipDocument(Relationship relationship, Date createdAt) {
        this.document = new Document();
        setId(relationship.getId());
        setFollowingId(relationship.getFollowing().getId());
        setFanId(relationship.getFan().getId());
        setIsFriend(relationship.isFriend());
        setCreatedAt(createdAt);
    }

    private void setId(UUID id) {
        if (Objects.isNull(id)){
            return;
        }
        document.append(_ID, id.toString());
    }

    private void setFollowingId(UUID id) {
        if (Objects.isNull(id)){
            return;
        }
        document.append(FOLLOWING_ID, id.toString());
    }

    private void setFanId(UUID id) {
        if (Objects.isNull(id)){
            return;
        }
        document.append(FAN_ID, id.toString());
    }

    private void setIsFriend(boolean friend) {
        document.append(IS_FRIEND, friend);
    }

    private void setCreatedAt(Date date) {
        if (Objects.isNull(date)){
            return;
        }
        document.append(CREATED_AT, date);
    }

    public static Document toNewDocument(Relationship relationship) {
        return new RelationshipDocument(relationship, new Date()).document;
    }

    public static Document toDocument(Relationship relationship){
        return new RelationshipDocument(relationship).document;
    }

    public static Relationship toDomain(Document document) {
        return new Relationship(new User(UUID.fromString(document.get(FOLLOWING_ID).toString())), new User(UUID.fromString(document.get(FAN_ID).toString())), document.getBoolean(IS_FRIEND));
    }
}
