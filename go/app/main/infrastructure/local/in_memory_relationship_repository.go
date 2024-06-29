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

func filterFollowingOrFanWithRelationship(relationshipData *[]*RelationshipData, relationships []*RelationshipData, userId string) {
    for _, relationship := range relationships {
        if relationship.FanId == userId || relationship.FollowingId == userId {
            *relationshipData = append(*relationshipData, relationship)
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

func (repository *InMemoryRelationshipRepository) FindUserId(id uuid.UUID) []*domain.Relationship {
    var relationshipsData []*RelationshipData
    filterFollowingOrFanWithRelationship(&relationshipsData, repository.relationships, id.String())
    return toRelationshipsDomain(relationshipsData)
}

func filterFollowingAndFanWithRelationship(relationshipData *[]*RelationshipData, relationships []*RelationshipData, followingId string, fanId string) {
    for _, relationship := range relationships {
        if relationship.FollowingId == followingId && relationship.FanId == fanId ||
            relationship.FollowingId == fanId && relationship.FanId == followingId {
            *relationshipData = append(*relationshipData, relationship)
        }

        if len(*relationshipData) == 2 {
            return
        }
    }
}

func (repository *InMemoryRelationshipRepository) Find(followingId uuid.UUID, fanId uuid.UUID) []*domain.Relationship {
    var relationshipsData []*RelationshipData
    filterFollowingAndFanWithRelationship(&relationshipsData, repository.relationships, followingId.String(), fanId.String())
    return toRelationshipsDomain(relationshipsData)
}

func toRelationshipsData(relationships []*domain.Relationship) []*RelationshipData {
    var relationshipsData []*RelationshipData
    for _, relationship := range relationships {
        relationshipsData = append(relationshipsData, toRelationshipData(relationship))
    }
    return relationshipsData
}

func filterRelationshipById(selfRelatinoships []*RelationshipData, relationshipId string) int {
    for index, relationship := range selfRelatinoships {
        if relationship.Id == relationshipId {
            return index
        }
    }
    return -1
}

func (repository *InMemoryRelationshipRepository) Update(relationships []*domain.Relationship) {
    relationshipsData := toRelationshipsData(relationships)
    for _, relationshipData := range relationshipsData {
        index := filterRelationshipById(repository.relationships, relationshipData.Id)
        if index == -1 {
            repository.relationships = append(repository.relationships, relationshipData)
        } else {
            repository.relationships[index] = relationshipData
        }
    }
}

func (repository *InMemoryRelationshipRepository) Remove(followingId uuid.UUID, fanId uuid.UUID) {
    for index, relationship := range repository.relationships {
        if relationship.FollowingId == followingId.String() && relationship.FanId == fanId.String() {
            repository.relationships = append(repository.relationships[:index], repository.relationships[index+1:]...)
        }
    }
}

func (repository *InMemoryRelationshipRepository) Save(relationships []*domain.Relationship) {
    relationshipsData := toRelationshipsData(relationships)
    repository.relationships = append(repository.relationships, relationshipsData...)
}
