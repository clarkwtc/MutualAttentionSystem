package org.app.domain;

import java.util.List;
import java.util.UUID;

public interface IRelationshipRepository {
    List<Relationship> find(UUID followingId, UUID fanId);
    List<Relationship> findByFollowingId(UUID followingId, UUID fanId);
    List<Relationship> findByUserId(UUID userId);
    void addAll(List<Relationship> relationships);
    void removeAll(List<Relationship> relationships);
}
