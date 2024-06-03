package org.app.infrastructure.local;

import jakarta.enterprise.context.ApplicationScoped;
import org.app.domain.IRelationshipRepository;
import org.app.domain.Relationship;

import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
public class InMemoryRelationshipRepository implements IRelationshipRepository {
    List<RelationshipData> relationships;

    public InMemoryRelationshipRepository() {
        this.relationships = new ArrayList<>();
    }

    private boolean matchFollowingAndFanWithRelationship(RelationshipData relationship, UUID followingId, UUID fanId){
        return relationship.getFollowingId().equals(followingId.toString()) && relationship.getFanId().equals(fanId.toString()) ||
                relationship.getFollowingId().equals(fanId.toString()) && relationship.getFanId().equals(followingId.toString());
    }

    private List<Relationship> toRelationships(List<RelationshipData> relationshipData){
        return relationshipData.stream().map(RelationshipData::toDomain).collect(Collectors.toList());
    }

    private List<RelationshipData> toRelationshipsData(List<Relationship> relationships){
        return relationships.stream().map(RelationshipData::toData).collect(Collectors.toList());
    }

    @Override
    public List<Relationship> find(UUID followingId, UUID fanId) {
        List<RelationshipData> relationshipData = relationships.stream().filter(relationship -> matchFollowingAndFanWithRelationship(relationship, followingId, fanId)).collect(Collectors.toList());
        return toRelationships(relationshipData);
    }

    private boolean matchRelationship(RelationshipData relationship, String followingId, String fanId){
        return relationship.getFollowingId().equals(followingId) && relationship.getFanId().equals(fanId);
    }

    @Override
    public List<Relationship> findByFollowingId(UUID followingId, UUID fanId) {
        List<RelationshipData> relationshipData = relationships.stream().filter(relationship -> matchRelationship(relationship, followingId.toString(), fanId.toString())).collect(Collectors.toList());
        return toRelationships(relationshipData);
    }

    private boolean matchFollowingOrFanWithRelationship(RelationshipData relationship, UUID userId){
        return relationship.getFollowingId().equals(userId.toString()) || relationship.getFanId().equals(userId.toString());
    }

    @Override
    public List<Relationship> findByUserId(UUID userId) {
        List<RelationshipData> relationshipData = relationships.stream().filter(relationship -> matchFollowingOrFanWithRelationship(relationship, userId)).collect(Collectors.toList());
        return toRelationships(relationshipData);
    }

    @Override
    public void addAll(List<Relationship> relationships) {
        this.relationships.addAll(toRelationshipsData(relationships));
    }

    private Optional<RelationshipData> getRelationship(List<RelationshipData> relationshipsData, RelationshipData targetRelationship){
        return relationshipsData.stream().filter(relationship -> matchRelationship(relationship, targetRelationship.getFollowingId(), targetRelationship.getFanId())).findFirst();
    }

    @Override
    public void update(List<Relationship> relationships) {
        this.relationships = this.relationships.stream().map(relationship -> getRelationship(toRelationshipsData(relationships), relationship).orElse(relationship)).collect(Collectors.toList());
    }

    @Override
    public void remove(List<Relationship> relationships) {
        this.relationships = this.relationships.stream().filter(relationship -> getRelationship(toRelationshipsData(relationships), relationship).isEmpty()).collect(Collectors.toList());
    }
}
