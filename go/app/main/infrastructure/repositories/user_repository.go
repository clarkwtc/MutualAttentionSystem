package repositories

import (
    "github.com/google/uuid"
    "mutualAttentionSystem/app/main/domain"
    "mutualAttentionSystem/app/main/infrastructure/local"
)

type UserRepository struct {
    access *local.InMemoryUserRepository
}

func NewUserRepository() domain.IUserRepository {
    return &UserRepository{local.NewInMemoryUserRepository()}
}

func (repository *UserRepository) Find(id uuid.UUID) *domain.User {
    return repository.access.Find(id)
}

func (repository *UserRepository) FindByIds(ids []uuid.UUID) []*domain.User {
    return repository.access.FindByIds(ids)
}

func (repository *UserRepository) FindByUsername(username string) *domain.User {
    return repository.access.FindByUsername(username)
}

func (repository *UserRepository) Save(user *domain.User) {
    repository.access.Save(user)
}
