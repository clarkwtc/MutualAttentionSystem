package repositories

import (
    "github.com/google/uuid"
    "mutualAttentionSystem/app/main/domain"
)

type RelationshipRepository struct {
    access domain.IRelationshipRepository
}

func NewRelationshipRepository(access domain.IRelationshipRepository) domain.IRelationshipRepository {
    return &RelationshipRepository{access}
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
