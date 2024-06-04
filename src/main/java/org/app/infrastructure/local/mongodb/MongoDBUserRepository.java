package org.app.infrastructure.local.mongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.app.domain.IUserRepository;
import org.app.domain.User;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class MongoDBUserRepository implements IUserRepository {
    @Inject
    MongoClient mongoClient;

    private MongoCollection<Document> getCollection(){
        return mongoClient.getDatabase("mutual_attention").getCollection("User");
    }

    @Override
    public void register(User user) {
        Document query = UserDocument.toNewDocument(user);
        getCollection().insertOne(query);
    }

    @Override
    public List<User> find(UUID userId) {
        Document query = UserDocument.toDocument(new User(userId));
        return getCollection().find(query).map(UserDocument::toDomain).into(new ArrayList<>());
    }

    @Override
    public List<User> find(List<UUID> userIds) {
        List<String> ids = userIds.stream().map(UUID::toString).collect(Collectors.toList());
        return getCollection().find(Filters.in("_id", ids)).map(UserDocument::toDomain).into(new ArrayList<>());
    }

    @Override
    public List<User> findByUsername(String username) {
        Document query = UserDocument.toDocument(new User(null, username));
        return getCollection().find(query).map(UserDocument::toDomain).into(new ArrayList<>());
    }

    public void removeUsers(List<User> users) {
        List<WriteModel<?>> list = Arrays.asList(
                new UpdateOneModel<>(new Document("name", "A Sample Movie"),
                        new Document("$set", new Document("name", "An Old Sample Movie")),
                        new UpdateOptions().upsert(false)),
                new DeleteOneModel<>(new Document("name", "Yet Another Sample Movie")),
                new ReplaceOneModel<>(new Document("name", "Yet Another Sample Movie"),
                        new Document("name", "The Other Sample Movie").append("runtime", "42"))
        );
        getCollection()
                .bulkWrite(
                        Arrays.asList(
                                new UpdateOneModel<>(new Document("name", "A Sample Movie"),
                                        new Document("$set", new Document("name", "An Old Sample Movie")),
                                        new UpdateOptions().upsert(false)),
                                new DeleteOneModel<>(new Document("name", "Yet Another Sample Movie")),
                                new ReplaceOneModel<>(new Document("name", "Yet Another Sample Movie"),
                                        new Document("name", "The Other Sample Movie").append("runtime",  "42"))
                        ));
    }

}
