package application

import (
    "github.com/google/uuid"
    "mutualAttentionSystem/app/main/domain"
    "mutualAttentionSystem/app/main/domain/events"
    "mutualAttentionSystem/app/main/domain/exceptions"
)

type GetUserUseCase struct {
    UserRepository         domain.IUserRepository
    RelationshipRepository domain.IRelationshipRepository
}

func (usecase *GetUserUseCase) Execute(id string) (*events.GetUserEvent, error) {
    user := usecase.UserRepository.Find(uuid.MustParse(id))
    if user == nil {
        return nil, &exceptions.NotExistUserError{}
    }
    
    sysyem := domain.NewMutualAttentionSysyem()
    sysyem.AddUser(user)
    SetRelationshipsOfUser(user, usecase.UserRepository, usecase.RelationshipRepository)
    return &events.GetUserEvent{User: sysyem.GetUser(id)}, nil
}
