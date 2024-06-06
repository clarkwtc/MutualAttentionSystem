package org.app.infrastructure.repositories;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.app.domain.IRelationshipRepository;
import org.app.domain.Relationship;
import org.app.infrastructure.local.InMemoryRelationshipRepository;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class RelationshipRepository implements IRelationshipRepository {
    @Inject
    InMemoryRelationshipRepository relationshipRepository;

    @Override
    public List<Relationship> find(UUID followingId, UUID fanId) {
        return relationshipRepository.find(followingId, fanId);
    }

    @Override
    public List<Relationship> findByFollowingId(UUID followingId, UUID fanId) {
        return relationshipRepository.findByFollowingId(followingId, fanId);
    }

    @Override
    public List<Relationship> findByUserId(UUID userId) {
        return relationshipRepository.findByUserId(userId);
    }

    @Override
    public void addAll(List<Relationship> relationships) {
        relationshipRepository.addAll(relationships);
    }


    @Override
    public void removeAll(List<Relationship> relationships) {
        relationshipRepository.removeAll(relationships);
    }
}
