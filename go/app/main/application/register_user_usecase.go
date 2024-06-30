package application

import (
    "mutualAttentionSystem/app/main/domain"
)

type RegisterUserUseCase struct {
    UserRepository domain.IUserRepository
}

func (usecase *RegisterUserUseCase) Execute(username string) *domain.User {
    user := usecase.UserRepository.FindByUsername(username)

    if user != nil {
        return user
    }

    system := domain.NewMutualAttentionSysyem()
    system.AddUserByUsername(username)
    user = system.Users[0]

    usecase.UserRepository.Save(user)
    return user
}
