package application

import (
	"mutualAttentionSystem/app/main/domain"
	"mutualAttentionSystem/app/main/domain/events"
)

type RegisterUserUseCase struct {
    UserRepository domain.IUserRepository
}

func (usecase *RegisterUserUseCase) Execute(username string) *events.GetUserEvent {
    user := usecase.UserRepository.FindByUsername(username)

    if user != nil {
        return &events.GetUserEvent{User: user}
    }

    system := domain.NewMutualAttentionSysyem()
    system.AddUserByUsername(username)
    user = system.Users[0]

    usecase.UserRepository.Save(user)
    return &events.GetUserEvent{User: user}
}
