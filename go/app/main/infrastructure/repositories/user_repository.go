package repositories

import (
    "github.com/google/uuid"
    "mutualAttentionSystem/app/main/domain"
)

type UserRepository struct {
    access domain.IUserRepository
}

func NewUserRepository(access domain.IUserRepository) domain.IUserRepository {
    return &UserRepository{access}
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
