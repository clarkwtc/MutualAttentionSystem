package local

import (
    "github.com/google/uuid"
    "mutualAttentionSystem/app/main/domain"
)

type InMemoryRelationshipRepository struct {
    relationships []*RelationshipData
}

func NewInMemoryRelationshipRepository() *InMemoryRelationshipRepository {
    return &InMemoryRelationshipRepository{}
}

func filterFollowingOrFanWithRelationship(relationshipData []*RelationshipData, relationships []*RelationshipData, userId string) {
    for _, relationship := range relationships {
        if relationship.FanId == userId || relationship.FollowingId == userId {
            relationshipData = append(relationshipData, relationship)
        }
    }
}

func toRelationshipsDomain(relationshipsData []*RelationshipData) []*domain.Relationship {
    var relationship []*domain.Relationship
    for _, relationshipData := range relationshipsData {
        relationship = append(relationship, toRelationshipDomain(relationshipData))
    }
    return relationship
}

func (repository InMemoryRelationshipRepository) FindUserId(id uuid.UUID) []*domain.Relationship {
    var relationshipsData []*RelationshipData
    filterFollowingOrFanWithRelationship(relationshipsData, repository.relationships, id.String())
    return toRelationshipsDomain(relationshipsData)
}
