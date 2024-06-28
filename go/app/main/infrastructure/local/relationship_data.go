package local

import (
    "github.com/google/uuid"
    "mutualAttentionSystem/app/main/domain"
    "time"
)

type RelationshipData struct {
    Id          string
    FollowingId string
    FanId       string
    IsFriend    bool
    createdAt   time.Time
}

func toRelationshipDomain(relationshipData *RelationshipData) *domain.Relationship {
    following := domain.User{ID: uuid.MustParse(relationshipData.FollowingId)}
    fan := domain.User{ID: uuid.MustParse(relationshipData.FanId)}
    return &domain.Relationship{ID: uuid.MustParse(relationshipData.Id), Following: &following, Fan: &fan, IsFriend: relationshipData.IsFriend}
}

func toRelationshipData(relationship *domain.Relationship) *RelationshipData {
    return &RelationshipData{relationship.ID.String(), relationship.Following.ID.String(),
        relationship.Fan.ID.String(), relationship.IsFriend, time.Now()}
}
