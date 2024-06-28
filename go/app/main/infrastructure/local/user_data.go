package local

import (
    "github.com/google/uuid"
    "mutualAttentionSystem/app/main/domain"
    "time"
)

type UserData struct {
    ID        string
    Username  string
    CreatedAt time.Time
}

func toDomain(userData *UserData) *domain.User {
    var relationship []*domain.Relationship
    return &domain.User{ID: uuid.MustParse(userData.ID), Username: userData.Username, Relationships: relationship}
}

func toData(user *domain.User) *UserData {
    return &UserData{ID: user.ID.String(), Username: user.Username, CreatedAt: time.Now()}
}
