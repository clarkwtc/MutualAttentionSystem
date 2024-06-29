package domain

import "github.com/google/uuid"

type IRelationshipRepository interface {
    FindUserId(userId uuid.UUID) []*Relationship
    Find(followingId uuid.UUID, fanId uuid.UUID) []*Relationship
    Update(relationships []*Relationship)
}
