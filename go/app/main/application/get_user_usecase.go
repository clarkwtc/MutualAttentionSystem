package application

import (
    "github.com/google/uuid"
    "mutualAttentionSystem/app/main/domain"
    "mutualAttentionSystem/app/main/infrastructure/repositories"
)

type GetUserUseCase struct {
    UserRepository         domain.IUserRepository
    RelationshipRepository domain.IRelationshipRepository
}

func NewGetUserUseCase() *GetUserUseCase {
    return &GetUserUseCase{repositories.NewUserRepository(),
        repositories.NewRelationshipRepository()}
}

func (usecase *GetUserUseCase) Execute(id string) *domain.User {
    ii := uuid.MustParse(id)
    user := usecase.UserRepository.Find(ii)
    if user == nil {
        return nil
    }

    sysyem := domain.NewMutualAttentionSysyem()
    relationships := usecase.RelationshipRepository.FindUserId(user.ID)
    userIds := getUsersIdByRelationships(relationships, user.ID)
    users := usecase.UserRepository.FindByIds(userIds)

    setUserInRelationships(relationships, users)
    sysyem.Users = []*domain.User{user}
    sysyem.GetUser(id).Relationships = relationships
    return sysyem.GetUser(id)
}

func getUsersIdByRelationships(relationships []*domain.Relationship, userId uuid.UUID) []uuid.UUID {
    var userIdsMap map[uuid.UUID]bool

    for _, relationship := range relationships {
        followingId := relationship.Following.ID
        fanId := relationship.Fan.ID

        if userId != followingId && !userIdsMap[followingId] {
            userIdsMap[followingId] = true
        }

        if userId != fanId && !userIdsMap[fanId] {
            userIdsMap[fanId] = true
        }
    }

    var userIds []uuid.UUID
    for key := range userIdsMap {
        userIds = append(userIds, key)
    }

    return userIds
}

func setUserInRelationships(relationships []*domain.Relationship, users []*domain.User) {
    for _, relationship := range relationships {
        for _, user := range users {
            if relationship.Fan.ID == user.ID {
                relationship.Fan = user
            }

            if relationship.Following.ID == user.ID {
                relationship.Following = user
            }
        }
    }
}
