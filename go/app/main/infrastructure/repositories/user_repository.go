package repositories

import (
    "mutualAttentionSystem/app/main/domain"
    "mutualAttentionSystem/app/main/infrastructure/local"
)

type UserRepository struct {
    access *local.InMemoryUserRepository
}

func NewUserRepository() domain.IUserRepository {
    return &UserRepository{local.NewInMemoryUserRepository()}
}

func (repository *UserRepository) FindByUsername(username string) *domain.User {
    return repository.access.FindByUsername(username)
}

func (repository *UserRepository) Register(user *domain.User) {
    repository.access.Register(user)
}
