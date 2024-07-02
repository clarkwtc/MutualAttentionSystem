package application

import (
    "github.com/google/uuid"
    "mutualAttentionSystem/app/main/domain"
    "mutualAttentionSystem/app/main/domain/events"
)

type GetUserUseCase struct {
    UserRepository         domain.IUserRepository
    RelationshipRepository domain.IRelationshipRepository
}

func (usecase *GetUserUseCase) Execute(id string) *events.GetUserEvent {
    user := usecase.UserRepository.Find(uuid.MustParse(id))
    if user == nil {
        return nil
    }

    sysyem := domain.NewMutualAttentionSysyem()
    SetRelationshipsOfUser(user, usecase.UserRepository, usecase.RelationshipRepository)
    sysyem.AddUser(user)
    return &events.GetUserEvent{User: sysyem.GetUser(id)}
}
