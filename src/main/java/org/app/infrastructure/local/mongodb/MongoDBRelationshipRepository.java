package org.app.infrastructure.local.mongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.DeleteOneModel;
import com.mongodb.client.model.InsertOneModel;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.app.domain.IRelationshipRepository;
import org.app.domain.Relationship;
import org.bson.Document;

import java.util.*;

@ApplicationScoped
public class MongoDBRelationshipRepository implements IRelationshipRepository {
    @Inject
    MongoClient mongoClient;

    private MongoCollection<Document> getCollection(){
        return mongoClient.getDatabase("mutual_attention").getCollection("Relationship");
    }

    @Override
    public List<Relationship> find(UUID followingId, UUID fanId) {
        Document query = new Document().append("followingId", followingId.toString()).append("fanId", fanId.toString());
        Document query2 = new Document().append("followingId", fanId.toString()).append("fanId", followingId.toString());
        ArrayList<Relationship> relationships = getCollection().find(query).map(RelationshipDocument::toDomain).into(new ArrayList<>());
        ArrayList<Relationship> relationships2 = getCollection().find(query2).map(RelationshipDocument::toDomain).into(new ArrayList<>());
        relationships.addAll(relationships2);
        return relationships;
    }

    @Override
    public List<Relationship> findByFollowingId(UUID followingId, UUID fanId) {
        Document query = new Document().append("followingId", followingId.toString()).append("fanId", fanId.toString());
        return getCollection().find(query).map(RelationshipDocument::toDomain).into(new ArrayList<>());
    }


    @Override
    public List<Relationship> findByUserId(UUID userId) {
        Document query = new Document().append("followingId", userId.toString());
        Document query2 = new Document().append("fanId", userId.toString());
        ArrayList<Relationship> relationships = getCollection().find(query).map(RelationshipDocument::toDomain).into(new ArrayList<>());
        ArrayList<Relationship> relationships2 = getCollection().find(query2).map(RelationshipDocument::toDomain).into(new ArrayList<>());
        relationships.addAll(relationships2);
        return relationships;
    }

    @Override
    public void addAll(List<Relationship> relationships) {
        List<DeleteOneModel<Document>> bulk = new ArrayList<>();
        for (Relationship relationship: relationships){
            DeleteOneModel<Document> deleteOneModel = new DeleteOneModel<>(new Document("followingId", relationship.getFollowing().getId().toString()).append("fanId", relationship.getFan().getId().toString()));
            bulk.add(deleteOneModel);
        }
        if (!bulk.isEmpty()){
            getCollection().bulkWrite(bulk);
        }

        List<InsertOneModel<Document>> bulk2 = new ArrayList<>();
        for (Relationship relationship: relationships){
            Document document = RelationshipDocument.toNewDocument(relationship);
            bulk2.add(new InsertOneModel<>(document));
        }
        if (!bulk2.isEmpty()){
            getCollection().bulkWrite(bulk2);
        }
    }


    @Override
    public void removeAll(List<Relationship> relationships) {
        List<DeleteOneModel<Document>> bulk = new ArrayList<>();
        for (Relationship relationship: relationships){
            DeleteOneModel<Document> deleteOneModel = new DeleteOneModel<>(new Document("followingId", relationship.getFollowing().getId().toString()).append("fanId", relationship.getFan().getId().toString()));
            bulk.add(deleteOneModel);
        }
        getCollection().bulkWrite(bulk);
    }
}
