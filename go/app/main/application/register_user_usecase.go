package application

import (
    "mutualAttentionSystem/app/main/domain"
    "mutualAttentionSystem/app/main/domain/events"
    "mutualAttentionSystem/app/main/domain/exceptions"
)

type RegisterUserUseCase struct {
    UserRepository domain.IUserRepository
}

func (usecase *RegisterUserUseCase) Execute(username string) (*events.GetUserEvent, error) {
    user := usecase.UserRepository.FindByUsername(username)

    if user != nil {
        return nil, &exceptions.DuplicatedUserError{}
    }

    system := domain.NewMutualAttentionSysyem()
    system.AddUserByUsername(username)
    user = system.Users[0]

    usecase.UserRepository.Save(user)
    return &events.GetUserEvent{User: user}, nil
}
