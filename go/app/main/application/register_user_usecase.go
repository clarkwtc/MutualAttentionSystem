package application

import (
    "mutualAttentionSystem/app/main/domain"
    "mutualAttentionSystem/app/main/infrastructure/repositories"
)

type RegisterUserUseCase struct {
    UserRepository domain.IUserRepository
}

func NewRegisterUserUseCase() *RegisterUserUseCase {
    return &RegisterUserUseCase{repositories.NewUserRepository()}
}

func (usecase *RegisterUserUseCase) Execute(username string) *domain.User {
    user := usecase.UserRepository.FindByUsername(username)

    if user != nil {
        return user
    }

    system := domain.NewMutualAttentionSysyem()
    system.AddUser(username)
    user = system.Users[0]

    usecase.UserRepository.Register(user)
    return user
}
