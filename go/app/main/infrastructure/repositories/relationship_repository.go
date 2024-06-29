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

func (repository *RelationshipRepository) Find(followingId uuid.UUID, fanId uuid.UUID) []*domain.Relationship {
    return repository.access.Find(followingId, fanId)
}

func (repository *RelationshipRepository) Update(relationships []*domain.Relationship) {
    repository.access.Update(relationships)
}

func (repository *RelationshipRepository) Remove(followingId uuid.UUID, fanId uuid.UUID) {
    repository.access.Remove(followingId, fanId)
}

func (repository *RelationshipRepository) Save(relationships []*domain.Relationship) {
    repository.access.Save(relationships)
}
