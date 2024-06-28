package repositories

import (
    "github.com/google/uuid"
    "mutualAttentionSystem/app/main/domain"
    "mutualAttentionSystem/app/main/infrastructure/local"
)

type RelationshipRepository struct {
    access *local.InMemoryRelationshipRepository
}

func NewRelationshipRepository() domain.IRelationshipRepository {
    return &RelationshipRepository{local.NewInMemoryRelationshipRepository()}
}

func (repository *RelationshipRepository) FindUserId(userId uuid.UUID) []*domain.Relationship {
    return repository.access.FindUserId(userId)
}
