package org.app.infrastructure.mongodb;

import org.app.domain.User;
import org.bson.Document;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class UserDocument {
    private final Document document;
    private static final String _ID = "_id";
    private static final String USERNAME = "username";
    private static final String CREATED_AT = "createdAt";

    public UserDocument(User user) {
        this.document = new Document();
        setId(user.getId());
        setUsername(user.getUsername());
    }

    public UserDocument(User user, Date createdAt) {
        this.document = new Document();
        setId(user.getId());
        setUsername(user.getUsername());
        setCreatedAt(createdAt);
    }

    public void setId(UUID id){
        if (Objects.isNull(id)){
            return;
        }
        document.append(_ID, id.toString());
    }

    public void setUsername(String username){
        if (Objects.isNull(username)){
            return;
        }
        document.append(USERNAME, username);
    }

    public void setCreatedAt(Date date){
        if (Objects.isNull(date)){
            return;
        }
        document.append(CREATED_AT, date);
    }

    public static Document toNewDocument(User user) {
        return new UserDocument(user, new Date()).document;
    }

    public static Document toDocument(User user){
        return new UserDocument(user).document;
    }

    public static User toDomain(Document document) {
        return new User(UUID.fromString(document.get(_ID).toString()), document.get(USERNAME).toString());
    }
}
