package local

import "mutualAttentionSystem/app/main/domain"

type InMemoryUserRepository struct {
    users []*UserData
}

func NewInMemoryUserRepository() *InMemoryUserRepository {
    return &InMemoryUserRepository{}
}

func (repository *InMemoryUserRepository) FindByUsername(username string) *domain.User {
    for _, user := range repository.users {
        if user.Username == username {
            return toDomain(user)
        }
    }
    return nil
}

func (repository *InMemoryUserRepository) Register(user *domain.User) {
    userData := toData(user)
    repository.users = append(repository.users, userData)
}
