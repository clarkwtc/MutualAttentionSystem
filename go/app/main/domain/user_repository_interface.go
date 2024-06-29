package domain

import "github.com/google/uuid"

type IUserRepository interface {
    FindByUsername(username string) *User
    Find(id uuid.UUID) *User
    FindByIds(ids []uuid.UUID) []*User
    Save(user *User)
}
