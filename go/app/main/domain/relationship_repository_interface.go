package domain

import "github.com/google/uuid"

type IRelationshipRepository interface {
    FindUserId(userId uuid.UUID) []*Relationship
}
