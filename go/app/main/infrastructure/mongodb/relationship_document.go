package mongodb

import (
    "github.com/google/uuid"
    "mutualAttentionSystem/app/main/domain"
    "time"
)

type RelationDocument struct {
    ID          string    `bson:"_id"`
    FollowingId string    `bson:"followingId"`
    FanId       string    `bson:"fanId"`
    IsFriend    bool      `bson:"isFriend"`
    CreatedAt   time.Time `bson:"createdAt"`
}

func toRelationshipDomain(relationDocument *RelationDocument) *domain.Relationship {
    following := domain.User{ID: uuid.MustParse(relationDocument.FollowingId), Username: ""}
    fan := domain.User{ID: uuid.MustParse(relationDocument.FanId), Username: ""}
    return &domain.Relationship{ID: uuid.MustParse(relationDocument.ID), Following: &following, Fan: &fan, IsFriend: relationDocument.IsFriend}
}

func toRelationshipDocument(relationship *domain.Relationship) *RelationDocument {
    return &RelationDocument{relationship.ID.String(), relationship.Following.ID.String(), relationship.Fan.ID.String(), relationship.IsFriend, time.Now()}
}
