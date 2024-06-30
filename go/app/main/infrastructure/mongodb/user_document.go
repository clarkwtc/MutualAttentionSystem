package mongodb

import (
    "github.com/google/uuid"
    "mutualAttentionSystem/app/main/domain"
    "time"
)

type UserDocument struct {
    ID        string    `bson:"_id"`
    Username  string    `bson:"username"`
    CreatedAt time.Time `bson:"createdAt"`
}

func toUserDomain(userDocument *UserDocument) *domain.User {
    var relationship []*domain.Relationship
    return &domain.User{ID: uuid.MustParse(userDocument.ID), Username: userDocument.Username, Relationships: relationship}
}

func toUserDocument(user *domain.User) *UserDocument {
    return &UserDocument{ID: user.ID.String(), Username: user.Username, CreatedAt: time.Now()}
}
