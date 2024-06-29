package local

import (
    "github.com/google/uuid"
    "mutualAttentionSystem/app/main/domain"
)

type InMemoryUserRepository struct {
    users []*UserData
}

func NewInMemoryUserRepository() *InMemoryUserRepository {
    return &InMemoryUserRepository{}
}

func filterUserId(users []*UserData, id string) *UserData {
    for _, user := range users {
        if user.ID == id {
            return user
        }
    }
    return nil
}

func (repository *InMemoryUserRepository) Find(id uuid.UUID) *domain.User {
    userData := filterUserId(repository.users, id.String())
    if userData == nil {
        return nil
    }
    return toUserDomain(userData)
}

func (repository *InMemoryUserRepository) FindByIds(ids []uuid.UUID) []*domain.User {
    var users []*domain.User
    for _, id := range ids {
        userData := filterUserId(repository.users, id.String())
        if userData == nil {
            continue
        }

        users = append(users, toUserDomain(userData))
    }
    return users
}

func filterUsername(users []*UserData, username string) *UserData {
    for _, user := range users {
        if user.Username == username {
            return user
        }
    }
    return nil
}

func (repository *InMemoryUserRepository) FindByUsername(username string) *domain.User {
    userData := filterUsername(repository.users, username)
    if userData == nil {
        return nil
    }
    return toUserDomain(userData)
}

func (repository *InMemoryUserRepository) Save(user *domain.User) {
    userData := toUserData(user)
    repository.users = append(repository.users, userData)
}
